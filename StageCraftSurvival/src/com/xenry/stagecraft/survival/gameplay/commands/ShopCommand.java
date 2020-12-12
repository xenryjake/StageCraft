package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.growth.GrowthIngredient;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ShopCommand extends PlayerCommand<Survival,GameplayManager> {
	
	public static final String shopName = "§1§lStage§2§lCraft §6§lShop";
	
	public ShopCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "shop");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.getPlayer().openMerchant(getMerchant(), true);
	}
	
	private Merchant getMerchant(){
		Merchant merchant = Bukkit.createMerchant(shopName);
		List<MerchantRecipe> recipes = new ArrayList<>();
		{
			ItemStack result = manager.getBasicEnchantmentHandler().addEnchantmentToItem(
					new ItemStack(Material.ENCHANTED_BOOK), CustomEnchantment.DELICATE_WALKER, 1, true);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(new ItemStack(Material.EMERALD, 24));
			recipe.addIngredient(new ItemStack(Material.BOOK));
			recipes.add(recipe);
		}{
			ItemStack result = manager.getBasicEnchantmentHandler().addEnchantmentToItem(
					new ItemStack(Material.ENCHANTED_BOOK), CustomEnchantment.TELEKINESIS, 1, true);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(new ItemStack(Material.EMERALD, 12));
			recipe.addIngredient(new ItemStack(Material.BOOK));
			recipes.add(recipe);
		}{
			ItemStack result = manager.getBasicEnchantmentHandler().addEnchantmentToItem(
					new ItemStack(Material.ENCHANTED_BOOK), CustomEnchantment.LUMBERJACK, 1, true);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(new ItemStack(Material.EMERALD, 20));
			recipe.addIngredient(new ItemStack(Material.BOOK));
			recipes.add(recipe);
		}{
			ItemStack result = manager.getBasicEnchantmentHandler().addEnchantmentToItem(
					new ItemStack(Material.ENCHANTED_BOOK), CustomEnchantment.ORE_MINER, 1, true);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(new ItemStack(Material.EMERALD, 14));
			recipe.addIngredient(new ItemStack(Material.BOOK));
			recipes.add(recipe);
		}{
			ItemStack result = manager.getBasicEnchantmentHandler().addEnchantmentToItem(
					new ItemStack(Material.ENCHANTED_BOOK), CustomEnchantment.ORE_SMELTING, 1, true);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(new ItemStack(Material.EMERALD, 24));
			recipe.addIngredient(new ItemStack(Material.BOOK));
			recipes.add(recipe);
		}{
			ItemStack result = manager.getBasicEnchantmentHandler().addEnchantmentToItem(
					new ItemStack(Material.ENCHANTED_BOOK), CustomEnchantment.GROWTH, 1, true);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(new ItemStack(Material.EMERALD, 16));
			recipe.addIngredient(GrowthIngredient.FARMING_BOOK.getItemStack(1));
			recipes.add(recipe);
		}{
			ItemStack result = GrowthIngredient.FARMING_BOOK.getItemStack(1);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(GrowthIngredient.ESSENCE_OF_FARMING.getItemStack(1));
			recipe.addIngredient(new ItemStack(Material.BOOK));
			recipes.add(recipe);
		}{
			ItemStack result = GrowthIngredient.ESSENCE_OF_FARMING.getItemStack(1);
			MerchantRecipe recipe = new MerchantRecipe(result, 64);
			recipe.addIngredient(GrowthIngredient.ENCHANTED_POTATO.getItemStack(64));
			recipe.addIngredient(GrowthIngredient.ENCHANTED_CARROT.getItemStack(64));
			recipes.add(recipe);
		}
		merchant.setRecipes(recipes);
		return merchant;
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
