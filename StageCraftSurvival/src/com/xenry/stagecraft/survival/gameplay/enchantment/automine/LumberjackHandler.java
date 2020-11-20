package com.xenry.stagecraft.survival.gameplay.enchantment.automine;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.event.FakeBlockBreakEvent;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.util.BlockRelative;
import com.xenry.stagecraft.util.ItemUtil;
import com.xenry.stagecraft.util.MapUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment.LUMBERJACK;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/31/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class LumberjackHandler extends Handler<Survival,GameplayManager> {
	
	private static final int maxLoopSize = 128;
	
	public LumberjackHandler(GameplayManager manager){
		super(manager);
	}
	
	public enum WoodFlavor {
		OAK, BIRCH, SPRUCE, JUNGLE, ACACIA, DARK_OAK, CRIMSON, WARPED, NONE
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockBreakEvent event){
		if(event instanceof AutoMineBreakEvent || event instanceof FakeBlockBreakEvent){
			return;
		}
		Player player = event.getPlayer();
		if(player.getGameMode() != GameMode.SURVIVAL){
			return;
		}
		Block block = event.getBlock();
		if(!isWood(block)){
			return;
		}
		
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(!hasLumberjackEnchantment(item)){
			return;
		}
		
		int maxDistance = getMaxDistance(item);
		Map<Block,Double> blocksRaw = new HashMap<>();
		treeFlood(blocksRaw, block, block, maxDistance);
		Map<Block,Double> blocksByDistance = MapUtil.sortByValue(blocksRaw);
		
		int maxBlocks = getMaxBlocks(item);
		int i = 0;
		int total = 0;
		for(Block b : blocksByDistance.keySet()){
			i++;
			if(i > maxBlocks) break;
			BlockBreakEvent ambe = new AutoMineBreakEvent(b, player);
			manager.plugin.getServer().getPluginManager().callEvent(ambe);
			if(ambe.isCancelled()){
				continue;
			}
			b.breakNaturally();
			total++;
		}
		ItemUtil.damageItem(item, total);
	}
	
	public void treeFlood(Map<Block,Double> blockList, Block block, Block original, int maxDistance){
		if(blockList.size() >= maxLoopSize || getFlavor(block) != getFlavor(original) || !isWood(block) || blockList.containsKey(block)){
			return;
		}
		double distance = block.getLocation().distance(original.getLocation());
		if(distance > maxDistance){
			return;
		}
		blockList.put(block, distance);
		for(BlockRelative br : BlockRelative.values()){
			Block relative = block.getRelative(br.getX(), br.getY(), br.getZ());
			if(getFlavor(relative.getType()) == getFlavor(block)){
				treeFlood(blockList, relative, original, maxDistance);
			}
		}
	}
	
	public WoodFlavor getFlavor(Material material){
		switch(material){
			case OAK_LOG:
			case OAK_WOOD:
				return WoodFlavor.OAK;
			case BIRCH_LOG:
			case BIRCH_WOOD:
				return WoodFlavor.BIRCH;
			case SPRUCE_LOG:
			case SPRUCE_WOOD:
				return WoodFlavor.SPRUCE;
			case JUNGLE_LOG:
			case JUNGLE_WOOD:
				return WoodFlavor.JUNGLE;
			case ACACIA_LOG:
			case ACACIA_WOOD:
				return WoodFlavor.ACACIA;
			case DARK_OAK_LOG:
			case DARK_OAK_WOOD:
				return WoodFlavor.DARK_OAK;
			case CRIMSON_STEM:
			case CRIMSON_HYPHAE:
				return WoodFlavor.CRIMSON;
			case WARPED_STEM:
			case WARPED_HYPHAE:
				return WoodFlavor.WARPED;
			default:
				return WoodFlavor.NONE;
		}
	}
	
	public WoodFlavor getFlavor(Block block){
		return getFlavor(block.getType());
	}
	
	public boolean hasLumberjackEnchantment(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return false;
		}
		return item.getItemMeta().getEnchantLevel(LUMBERJACK) > 0;
	}
	
	public boolean isWood(Block block){
		return isWood(block.getType());
	}
	
	public boolean isWood(Material material){
		return getFlavor(material) != WoodFlavor.NONE;
	}
	
	/*
	 * level 1: breaks up to 8 blocks in a 7 block radius
	 * level 2: breaks up to 16 blocks in an 8 block radius
	 * level 3: breaks up to 24 blocks in a 9 block radius
	 * level 4: breaks up to 32 blocks in a 10 block radius
	 * level 5: breaks up to 40 blocks in an 11 block radius
	 */
	
	public static int getMaxDistance(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return 0;
		}
		return getMaxDistance(item.getItemMeta().getEnchantLevel(CustomEnchantment.LUMBERJACK));
	}
	
	public static int getMaxDistance(int level){
		if(level > LUMBERJACK.getMaxLevel()){
			level = LUMBERJACK.getMaxLevel();
		}
		return level < 1 ? 0 : level + 5;
	}
	
	public int getMaxBlocks(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return 0;
		}
		return getMaxBlocks(item.getItemMeta().getEnchantLevel(CustomEnchantment.LUMBERJACK));
	}
	
	public int getMaxBlocks(int level){
		if(level > LUMBERJACK.getMaxLevel()){
			level = LUMBERJACK.getMaxLevel();
		}
		return level < 1 ? 0 : level * 8;
	}
	
}
