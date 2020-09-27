package com.xenry.stagecraft.survival.gameplay.enchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.automine.LumberjackEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.automine.OreMinerEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.growth.GrowthEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.autosmelt.OreSmeltingEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.flintytouch.FlintyTouchEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.specialitem.SpecialItemEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.telekinesis.TelekinesisEnchantment;
import com.xenry.stagecraft.survival.gameplay.grapplinghook.GrapplingHookEnchantment;
import com.xenry.stagecraft.util.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class CustomEnchantment extends Enchantment {
	
	public static final CustomEnchantment SPECIAL_ITEM = new SpecialItemEnchantment();
	public static final CustomEnchantment ORE_MINER = new OreMinerEnchantment();
	public static final CustomEnchantment LUMBERJACK = new LumberjackEnchantment();
	public static final CustomEnchantment TELEKINESIS = new TelekinesisEnchantment();
	public static final CustomEnchantment DELICATE_WALKER = new DelicateWalkerEnchantment();
	public static final CustomEnchantment GROWTH = new GrowthEnchantment();
	public static final CustomEnchantment ORE_SMELTING = new OreSmeltingEnchantment();
	public static final CustomEnchantment FLINTY_TOUCH = new FlintyTouchEnchantment();
	public static final CustomEnchantment GRAPPLING_HOOK = new GrapplingHookEnchantment();
	
	private boolean registered = false;
	
	@SuppressWarnings("deprecation")
	protected CustomEnchantment(String id){
		super(ItemUtil.key(id));
	}
	
	public abstract boolean canEnchantItem(@NotNull ItemStack item);
	
	public abstract @NotNull EnchantmentTarget getItemTarget();
	
	public abstract int getMaxLevel();
	
	public abstract @NotNull String getName();
	
	@Override
	public boolean conflictsWith(@NotNull Enchantment other){
		return false;
	}
	
	@Override
	public int getStartLevel(){
		return 1;
	}
	
	@Override
	public boolean isCursed(){
		return false;
	}
	
	@Override
	public boolean isTreasure(){
		return false;
	}
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void setRegistered(){
		registered = true;
	}
	
	public static String levelToRoman(int level){
		switch(level){
			case 1:
				return "I";
			case 2:
				return "II";
			case 3:
				return "III";
			case 4:
				return "IV";
			case 5:
				return "V";
			case 6:
				return "VI";
			case 7:
				return "VII";
			case 8:
				return "VIII";
			case 9:
				return "IX";
			case 10:
				return "X";
			default:
				return String.valueOf(level);
		}
	}
	
	public String getLorePrefix(){
		return "§7" + getName();
	}
	
}
