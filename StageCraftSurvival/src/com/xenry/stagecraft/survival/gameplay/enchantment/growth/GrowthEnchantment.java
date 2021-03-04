package com.xenry.stagecraft.survival.gameplay.enchantment.growth;
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
 * StageCraft created by Henry Blasingame (Xenry) on 4/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GrowthEnchantment extends CustomEnchantment {
	
	/* 		question: allow only in multiples of 2 (so full heart)?
	 * GROWTH 1: +1 health per armor
	 * GROWTH 2: +2 health per armor
	 * GROWTH 3: +3 health per armor
	 * GROWTH 4: +4 health per armor
	 * GROWTH 5: +5 health per armor
	 */
	
	private static final List<Material> types = Arrays.asList(
			LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS,
			CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS,
			IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS,
			GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS,
			DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS,
			NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS,
			TURTLE_HELMET, ELYTRA
	);
	
	public GrowthEnchantment(){
		super("growth");
	}
	
	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		return types.contains(item.getType());
	}
	
	@Override
	public @NotNull Component displayName(int i) {
		return Component.text("Growth " + levelToRoman(i));
	}
	
	@Override
	public @NotNull EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ARMOR;
	}
	
	@Override
	public int getMaxLevel() {
		return 5;
	}
	
	@Override
	public @NotNull String getName() {
		return "Growth";
	}
	
}
