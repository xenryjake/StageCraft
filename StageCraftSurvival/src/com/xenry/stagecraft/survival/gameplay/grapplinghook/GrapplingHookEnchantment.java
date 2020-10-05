package com.xenry.stagecraft.survival.gameplay.grapplinghook;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GrapplingHookEnchantment extends CustomEnchantment {
	
	public GrapplingHookEnchantment(){
		super("grappling_hook");
	}
	
	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		return true;
	}
	
	@Override
	public @NotNull EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.FISHING_ROD;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public @NotNull String getName() {
		return "Grappling Hook";
	}
	
}
