package com.xenry.stagecraft.survival.gameplay.enchantment.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.survival.gameplay.grapplinghook.GrapplingHookHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/8/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class GiveEnchantedItemCommand extends PlayerCommand<Survival,GameplayManager> {
	
	public GiveEnchantedItemCommand(GameplayManager manager){
		super(manager, Rank.ADMIN,"giveenchanteditem", "gei");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <enchantment>"));
			return;
		}
		if(profile.getPlayer().getInventory().firstEmpty() == -1){
			profile.sendMessage(M.error("Not enough space in your inventory."));
			return;
		}
		ItemStack item;
		switch(args[0].toLowerCase()){
			case "oreminer":
			case "ore_miner":{
				item = manager.getBasicEnchantmentHandler().addEnchantmentToItem(new ItemStack(Material.GOLDEN_PICKAXE), CustomEnchantment.ORE_MINER, 1, false);
				break;
			}
			case "lumberjack":{
				item = manager.getBasicEnchantmentHandler().addEnchantmentToItem(new ItemStack(Material.DIAMOND_AXE), CustomEnchantment.LUMBERJACK, 1, false);
				break;
			}
			case "telekinesis":{
				item = manager.getBasicEnchantmentHandler().addEnchantmentToItem(new ItemStack(Material.DIAMOND_PICKAXE), CustomEnchantment.TELEKINESIS, 1, false);
				break;
			}
			case "delicatewalker":
			case "delicate_walker":{
				item = manager.getBasicEnchantmentHandler().addEnchantmentToItem(new ItemStack(Material.DIAMOND_BOOTS), CustomEnchantment.DELICATE_WALKER, 1, false);
				break;
			}
			case "growth":{
				item = manager.getBasicEnchantmentHandler().addEnchantmentToItem(new ItemStack(Material.DIAMOND_CHESTPLATE), CustomEnchantment.GROWTH, 1, false);
				break;
			}
			case "oresmelting":
			case "ore_smelting":{
				item = manager.getBasicEnchantmentHandler().addEnchantmentToItem(new ItemStack(Material.IRON_PICKAXE), CustomEnchantment.ORE_SMELTING, 1, false);
				break;
			}
			case "grappling_hook":
			case "grap_hook":
			case "grapplinghook":
			case "graphook":{
				item = GrapplingHookHandler.getNewGrapplingHookItem();
				break;
			}
			case "grappling_hook_fuel":
			case "grap_hook_fuel":
			case "grapplinghookfuel":
			case "graphookfuel":{
				item = GrapplingHookHandler.getNewFuelItem();
				break;
			}
			default:{
				profile.sendMessage(M.error("Invalid enchantment."));
				return;
			}
		}
		profile.getPlayer().getInventory().addItem(item);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("ore_miner", "lumberjack", "telekinesis", "delicate_walker", "growth", "ore_smelting", "grappling_hook", "grappling_hook_fuel"), args[0]) : Collections.emptyList();
	}
	
}
