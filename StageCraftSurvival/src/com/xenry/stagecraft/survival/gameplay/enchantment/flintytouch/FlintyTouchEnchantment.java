package com.xenry.stagecraft.survival.gameplay.enchantment.flintytouch;
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
 * StageCraft created by Henry Blasingame (Xenry) on 9/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class FlintyTouchEnchantment extends CustomEnchantment {
	
	private static final List<Material> types = Arrays.asList(WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL);
	
	public FlintyTouchEnchantment(){
		super("flinty_touch");
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
		return "Flinty Touch";
	}
	
	@Override
	public boolean conflictsWith(@NotNull Enchantment other) {
		return other == Enchantment.SILK_TOUCH;
	}
	
}
