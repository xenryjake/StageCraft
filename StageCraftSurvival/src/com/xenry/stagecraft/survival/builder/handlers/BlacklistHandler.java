package com.xenry.stagecraft.survival.builder.handlers;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public class BlacklistHandler extends Handler<Survival,BuilderManager> {
	
	private final Cooldown cooldown;
	
	private static final List<Material> bannedMaterials = Arrays.asList(BARRIER, ENDER_EYE,
			COMMAND_BLOCK_MINECART, BAT_SPAWN_EGG, BEE_SPAWN_EGG, DRAGON_EGG,
			BLAZE_SPAWN_EGG, CAT_SPAWN_EGG, CAVE_SPIDER_SPAWN_EGG, CHICKEN_SPAWN_EGG, COD_SPAWN_EGG,
			COW_SPAWN_EGG, CREEPER_SPAWN_EGG, DOLPHIN_SPAWN_EGG, DONKEY_SPAWN_EGG, DROWNED_SPAWN_EGG,
			ELDER_GUARDIAN_SPAWN_EGG, ENDERMAN_SPAWN_EGG, ENDERMITE_SPAWN_EGG, EVOKER_SPAWN_EGG,
			FOX_SPAWN_EGG, GHAST_SPAWN_EGG, GUARDIAN_SPAWN_EGG, HORSE_SPAWN_EGG, HUSK_SPAWN_EGG,
			LLAMA_SPAWN_EGG, MAGMA_CUBE_SPAWN_EGG, MOOSHROOM_SPAWN_EGG, MULE_SPAWN_EGG, OCELOT_SPAWN_EGG,
			PANDA_SPAWN_EGG, PARROT_SPAWN_EGG, PHANTOM_SPAWN_EGG, PIG_SPAWN_EGG, PILLAGER_SPAWN_EGG,
			POLAR_BEAR_SPAWN_EGG, PUFFERFISH_SPAWN_EGG, RABBIT_SPAWN_EGG, RAVAGER_SPAWN_EGG,
			SALMON_SPAWN_EGG, SHEEP_SPAWN_EGG, SHULKER_SPAWN_EGG, SILVERFISH_SPAWN_EGG,
			SKELETON_HORSE_SPAWN_EGG, SKELETON_SPAWN_EGG, SLIME_SPAWN_EGG, SPIDER_SPAWN_EGG,
			SQUID_SPAWN_EGG, STRAY_SPAWN_EGG, TRADER_LLAMA_SPAWN_EGG, TROPICAL_FISH_SPAWN_EGG,
			TURTLE_SPAWN_EGG, VEX_SPAWN_EGG, VILLAGER_SPAWN_EGG, VINDICATOR_SPAWN_EGG,
			WANDERING_TRADER_SPAWN_EGG, WITCH_SPAWN_EGG, WITHER_SKELETON_SPAWN_EGG, WOLF_SPAWN_EGG,
			ZOMBIE_HORSE_SPAWN_EGG, ZOMBIFIED_PIGLIN_SPAWN_EGG, ZOMBIE_SPAWN_EGG, ZOMBIE_VILLAGER_SPAWN_EGG,
			HOGLIN_SPAWN_EGG, PIGLIN_SPAWN_EGG, STRIDER_SPAWN_EGG, ZOGLIN_SPAWN_EGG,
			COD_BUCKET, PUFFERFISH_BUCKET, SALMON_BUCKET, TROPICAL_FISH_BUCKET, END_CRYSTAL, DEBUG_STICK);
	private static final List<Material> destroyOnlyMaterials = Arrays.asList(SPAWNER,
			INFESTED_CHISELED_STONE_BRICKS, INFESTED_COBBLESTONE, INFESTED_STONE,
			INFESTED_CRACKED_STONE_BRICKS, INFESTED_MOSSY_STONE_BRICKS, INFESTED_STONE_BRICKS,
			COMMAND_BLOCK, JIGSAW, NETHER_PORTAL, END_PORTAL, END_GATEWAY, STRUCTURE_BLOCK, STRUCTURE_VOID,
			CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK, BEACON);
	
	public BlacklistHandler(BuilderManager manager){
		super(manager);
		cooldown = new Cooldown(2000, null);
	}
	
	@EventHandler
	public void on(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(!manager.isBuilder(player)){
			if(!manager.isValidNonBuilderLocation(player)){
				event.setCancelled(true);
			}
			return;
		}
		if(!manager.isValidBuilderLocation(player)){
			event.setCancelled(true);
			return;
		}
		
		if(bannedMaterials.contains(event.getItemInHand().getType())
				|| bannedMaterials.contains(event.getBlock().getType())
				|| destroyOnlyMaterials.contains(event.getItemInHand().getType())
				|| destroyOnlyMaterials.contains(event.getBlock().getType())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't place that.");
		}else if(!manager.isInBuildArea(event.getBlock())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't build here while in builder mode.");
		}
	}
	
	@EventHandler
	public void on(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(!manager.isBuilder(player)){
			if(!manager.isValidNonBuilderLocation(player)){
				event.setCancelled(true);
			}
			return;
		}
		if(!manager.isValidBuilderLocation(player)){
			event.setCancelled(true);
			return;
		}
		
		if(bannedMaterials.contains(event.getBlock().getType())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't break that.");
		}else if(!manager.isInBuildArea(event.getBlock())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't build here while in builder mode.");
		}
	}
	
	@EventHandler
	public void on(InventoryClickEvent event){
		HumanEntity he = event.getWhoClicked();
		if(!(he instanceof Player)){
			return;
		}
		Player player = (Player)he;
		if(!manager.isBuilder(player)){
			if(!manager.isValidNonBuilderLocation(player)){
				event.setCancelled(true);
			}
			return;
		}
		if(!manager.isValidBuilderLocation(player)){
			event.setCancelled(true);
			return;
		}
		
		boolean allowed = true;
		if(event.getCurrentItem() != null && (bannedMaterials.contains(event.getCurrentItem().getType())
				|| destroyOnlyMaterials.contains(event.getCurrentItem().getType()))){
			allowed = false;
		}else if(event.getCursor() != null && (bannedMaterials.contains(event.getCursor().getType())
				|| destroyOnlyMaterials.contains(event.getCursor().getType()))){
			allowed = false;
		}
		if(!allowed){
			event.setCancelled(true);
			sendMessage(player,M.err + "You can't do that.");
			player.closeInventory();
		}
	}
	
	@EventHandler
	public void on(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(!manager.isBuilder(player)){
			if(!manager.isValidNonBuilderLocation(player)){
				event.setCancelled(true);
			}
			return;
		}
		if(!manager.isValidBuilderLocation(player)){
			event.setCancelled(true);
			return;
		}
		
		if(!manager.isInBuildArea(player)){
			manager.removeBuilder(player);
			event.setCancelled(true);
			sendMessage(player, M.err + "Your builder mode was deactivated because you left the build area.");
			return;
		}
		if(bannedMaterials.contains(event.getItemDrop().getItemStack().getType())
				|| destroyOnlyMaterials.contains(event.getItemDrop().getItemStack().getType())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't drop that.");
		}
	}
	
	@EventHandler
	public void on(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(!manager.isBuilder(player)){
			if(!manager.isValidNonBuilderLocation(player)){
				event.setCancelled(true);
			}
			return;
		}
		if(!manager.isValidBuilderLocation(player)){
			event.setCancelled(true);
			return;
		}
		
		if(bannedMaterials.contains(event.getMaterial())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't interact with that.");
			return;
		}
		if(event.getClickedBlock() != null && (bannedMaterials.contains(event.getClickedBlock().getType()))){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't interact with that.");
		}
	}
	
	private void sendMessage(Player player, String message){
		if(!cooldown.use(player, false)){
			return;
		}
		player.sendMessage(message);
	}
	
}
