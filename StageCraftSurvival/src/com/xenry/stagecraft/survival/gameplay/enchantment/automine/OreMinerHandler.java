package com.xenry.stagecraft.survival.gameplay.enchantment.automine;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.event.FakeBlockBreakEvent;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
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

import static com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment.ORE_MINER;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/31/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class OreMinerHandler extends Handler<Survival,GameplayManager> {
	
	private static final int maxLoopSize = 128;
	
	public OreMinerHandler(GameplayManager manager){
		super(manager);
	}
	
	public enum OreFlavor {
		COAL, IRON, GOLD, NETHER_GOLD, LAPIS, REDSTONE, DIAMOND, EMERALD, NETHERITE, QUARTZ, NONE
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
		if(!isOre(block)){
			return;
		}
		
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(!hasOreMinerEnchantment(item)){
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
		if(blockList.size() >= maxLoopSize || getFlavor(block) != getFlavor(original) || !isOre(block) || blockList.containsKey(block)){
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
	
	public OreFlavor getFlavor(Material material){
		switch(material){
			case COAL_ORE:
				return OreFlavor.COAL;
			case IRON_ORE:
				return OreFlavor.IRON;
			case GOLD_ORE:
				return OreFlavor.GOLD;
			case NETHER_GOLD_ORE:
				return OreFlavor.NETHER_GOLD;
			case LAPIS_ORE:
				return OreFlavor.LAPIS;
			case REDSTONE_ORE:
				return OreFlavor.REDSTONE;
			case DIAMOND_ORE:
				return OreFlavor.DIAMOND;
			case EMERALD_ORE:
				return OreFlavor.EMERALD;
			case ANCIENT_DEBRIS:
				return OreFlavor.NETHERITE;
			case NETHER_QUARTZ_ORE:
				return OreFlavor.QUARTZ;
			default:
				return OreFlavor.NONE;
		}
	}
	
	public OreFlavor getFlavor(Block block){
		return getFlavor(block.getType());
	}
	
	public boolean hasOreMinerEnchantment(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return false;
		}
		return item.getItemMeta().getEnchantLevel(ORE_MINER) > 0;
	}
	
	public boolean isOre(Block block){
		return isOre(block.getType());
	}
	
	public boolean isOre(Material material){
		return getFlavor(material) != OreFlavor.NONE;
	}
	
	/*
	 * level 1: breaks up to 4 blocks in a 4 block radius
	 * level 2: breaks up to 8 blocks in an 5 block radius
	 * level 3: breaks up to 12 blocks in a 6 block radius
	 * level 4: breaks up to 16 blocks in a 7 block radius
	 * level 5: breaks up to 20 blocks in an 8 block radius
	 */
	
	public static int getMaxDistance(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return 0;
		}
		return getMaxDistance(item.getItemMeta().getEnchantLevel(ORE_MINER));
	}
	
	public static int getMaxDistance(int level){
		if(level > ORE_MINER.getMaxLevel()){
			level = ORE_MINER.getMaxLevel();
		}
		return level < 1 ? 0 : level + 3;
	}
	
	public int getMaxBlocks(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return 0;
		}
		return getMaxBlocks(item.getItemMeta().getEnchantLevel(ORE_MINER));
	}
	
	public int getMaxBlocks(int level){
		if(level > ORE_MINER.getMaxLevel()){
			level = ORE_MINER.getMaxLevel();
		}
		return level < 1 ? 0 : level * 4;
	}
	
}
