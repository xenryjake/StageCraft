package com.xenry.stagecraft.survival.gameplay.enchantment.autosmelt;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/4/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class OreSmeltingEnchantment extends CustomEnchantment {
	
	private static final List<Material> types = Arrays.asList(STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE);
	
	public OreSmeltingEnchantment(){
		super("ore_smelting");
	}
	
	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		return item.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0 && types.contains(item.getType());
	}
	
	@Override
	public @NotNull EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public @NotNull String getName() {
		return "Ore Smelting";
	}
	
	@Override
	public boolean conflictsWith(@NotNull Enchantment other) {
		return other == Enchantment.SILK_TOUCH;
	}
	
}
