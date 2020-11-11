package com.xenry.stagecraft.creative.gameplay;
import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.entity.Entities;
import com.xenry.stagecraft.util.entity.EntityUtil;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import static com.xenry.stagecraft.util.entity.Entities.Category.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EntityHandler extends Handler<Creative,GameplayManager> {
	
	public EntityHandler(GameplayManager manager){
		super(manager);
	}
	
	/* *
	 * events to handle
	 * PAPER
	 * 	ThrownEggHatchEvent [done]
	 * 	TurtleLayEggEvent
	 * 	TurtleStartDiggingEvent
	 * 	WitchThrowPotionEvent
	 * BUKKIT
	 * 	CreatureSpawnEvent
	 * 	EntityCombustEvent
	 * 	EntityInteractEvent
	 * 	EntitySpawnEvent
	 *
	 */
	
	private CreatureSpawnReasonResponse getCreatureSpawnResp(CreatureSpawnEvent.SpawnReason spawnReason){
		switch(spawnReason){
			case SHEARED: // mooshroom is sheared to cow (net 0)
			case DROWNED: // zombie drowns (net 0)
			case CURED: // zombie vil cured (net 0)
			case INFECTION: // villager infected (net 0)
			case BEEHIVE: // bee leaving hive
			case SHOULDER_ENTITY: // parrot gets off shoulder
			case CUSTOM: // plugin does spawning
				// ignore (allow) these spawn reasons
				return CreatureSpawnReasonResponse.SPAWN;
			case DISPENSE_EGG: // thrown egg from dispenser
			case EGG: // thrown egg
			case ENDER_PEARL: // endermite from ender pearl
			case EXPLOSION: // area effect cloud creeper explosion
			case NATURAL: // normal spawning
			case PATROL: // illager patrol
			case RAID: // raid illagers
			case SILVERFISH_BLOCK: // infested block mined
			case VILLAGE_DEFENSE: // iron golems spawned by villagers
			case VILLAGE_INVASION: // zombies to attack villagers
			case NETHER_PORTAL: // mob gos thru nether portal
			case SPAWNER: // mob spawners
			case REINFORCEMENTS: // zombie or other mob calls reinforcements
			case BUILD_WITHER: // player builds wither
			case TRAP: // mob spawns as trap for player
			case JOCKEY: // mob spawns as jockey
			case MOUNT: // mob spawns as mount
			case OCELOT_BABY: // baby spawns WITH ocelot spawn
				// deny these spawn reasons
				return CreatureSpawnReasonResponse.CANCEL;
			case BREEDING: // baby
			case BUILD_IRONGOLEM: // player builds golem
			case BUILD_SNOWMAN: // player builds snowman
			case LIGHTNING: // lightning strike
			case SLIME_SPLIT: // a slime splits
			case SPAWNER_EGG: // player uses spawner egg
			case DEFAULT: // undefined reason, or armor stand, or /summon
			default: // idk something weird happened
				// limit these spawn reasons
				return CreatureSpawnReasonResponse.FILTER;
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(EntitySpawnEvent event){
		Entity entity = event.getEntity();
		Entities.Category category = Entities.get(entity).category;
		Chunk chunk = entity.getChunk();
		
		// disable all AreaEffectClouds and EnderCrystals
		if(entity instanceof AreaEffectCloud || entity instanceof EnderCrystal){
			event.setCancelled(true);
			return;
		}
		
		// absolute cap 128
		if(EntityUtil.countEntities(chunk) >= 128){
			event.setCancelled(true);
			return;
		}
		
		if(event instanceof CreatureSpawnEvent && !(entity instanceof ArmorStand)){
			CreatureSpawnReasonResponse response = getCreatureSpawnResp(((CreatureSpawnEvent)event).getSpawnReason());
			if(response == CreatureSpawnReasonResponse.CANCEL){
				event.setCancelled(true);
				return;
			}else if(response == CreatureSpawnReasonResponse.SPAWN){
				return;
			}
		}
		
		if(category == HANGING){ // cap 64 hanging entities per chunk (rt 64)
			if(EntityUtil.countEntities(chunk, HANGING) >= 64){
				event.setCancelled(true);
				return;
			}
		}else if(category == PROJECTILE){ // cap 16 projectiles per chunk (rt 84)
			if(EntityUtil.countEntities(chunk, PROJECTILE) >= 20){
				event.setCancelled(true);
				return;
			}
		}else if(category == VEHICLE){ // cap 8 vehicles per chunk (rt 92)
			if(EntityUtil.countEntities(chunk, VEHICLE) >= 8){
				event.setCancelled(true);
				return;
			}
		}else if(category == GENERIC){ // cap 8 generic entities per chunk (rt 100)
			if(EntityUtil.countEntities(chunk, GENERIC) >= 8){
				event.setCancelled(true);
				return;
			}
		}else if(category.isMob()){ // cap 20 mobs per chunk (rt 120)
			if(EntityUtil.countMobs(chunk) >= 20){
				event.setCancelled(true);
				return;
			}
		}
		
		// effectively prevent ArmorStand lag machines
		if(entity instanceof ArmorStand){
			entity.setGravity(false);
		}
	}
	
	@EventHandler
	public void on(VehicleCreateEvent event){
		Vehicle vehicle = event.getVehicle();
		Chunk chunk = vehicle.getChunk();
		
		// absolute cap 128
		if(EntityUtil.countEntities(chunk) >= 128){
			event.setCancelled(true);
			return;
		}
		
		// cap 8 vehicles per chunk (rt 92)
		if(EntityUtil.countEntities(chunk, VEHICLE) >= 8){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(ThrownEggHatchEvent event){
		event.setHatching(false);
	}
	
	private enum CreatureSpawnReasonResponse {
		CANCEL, FILTER, SPAWN
	}
	
	@EventHandler
	public void on(BlockDispenseEvent event){
		if(event.getBlock().getType() == Material.DISPENSER && event.getItem().getType().name().endsWith("SPAWN_EGG")){
			event.setCancelled(true);
		}
	}
	
}
