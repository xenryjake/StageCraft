package com.xenry.stagecraft.survival.gameplay.enchantment.telekinesis;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Arrays;

import static org.bukkit.Material.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class TelekinesisEnchantment extends CustomEnchantment {
	
	private static final List<Material> types = Arrays.asList(
			WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE,
			WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL,
			WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE,
			WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD,
			SHEARS, BOW, CROSSBOW);
	
	public TelekinesisEnchantment(){
		super("telekinesis");
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
		return 1;
	}
	
	@Override
	public @NotNull String getName() {
		return "Telekinesis";
	}
	
}
