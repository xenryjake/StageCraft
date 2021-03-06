package com.xenry.stagecraft.skyblock.gameplay.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ItemNameCommand extends PlayerCommand<SkyBlock,GameplayManager> {
	
	public ItemNameCommand(GameplayManager manager) {
		super(manager, Rank.HEAD_MOD, "itemname", "iname");
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
			name = ChatColor.translateAlternateColorCodes('&', Joiner.on(' ').join(args));
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
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
