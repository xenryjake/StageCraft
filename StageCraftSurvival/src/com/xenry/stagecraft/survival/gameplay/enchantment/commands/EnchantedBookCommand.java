package com.xenry.stagecraft.survival.gameplay.enchantment.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.ItemUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

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
public final class EnchantedBookCommand extends Command<Survival,GameplayManager> {
	
	public EnchantedBookCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "enchantedbook", "eb");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <enchantment> [level]"));
			return;
		}
		int level = 1;
		if(args.length > 1){
			try{
				level = Integer.parseInt(args[1]);
			}catch(Exception ex){
				profile.sendMessage(M.error("Invalid level."));
				return;
			}
		}
		CustomEnchantment enchantment = (CustomEnchantment)Enchantment.getByKey(ItemUtil.key(args[0]));
		if(enchantment == null){
			profile.sendMessage(M.error("Invalid enchantment name."));
			return;
		}
		if(profile.getPlayer().getInventory().firstEmpty() == -1){
			profile.sendMessage(M.error("No space in your inventory."));
			return;
		}
		profile.getPlayer().getInventory().addItem(manager.getBasicEnchantmentHandler().addEnchantmentToItem(new ItemStack(Material.ENCHANTED_BOOK), enchantment, level, true));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("special_item", "ore_miner", "lumberjack", "telekinesis", "delicate_walker", "growth", "ore_smelting") : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("special_item", "ore_miner", "lumberjack", "telekinesis", "delicate_walker", "growth", "ore_smelting") : Collections.emptyList();
	}
	
}
