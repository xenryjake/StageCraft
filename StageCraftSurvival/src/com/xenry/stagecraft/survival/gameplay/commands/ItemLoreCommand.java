package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ItemLoreCommand extends Command<Survival,GameplayManager> {
	
	public ItemLoreCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "itemlore", "ilore");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length < 1){
			player.sendMessage(M.usage("/" + label + " <set|add|clear> [text]"));
			return;
		}
		ItemStack is = player.getInventory().getItemInMainHand();
		ItemMeta im;
		if(is == null || is.getType() == Material.AIR || (im = is.getItemMeta()) == null){
			player.sendMessage(M.error("You can't change the lore on this item."));
			return;
		}
		if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("reset")){
			im.setLore(Collections.emptyList());
			player.sendMessage(M.msg + "Cleared the lore on " + M.elm + is.getType().name() + M.msg + ".");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++){
			sb.append(args[i]).append(" ");
		}
		List<String> newLore = Arrays.asList(ChatColor.translateAlternateColorCodes('&',
				sb.toString().trim()).split("\\\\n"));
		List<String> lore = im.getLore();
		if(args[0].equalsIgnoreCase("set")){
			lore = newLore;
		}else if(args[0].equalsIgnoreCase("add")){
			lore.addAll(newLore);
		}else{
			player.sendMessage(M.usage("/" + label + " <set|add|clear> [text]"));
			return;
		}
		im.setLore(lore);
		player.sendMessage(M.msg + "Set the lore on " + M.elm + is.getType().name() + M.msg + " to:");
		for(String s : newLore){
			player.sendMessage(M.arrow(s));
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("set","add","clear") : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
