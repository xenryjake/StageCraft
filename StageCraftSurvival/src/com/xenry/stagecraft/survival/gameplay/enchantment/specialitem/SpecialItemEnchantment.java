package com.xenry.stagecraft.survival.gameplay.enchantment.specialitem;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class SpecialItemEnchantment extends CustomEnchantment {
	
	public SpecialItemEnchantment(){
		super("special_item");
	}
	
	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		return true;
	}
	
	@Override
	public @NotNull Component displayName(int i) {
		return Component.text("");
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public @NotNull EnchantmentTarget getItemTarget() {
		return null;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public @NotNull String getName() {
		return "Special Item";
	}
	
}
