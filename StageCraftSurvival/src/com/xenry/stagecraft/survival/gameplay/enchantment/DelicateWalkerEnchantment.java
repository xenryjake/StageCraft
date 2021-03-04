package com.xenry.stagecraft.survival.gameplay.enchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/7/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class DelicateWalkerEnchantment extends CustomEnchantment {
	
	private static final List<Material> types = Arrays.asList(LEATHER_BOOTS, CHAINMAIL_BOOTS, IRON_BOOTS, GOLDEN_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS);
	
	public DelicateWalkerEnchantment(){
		super("delicate_walker");
	}
	
	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		return types.contains(item.getType());
	}
	
	@Override
	public @NotNull Component displayName(int i) {
		return Component.text("Delicate Walker");
	}
	
	@Override
	public @NotNull EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ARMOR_FEET;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public @NotNull String getName() {
		return "Delicate Walker";
	}
	
}
