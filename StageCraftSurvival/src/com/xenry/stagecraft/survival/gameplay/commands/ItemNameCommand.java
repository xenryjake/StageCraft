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

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ItemNameCommand extends Command<Survival,GameplayManager> {
	
	public ItemNameCommand(GameplayManager manager) {
		super(manager, Rank.ADMIN, "itemname", "iname");
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
		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta meta;
		if(item == null || item.getType() == Material.AIR || (meta = item.getItemMeta()) == null){
			player.sendMessage(M.error("You can't rename that item."));
			return;
		}
		
		String name = null;
		if(args.length > 0){
			StringBuilder sb = new StringBuilder();
			for(String string : args){
				sb.append(string).append(" ");
			}
			name = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
		}
		
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		if(name == null){
			player.sendMessage(M.msg + "You reset this item's name.");
		}else{
			player.sendMessage(M.msg + "You set this item's name to " + M.WHITE + M.ITALIC + name + M.msg + ".");
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
