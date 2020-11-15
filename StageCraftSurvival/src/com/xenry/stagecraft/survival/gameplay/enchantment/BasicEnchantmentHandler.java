package com.xenry.stagecraft.survival.gameplay.enchantment;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/7/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class BasicEnchantmentHandler extends Handler<Survival,GameplayManager> {
	
	public BasicEnchantmentHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler
	public void onAnvil(PrepareAnvilEvent event){
		//if item in first slot [i0] is null/air, return
		ItemStack i0 = event.getInventory().getItem(0);
		if(i0 == null || i0.getType() == Material.AIR){
			return;
		}
		
		//if item in first slot has SPECIAL_ITEM or GRAPPLING_HOOK, disallow the anvil use
		if(i0.getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0 || i0.getEnchantmentLevel(CustomEnchantment.GRAPPLING_HOOK) != 0){
			event.setResult(null);
			event.getInventory().setRepairCost(0);
			return;
		}
		
		//if item in second slot [i1] is null/air, return
		ItemStack i1 = event.getInventory().getItem(1);
		if(i1 == null || i1.getType() == Material.AIR){
			return;
		}
		
		//if either item has SPECIAL_ITEM or GRAPPLING_HOOK, disallow the anvil use
		if(i1.getEnchantmentLevel(CustomEnchantment.SPECIAL_ITEM) != 0 || i1.getEnchantmentLevel(CustomEnchantment.GRAPPLING_HOOK) != 0){
			event.setResult(null);
			event.getInventory().setRepairCost(0);
			return;
		}
		
		//if the second item isn't an enchanted book AND the items are not the same type, return
		if(i1.getType() != Material.ENCHANTED_BOOK && i0.getType() != i1.getType()){
			return;
		}
		
		ItemStack result = event.getResult();
		
		//get the enchantments on i1
		HashMap<CustomEnchantment,Integer> i1Enchants;
		if(i1.getType() == Material.ENCHANTED_BOOK) {
			i1Enchants = getEnchantmentsOnBook(i1);
		}else{
			i1Enchants = getEnchantmentsOnItem(i1);
		}
		
		//if the result variable is empty, fill it with a clone of i0
		if(result == null || result.getType() == Material.AIR){
			result = i0.clone();
		}
		
		//ensure the custom enchantments on i0 are on the result
		{
			
			//get the enchantments on i0
			HashMap<CustomEnchantment,Integer> i0Enchants;
			if(i0.getType() == Material.ENCHANTED_BOOK) {
				i0Enchants = getEnchantmentsOnBook(i0);
			}else{
				i0Enchants = getEnchantmentsOnItem(i0);
			}
			
			//get the enchantments on result
			HashMap<CustomEnchantment,Integer> resultEnchants;
			if(result.getType() == Material.ENCHANTED_BOOK) {
				resultEnchants = getEnchantmentsOnBook(result);
			}else{
				resultEnchants = getEnchantmentsOnItem(result);
			}
			
			boolean resultIsBook = isValidEnchantedBook(result);
			
			//ensure the custom enchantments on i0 are on the result
			boolean changed = false;
			for(CustomEnchantment ench : manager.getRegisteredEnchantments()){
				
				//if the input does not have this enchantment, skip to the next enchantment
				if(!i0Enchants.containsKey(ench)){
					continue;
				}
				
				int i0Level = i0Enchants.get(ench);
				Integer resultLevel = resultEnchants.get(ench);
				if(resultLevel == null){
					resultLevel = 0;
				}
				
				//if the result already has at least the level of the input, skip to the next enchantment
				if(resultLevel >= i0Level){
					continue;
				}
				
				//add the enchantment to the result
				if(resultIsBook){
					addEnchantmentToBook(result, ench, i0Level, true);
				}else{
					addEnchantmentToItem(result, ench, i0Level, true);
				}
				changed = true;
			}
			
			//if the result was changed, set the result in the anvil
			if(changed){
				event.setResult(result);
			}
		}
		
		//loop through all custom enchantments
		boolean changed = false;
		boolean resultIsBook = isValidEnchantedBook(result);
		for(CustomEnchantment ench : manager.getRegisteredEnchantments()){
			
			//if i1 does not have this enchantment, skip to the next enchantment
			if(!i1Enchants.containsKey(ench)){
				continue;
			}
			int level = i1Enchants.get(ench);
			if(level < 1){
				continue;
			}
			
			//determine if the result is an enchanted book
			if(resultIsBook){
				//validate item meta
				ItemMeta itemMeta = result.getItemMeta();
				if(itemMeta == null){
					continue;
				}
				
				//if the result (i.e. i0)'s level is greater than the sacrifice (i1), skip to the next enchantment
				if(((EnchantmentStorageMeta)itemMeta).getStoredEnchantLevel(ench) > level){
					continue;
				}
				
				//if the levels match, increase the level of the enchantment by 1
				if(((EnchantmentStorageMeta)itemMeta).getStoredEnchantLevel(ench) == level){
					level = level + 1;
				}
				
				//if the proposed enchantment is valid, apply the enchantment
				if(level >= ench.getStartLevel() && level <= ench.getMaxLevel()){
					addEnchantmentToBook(result, ench, level, true);
					changed = true;
				}
			}else{
				//if the result (i.e. i0)'s level is greater than the sacrifice (i1), skip to the next enchantment
				if(result.getEnchantmentLevel(ench) > level){
					continue;
				}
				
				//if the levels match, increase the level of the enchant by 1
				if(result.getEnchantmentLevel(ench) == level){
					level = level + 1;
				}
				
				//if the proposed enchantment is valid, apply the enchantment
				if(ench.canEnchantItem(result) && level >= ench.getStartLevel() && level <= ench.getMaxLevel()){
					addEnchantmentToItem(result, ench, level, true);
					changed = true;
				}
			}
			
		}
		
		//if nothing was changed, return
		if(!changed){
			return;
		}
		
		//set the anvil result to the result variable and add a cost if necessary
		event.setResult(result);
		if(event.getInventory().getRepairCost() < 1){
			event.getInventory().setRepairCost(1); //todo determine correct repair cost
		}
	}
	
	private boolean isValidEnchantedBook(ItemStack itemStack){
		if(itemStack == null || itemStack.getType() == Material.AIR || itemStack.getItemMeta() == null){
			return false;
		}
		return itemStack.getType() == Material.ENCHANTED_BOOK && itemStack.getItemMeta() instanceof EnchantmentStorageMeta;
	}
	
	public HashMap<CustomEnchantment,Integer> getEnchantmentsOnItem(ItemStack item){
		HashMap<CustomEnchantment,Integer> enchantments = new HashMap<>();
		for(CustomEnchantment ench : manager.getRegisteredEnchantments()){
			int level = item.getEnchantmentLevel(ench);
			if(level > 0){
				enchantments.put(ench, level);
			}
		}
		return enchantments;
	}
	
	public HashMap<CustomEnchantment,Integer> getEnchantmentsOnBook(ItemStack item){
		if(item.getType() != Material.ENCHANTED_BOOK){
			throw new IllegalArgumentException("Material must be ENCHANTED_BOOK");
		}
		ItemMeta itemMeta = item.getItemMeta();
		if(!(itemMeta instanceof EnchantmentStorageMeta)){
			throw new IllegalArgumentException("ItemMeta does not store enchantments");
		}
		EnchantmentStorageMeta esMeta = (EnchantmentStorageMeta)itemMeta;
		HashMap<CustomEnchantment,Integer> enchantments = new HashMap<>();
		for(CustomEnchantment ench : manager.getRegisteredEnchantments()){
			int level = esMeta.getStoredEnchantLevel(ench);
			if(level > 0){
				enchantments.put(ench, level);
			}
		}
		return enchantments;
	}
	
	public ItemStack addEnchantmentToItem(ItemStack item, CustomEnchantment enchantment, int level, boolean unsafe){
		if(item.getType() == Material.AIR){
			return item;
		}
		if(item.getType() == Material.ENCHANTED_BOOK){
			return addEnchantmentToBook(item, enchantment, level, unsafe);
		}
		if(!unsafe && !enchantment.canEnchantItem(item)){
			return item;
		}
		if(!unsafe && enchantment.getStartLevel() > level){
			level = enchantment.getStartLevel();
		}
		if(!unsafe && enchantment.getMaxLevel() < level){
			level = enchantment.getMaxLevel();
		}
		String loreLine = enchantment.getLorePrefix();
		if(enchantment.getMaxLevel() == 1 && enchantment.getStartLevel() == 1){
			level = 1;
		}else{
			loreLine += " " + CustomEnchantment.levelToRoman(level);
		}
		item.addUnsafeEnchantment(enchantment, level);
		ItemMeta meta = item.getItemMeta();
		if(meta == null){
			throw new IllegalArgumentException("ItemMeta is null???");
		}
		List<String> lore = meta.getLore();
		if(lore == null){
			lore = new ArrayList<>();
		}
		lore.removeIf(line -> line.startsWith(enchantment.getLorePrefix()));
		if(!meta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)){
			lore.add(loreLine);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack addEnchantmentToBook(ItemStack item, CustomEnchantment enchantment, int level, boolean unsafe){
		if(item.getType() != Material.ENCHANTED_BOOK){
			throw new IllegalArgumentException("ItemStack must be an Enchanted Book");
		}
		if(!unsafe && !enchantment.canEnchantItem(item)){
			return item;
		}
		if(!unsafe && enchantment.getStartLevel() > level){
			level = enchantment.getStartLevel();
		}
		if(!unsafe && enchantment.getMaxLevel() < level){
			level = enchantment.getMaxLevel();
		}
		String loreLine = enchantment.getLorePrefix();
		if(enchantment.getMaxLevel() == 1 && enchantment.getStartLevel() == 1){
			level = 1;
		}else{
			loreLine += " " + CustomEnchantment.levelToRoman(level);
		}
		ItemMeta itemMeta = item.getItemMeta();
		if(!(itemMeta instanceof EnchantmentStorageMeta)){
			throw new IllegalArgumentException("ItemMeta does not store enchantments");
		}
		EnchantmentStorageMeta esMeta = (EnchantmentStorageMeta)itemMeta;
		esMeta.addStoredEnchant(enchantment, level, true);
		List<String> lore = esMeta.getLore();
		if(lore == null){
			lore = new ArrayList<>();
		}
		lore.removeIf(line -> line.startsWith(enchantment.getLorePrefix()));
		if(!esMeta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)){
			lore.add(loreLine);
		}
		esMeta.setLore(lore);
		item.setItemMeta(esMeta);
		return item;
	}
	
	@EventHandler
	public void onDelicateWalker(PlayerInteractEvent event){
		if(event.getAction() != Action.PHYSICAL){
			return;
		}
		Block block = event.getClickedBlock();
		if(block == null || (block.getType() != Material.FARMLAND && block.getType() != Material.TURTLE_EGG)){
			return;
		}
		ItemStack item = event.getPlayer().getInventory().getBoots();
		if(item == null){
			return;
		}
		ItemMeta meta = item.getItemMeta();
		if(meta == null){
			return;
		}
		if(meta.getEnchantLevel(CustomEnchantment.DELICATE_WALKER) < 1){
			return;
		}
		event.setCancelled(true);
		event.setUseInteractedBlock(Event.Result.DENY);
	}
	
}
