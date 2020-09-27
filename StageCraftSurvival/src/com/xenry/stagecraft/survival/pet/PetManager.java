package com.xenry.stagecraft.survival.pet;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.pet.commands.TestSpawnCommand;
import net.minecraft.server.v1_16_R2.Entity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PetManager extends Manager<Survival> {
	
	public PetManager(Survival plugin) {
		super("Pets", plugin);
	}
	
	@Override
	protected void onEnable() {
		registerCommand(new TestSpawnCommand(this));
	}
	
	public void spawn(Entity entity, Location location){
		World world = location.getWorld();
		if(world == null){
			throw new IllegalArgumentException("World cannot be null");
		}
		((CraftWorld)world).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
	}
	
}
