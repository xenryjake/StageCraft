package com.xenry.stagecraft.survival.gameplay.enchantment.growth;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum GrowthIngredient {
	
	ENCHANTED_POTATO("Enchanted Potato", Material.POTATO),
	ENCHANTED_CARROT("Enchanted Carrot", Material.CARROT),
	ESSENCE_OF_FARMING("Essence of Farming", Material.SCUTE),
	FARMING_BOOK("Farming Book", Material.BOOK);
	
	private final String name;
	private final Material material;
	
	GrowthIngredient(String name, Material material) {
		this.name = name;
		this.material = material;
	}
	
	public String getName() {
		return name;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public ItemStack getItemStack(){
		return getItemStack(1);
	}
	
	public ItemStack getItemStack(int amount){
		ItemStack item = new ItemStack(material, amount);
		item.addUnsafeEnchantment(CustomEnchantment.SPECIAL_ITEM, 1);
		ItemMeta meta = item.getItemMeta();
		if(meta == null){
			throw new IllegalArgumentException("ItemMeta is null???");
		}
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName("§a" + name);
		item.setItemMeta(meta);
		return item;
	}
	
}
