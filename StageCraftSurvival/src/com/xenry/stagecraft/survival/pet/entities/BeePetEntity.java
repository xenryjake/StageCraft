package com.xenry.stagecraft.survival.pet.entities;
import net.minecraft.server.v1_16_R2.EntityBee;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

import java.util.Map;
import java.util.Set;

import static com.xenry.stagecraft.util.ReflectionUtil.getPrivateField;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class BeePetEntity extends EntityBee {
	
	public BeePetEntity(World world){
		super(EntityTypes.BEE, world);
		Map<?,?> goalB = (Map<?,?>)getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
		if(goalB != null){
			goalB.clear();
		}
		Set<?> goalC = (Set<?>)getPrivateField("d", PathfinderGoalSelector.class, goalSelector);
		if(goalC != null){
			goalC.clear();
		}
		
		Map<?,?> targetB = (Map<?,?>)getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
		if(targetB != null){
			targetB.clear();
		}
		Set<?> targetC = (Set<?>)getPrivateField("d", PathfinderGoalSelector.class, targetSelector);
		if(targetC != null){
			targetC.clear();
		}
	}
	
	public BeePetEntity(org.bukkit.World world){
		this(((CraftWorld)world).getHandle());
	}
	
	public BeePetEntity(Location location){
		this(location.getWorld());
		setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
}
