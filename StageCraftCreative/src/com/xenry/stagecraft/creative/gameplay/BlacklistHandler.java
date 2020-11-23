package com.xenry.stagecraft.creative.gameplay;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BlacklistHandler extends Handler<Creative,GameplayManager> {
	
	private static final List<Material> bannedMaterials = Arrays.asList(
			BARRIER, COMMAND_BLOCK_MINECART, END_CRYSTAL, DEBUG_STICK, DRAGON_EGG/*,
			COD_BUCKET, PUFFERFISH_BUCKET, SALMON_BUCKET, TROPICAL_FISH_BUCKET,
			BAT_SPAWN_EGG, BEE_SPAWN_EGG, BLAZE_SPAWN_EGG, CAT_SPAWN_EGG, CAVE_SPIDER_SPAWN_EGG, CHICKEN_SPAWN_EGG,
			COD_SPAWN_EGG, COW_SPAWN_EGG, CREEPER_SPAWN_EGG, DOLPHIN_SPAWN_EGG, DONKEY_SPAWN_EGG, DROWNED_SPAWN_EGG,
			ELDER_GUARDIAN_SPAWN_EGG, ENDERMAN_SPAWN_EGG, ENDERMITE_SPAWN_EGG, EVOKER_SPAWN_EGG, FOX_SPAWN_EGG,
			GHAST_SPAWN_EGG, GUARDIAN_SPAWN_EGG, HORSE_SPAWN_EGG, HUSK_SPAWN_EGG, LLAMA_SPAWN_EGG,
			MAGMA_CUBE_SPAWN_EGG, MOOSHROOM_SPAWN_EGG, MULE_SPAWN_EGG, OCELOT_SPAWN_EGG, PANDA_SPAWN_EGG,
			PARROT_SPAWN_EGG, PHANTOM_SPAWN_EGG, PIG_SPAWN_EGG, PILLAGER_SPAWN_EGG, POLAR_BEAR_SPAWN_EGG,
			PUFFERFISH_SPAWN_EGG, RABBIT_SPAWN_EGG, RAVAGER_SPAWN_EGG, SALMON_SPAWN_EGG, SHEEP_SPAWN_EGG,
			SHULKER_SPAWN_EGG, SILVERFISH_SPAWN_EGG, SKELETON_HORSE_SPAWN_EGG, SKELETON_SPAWN_EGG, SLIME_SPAWN_EGG,
			SPIDER_SPAWN_EGG, SQUID_SPAWN_EGG, STRAY_SPAWN_EGG, TRADER_LLAMA_SPAWN_EGG, TROPICAL_FISH_SPAWN_EGG,
			TURTLE_SPAWN_EGG, VEX_SPAWN_EGG, VILLAGER_SPAWN_EGG, VINDICATOR_SPAWN_EGG, WANDERING_TRADER_SPAWN_EGG,
			WITCH_SPAWN_EGG, WITHER_SKELETON_SPAWN_EGG, WOLF_SPAWN_EGG, ZOMBIE_HORSE_SPAWN_EGG,
			ZOMBIFIED_PIGLIN_SPAWN_EGG, ZOMBIE_SPAWN_EGG, ZOMBIE_VILLAGER_SPAWN_EGG, HOGLIN_SPAWN_EGG,
			PIGLIN_SPAWN_EGG, STRIDER_SPAWN_EGG, ZOGLIN_SPAWN_EGG*/);
	private static final List<Material> destroyOnlyMaterials = Arrays.asList(SPAWNER,
			INFESTED_CHISELED_STONE_BRICKS, INFESTED_COBBLESTONE, INFESTED_STONE,
			INFESTED_CRACKED_STONE_BRICKS, INFESTED_MOSSY_STONE_BRICKS, INFESTED_STONE_BRICKS,
			COMMAND_BLOCK, JIGSAW, NETHER_PORTAL, END_PORTAL, END_GATEWAY, STRUCTURE_BLOCK, STRUCTURE_VOID,
			CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK);
	
	private final Cooldown cooldown;
	
	public BlacklistHandler(GameplayManager manager) {
		super(manager);
		cooldown = new Cooldown(2000, null);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(manager.isLockoutMode()){
			event.setCancelled(true);
			sendLockoutMessage(player);
			return;
		}
		if(manager.isPlayerOverride(player)) {
			return;
		}
		
		Block block = event.getBlock();
		if(block.getType() == Material.BEACON){
			for(int modX = -1; modX <= 1; modX++){
				for(int modZ = -1; modZ <= 1; modZ++){
					Chunk chunk = block.getWorld().getChunkAt(block.getChunk().getX()+modX, block.getChunk().getZ()+modZ);
					for(BlockState bs : chunk.getTileEntities()){
						if(bs instanceof Beacon){
							event.setCancelled(true);
							player.sendMessage(M.error("There is already a beacon nearby."));
							return;
						}
					}
				}
			}
		}
		
		if(bannedMaterials.contains(event.getItemInHand().getType())
				|| bannedMaterials.contains(event.getBlock().getType())
				|| destroyOnlyMaterials.contains(event.getItemInHand().getType())
				|| destroyOnlyMaterials.contains(event.getBlock().getType())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't place that.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(manager.isLockoutMode()){
			event.setCancelled(true);
			sendLockoutMessage(player);
			return;
		}
		if(manager.isPlayerOverride(player)){
			return;
		}
		if(event.getBlock().getY() < 1){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't break blocks here.");
		}
		
		if(bannedMaterials.contains(event.getBlock().getType())){
			event.setCancelled(true);
			sendMessage(player, M.err + "You can't break that.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(InventoryClickEvent event){
		HumanEntity he = event.getWhoClicked();
		if(!(he instanceof Player)){
			return;
		}
		Player player = (Player)he;
		if(manager.isLockoutMode()){
			event.setCancelled(true);
			sendLockoutMessage(player);
			return;
		}
		if(manager.isPlayerOverride(player)){
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
			sendMessage(player, M.err + "You can't do that.");
			player.closeInventory();
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(InventoryInteractEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(manager.isLockoutMode()){
			event.setCancelled(true);
			sendLockoutMessage(player);
			return;
		}
		if(manager.isPlayerOverride(player)){
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
		if(manager.isLockoutMode()){
			event.setCancelled(true);
			sendLockoutMessage(player);
			return;
		}
		if(manager.isPlayerOverride(player)){
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
	
	@EventHandler
	public void on(PortalCreateEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	private void sendMessage(Player player, String message){
		if(!cooldown.use(player, false)){
			return;
		}
		player.sendMessage(message);
	}
	
	private void sendLockoutMessage(Player player){
		if(!cooldown.use(player, false)){
			return;
		}
		player.sendMessage(M.err + "Lockout mode is enabled. You can't interact with things right now.");
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockFromToEvent event){
		if(manager.isLockoutMode() || event.getBlock().getType() == DRAGON_EGG){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockIgniteEvent event){
		switch(event.getCause()){
			case FLINT_AND_STEEL:
				return;
			case LAVA:
			case SPREAD:
			case LIGHTNING:
			case FIREBALL:
			case ENDER_CRYSTAL:
			case EXPLOSION:
			case ARROW:
			default:
				event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockPistonExtendEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockPistonRetractEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	// dispenser shears sheep
	@EventHandler(ignoreCancelled = true)
	public void on(BlockShearEntityEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(CauldronLevelChangeEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(FluidLevelChangeEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	/*@EventHandler(ignoreCancelled = true)
	public void on(LeavesDecayEvent event){
		event.setCancelled(true);
	}*/
	
	@EventHandler(ignoreCancelled = true)
	public void on(MoistureChangeEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(NotePlayEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(SpongeAbsorbEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(InventoryPickupItemEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
			event.getItem().remove(); // prevent lag
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockGrowEvent event){
		if(manager.isLockoutMode()){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockSpreadEvent event){
		Material type = event.getNewState().getType();
		Log.debug("BSE " + type.name() + " / " + event.getSource().getType().name() + " / " + event.getBlock().getType().name());
		if(manager.isLockoutMode() || type == VINE || type == TWISTING_VINES || type == TWISTING_VINES_PLANT
				|| type == WEEPING_VINES || type == WEEPING_VINES_PLANT || type == MUSHROOM_STEM
				|| type == RED_MUSHROOM || type == RED_MUSHROOM_BLOCK || type == BROWN_MUSHROOM
				|| type == BROWN_MUSHROOM_BLOCK || type == MYCELIUM || type == BAMBOO || type == BAMBOO_SAPLING){
			event.setCancelled(true);
		}
	}
	
}
