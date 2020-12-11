package com.xenry.stagecraft.survival.gameplay.enchantment.specialitem;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.BlockRelative;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/5/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SpecialItemHandler extends Handler<Survival,GameplayManager> {
	
	private static final List<Material> specialItemInteractableBlockMaterials = Arrays.asList(
			CHEST, TRAPPED_CHEST, HOPPER, CRAFTING_TABLE, ENDER_CHEST, HOPPER, SHULKER_BOX, BARREL,
			OAK_BUTTON, BIRCH_BUTTON, JUNGLE_BUTTON, SPRUCE_BUTTON, ACACIA_BUTTON, DARK_OAK_BUTTON, STONE_BUTTON,
			OAK_DOOR, BIRCH_DOOR, JUNGLE_DOOR, SPRUCE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, IRON_DOOR,
			OAK_FENCE_GATE, BIRCH_FENCE_GATE, JUNGLE_FENCE_GATE, SPRUCE_FENCE_GATE, ACACIA_FENCE_GATE, DARK_OAK_FENCE_GATE,
			LEVER, BEACON, REPEATER, COMPARATOR, COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK,
			DAYLIGHT_DETECTOR, NOTE_BLOCK, DISPENSER, DROPPER, FURNACE, BLAST_FURNACE, SMOKER, ENCHANTING_TABLE,
			OAK_TRAPDOOR, BIRCH_TRAPDOOR, JUNGLE_TRAPDOOR, SPRUCE_TRAPDOOR, ACACIA_TRAPDOOR, DARK_OAK_TRAPDOOR,
			ANVIL, CHIPPED_ANVIL, DAMAGED_ANVIL, LOOM, CARTOGRAPHY_TABLE, BELL, STONECUTTER, GRINDSTONE, BREWING_STAND,
			WHITE_BED, ORANGE_BED, MAGENTA_BED, LIGHT_BLUE_BED, YELLOW_BED, LIME_BED, PINK_BED, GRAY_BED,
			LIGHT_GRAY_BED, CYAN_BED, PURPLE_BED, BLUE_BED, BROWN_BED, GREEN_BED, RED_BED, BLACK_BED,
			WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX,
			LIME_SHULKER_BOX, PINK_SHULKER_BOX, GRAY_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, CYAN_SHULKER_BOX,
			PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX
	);
	
	public SpecialItemHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler
	public void onSpecialItem(BlockPlaceEvent event){
		if(event.getItemInHand().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(PlayerInteractEvent event){
		if(event.getItem() == null){
			return;
		}
		Block block = event.getClickedBlock();
		if(block != null && specialItemInteractableBlockMaterials.contains(block.getType())){
			return;
		}
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(InventoryMoveItemEvent event){
		if(isValidSpecialItemInventory(event.getDestination()) && !isHopperTransferToComposter(event)){
			return;
		}
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	private boolean isHopperTransferToComposter(InventoryMoveItemEvent event){
		if(event.getSource().getType() != InventoryType.HOPPER || !(event.getSource().getHolder() instanceof Hopper)){
			return false;
		}
		Location loc = event.getSource().getLocation();
		if(loc == null){
			return false;
		}
		Block block = loc.getBlock();
		BlockData data = block.getBlockData();
		if(!(data instanceof Directional)){
			return false;
		}
		Directional dir = (Directional) data;
		Block destination = block.getLocation().clone().add(BlockRelative.valueOf(dir.getFacing().name()).getVector()).getBlock();
		return destination.getType() == COMPOSTER;
	}
	
	@EventHandler
	public void onSpecialItem(InventoryClickEvent event){
		if(isValidSpecialItemInventory(event.getInventory())){
			return;
		}
		if(event.getCursor() != null && event.getCursor().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
		if(event.getCurrentItem() != null && event.getCurrentItem().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(BlockCookEvent event){
		if(event.getSource().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(BlockDispenseEvent event){
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(BrewingStandFuelEvent event){
		if(event.getFuel().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(FurnaceBurnEvent event){
		if(event.getFuel().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
			return;
		}
		Block block = event.getBlock();
		if(!(block instanceof Furnace)){
			return;
		}
		Furnace furnace = (Furnace)block;
		ItemStack item = furnace.getInventory().getSmelting();
		if(item == null){
			return;
		}
		if(item.getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(PlayerBucketFillEvent event){
		if(event.getItemStack() == null){
			return;
		}
		if(event.getItemStack().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(PlayerBucketEmptyEvent event){
		if(event.getItemStack() == null){
			return;
		}
		if(event.getItemStack().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(PlayerItemConsumeEvent event){
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(PlayerItemDamageEvent event){
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(PlayerItemMendEvent event){
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpecialItem(CraftItemEvent event){
		for(ItemStack item : event.getInventory().getMatrix()){
			if(item != null && item.getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0){
				event.setCancelled(true);
				return;
			}
		}
	}
	
	public boolean isValidSpecialItemInventory(@NotNull Inventory inventory){
		switch(inventory.getType()){
			case CHEST:
			case WORKBENCH:
			case CRAFTING:
			case PLAYER:
			case CREATIVE:
			case ENDER_CHEST:
			case HOPPER:
			case SHULKER_BOX:
			case MERCHANT:
			case BARREL:
				return true;
			case DISPENSER:
			case DROPPER:
			case FURNACE:
			case ENCHANTING:
			case BREWING:
			case ANVIL:
			case BEACON:
			case BLAST_FURNACE:
			case LECTERN:
			case SMOKER:
			case LOOM:
			case CARTOGRAPHY:
			case GRINDSTONE:
			case STONECUTTER:
			default:
				return false;
		}
	}
	
}
