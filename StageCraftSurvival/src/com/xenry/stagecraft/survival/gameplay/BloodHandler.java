package com.xenry.stagecraft.survival.gameplay;
import com.google.common.annotations.Beta;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/7/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@Beta
public final class BloodHandler extends Handler<Survival,GameplayManager> {
	
	private static final String NO_BLOOD_METADATA = "SC-NO-BLOOD";
	private static final List<DamageCause> DISABLED_CAUSES = Arrays.asList(DamageCause.CRAMMING, DamageCause.DROWNING,
			DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.HOT_FLOOR, DamageCause.LAVA, DamageCause.MELTING,
			DamageCause.SUICIDE, DamageCause.VOID);
	private static final List<EntityType> DISABLED_ENTITIES = Arrays.asList(EntityType.SKELETON,
			EntityType.SKELETON_HORSE, EntityType.WITHER_SKELETON);
	
	public BloodHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		if(showBloodForEntity(entity, event.getDamage(), event.getCause())){
			entity.getWorld().spawnParticle(Particle.REDSTONE, ((LivingEntity)entity).getEyeLocation(),
					7, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.RED, 1f));
		}
	}
	
	public boolean showBloodForEntity(Entity entity, double damage, DamageCause cause){
		return !entity.hasMetadata(NO_BLOOD_METADATA)
				&& damage > 0
				&& entity instanceof LivingEntity
				&& !(entity instanceof ArmorStand)
				&& !(DISABLED_CAUSES.contains(cause))
				&& !(DISABLED_ENTITIES.contains(entity.getType()));
	}
	
}
