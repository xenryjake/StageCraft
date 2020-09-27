package com.xenry.stagecraft.survival.gameplay.enchantment.automine;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/31/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class LumberjackEnchantment extends CustomEnchantment {
	
	private static final List<Material> types = Arrays.asList(WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE);
	
	public LumberjackEnchantment(){
		super("lumberjack");
	}
	
	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		return types.contains(item.getType());
	}
	
	@Override
	public @NotNull EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}
	
	@Override
	public int getMaxLevel() {
		return 5;
	}
	
	@Override
	public @NotNull String getName() {
		return "Lumberjack";
	}
	
}
