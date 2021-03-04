package com.xenry.stagecraft.survival.gameplay.enchantment.automine;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import net.kyori.adventure.text.Component;
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
public final class OreMinerEnchantment extends CustomEnchantment {
	
	private static final List<Material> types = Arrays.asList(WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE);
	
	public OreMinerEnchantment(){
		super("ore_miner");
	}
	
	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		return types.contains(item.getType());
	}
	
	@Override
	public @NotNull Component displayName(int i) {
		return Component.text("Ore Miner " + levelToRoman(i));
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
		return "Ore Miner";
	}
	
}
