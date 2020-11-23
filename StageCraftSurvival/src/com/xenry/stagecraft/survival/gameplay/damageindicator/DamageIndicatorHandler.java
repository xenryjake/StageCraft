package com.xenry.stagecraft.survival.gameplay.damageindicator;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.event.FakeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.DecimalFormat;
import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/7/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DamageIndicatorHandler extends Handler<Survival,GameplayManager> {
	
	public static final String METADATA_KEY = "SC-DAMAGE-INDICATOR";
	public static final FixedMetadataValue METADATA_VALUE = new FixedMetadataValue(Core.getInstance(), 0);
	
	private static final List<DamageCause> DISABLED_CAUSES = Collections.emptyList();
	private static final List<EntityType> DISABLED_ENTITIES = Collections.emptyList();
	
	private final Map<ArmorStand,Long> armorStands = new LinkedHashMap<>();
	private final DecimalFormat df = new DecimalFormat("#.#");
	
	public DamageIndicatorHandler(GameplayManager manager){
		super(manager);
		Bukkit.getScheduler().runTaskTimer(manager.plugin, () -> {
			Iterator<Map.Entry<ArmorStand,Long>> it = armorStands.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<ArmorStand,Long> entry = it.next();
				if(entry.getValue() + 1500 <= System.currentTimeMillis()){
					entry.getKey().remove();
					it.remove();
				}else{
					entry.getKey().teleport(entry.getKey().getLocation().clone().add(0, 0.07, 0));
				}
			}
		}, 0, 1);
	}
	
	public boolean isDamageIndicator(Entity entity){
		if(!(entity instanceof ArmorStand)){
			return false;
		}
		ArmorStand as = (ArmorStand)entity;
		return as.hasMetadata(METADATA_KEY) && as.isMarker() && !as.isVisible() && as.isCustomNameVisible()
				&& !as.hasGravity();
	}
	
	public boolean isDamageIndicatorUnregistered(Entity entity){
		if(isDamageIndicator(entity)){
			return true;
		}
		if(!(entity instanceof ArmorStand)){
			return false;
		}
		ArmorStand as = (ArmorStand)entity;
		if(!as.isMarker() || as.isVisible() || !as.isCustomNameVisible() || as.hasGravity() || !as.isSmall()
				|| as.isCollidable() || !as.isInvulnerable()){
			return false;
		}
		String name = as.getCustomName();
		if(name == null || (!name.startsWith("§c") && !name.startsWith("§a"))){
			return false;
		}
		try{
			name = name.substring(2);
			Double.parseDouble(name);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	//@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityRegainHealth(EntityRegainHealthEvent event){
		Entity entity = event.getEntity();
		if(!(entity instanceof LivingEntity)){
			return;
		}
		AttributeInstance ai = ((LivingEntity)entity).getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if(ai != null && ai.getValue() == ((LivingEntity)entity).getHealth()){
			return;
		}
		spawnArmorStand((LivingEntity)entity, event.getAmount(), null, true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event){
		if(event instanceof FakeEvent){
			return;
		}
		Entity entity = event.getEntity();
		if(!(entity instanceof LivingEntity)){
			return;
		}
		spawnArmorStand((LivingEntity)entity, event.getFinalDamage(), event.getCause(), false);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntitySpawn(EntitySpawnEvent event){
		if(event.isCancelled() && isDamageIndicator(event.getEntity())){
			event.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChunkUnload(ChunkUnloadEvent event){
		for(Entity entity : event.getChunk().getEntities()){
			if(isDamageIndicator(entity)){
				ArmorStand as = (ArmorStand)entity;
				armorStands.remove(as);
				as.remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChunkUnload(ChunkLoadEvent event){
		for(Entity entity : event.getChunk().getEntities()){
			if(isDamageIndicator(entity)){
				ArmorStand as = (ArmorStand)entity;
				armorStands.remove(as);
				as.remove();
			}
		}
	}
	
	public boolean showIndicator(Entity entity, double damage, DamageCause cause){
		return damage > 0
				&& entity instanceof LivingEntity
				&& !(entity instanceof ArmorStand)
				&& !(DISABLED_CAUSES.contains(cause))
				&& !(DISABLED_ENTITIES.contains(entity.getType()));
	}
	
	public void spawnArmorStand(LivingEntity entity, double damage, DamageCause cause, boolean regen){
		if(!showIndicator(entity, damage, cause)){
			return;
		}
		Location loc = entity.getLocation();
		World world = loc.getWorld();
		if(world == null){
			return;
		}
		ArmorStand stand = world.spawn(loc.clone().add(0, world.getMaxHeight() - loc.getY(), 0),
				ArmorStand.class);
		stand.setVisible(false);
		stand.setGravity(false);
		stand.setMarker(true);
		stand.setSmall(true);
		stand.setCustomNameVisible(false);
		stand.setMetadata(METADATA_KEY, METADATA_VALUE);
		stand.setCollidable(false);
		stand.setInvulnerable(true);
		stand.teleport(loc.clone().add(0, 1.6, 0));
		stand.setRemoveWhenFarAway(true);
		stand.setCustomName((regen ? "§a" : "§c") + df.format(damage));
		stand.setCustomNameVisible(true);
		armorStands.put(stand, System.currentTimeMillis());
	}
	
}
