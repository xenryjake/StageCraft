package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ClearInventoryCommand extends Command<Creative,GameplayManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	private final Cooldown confirms;

	public ClearInventoryCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "clearinventory", "clear", "ci");
		confirms = new Cooldown(15000, null);
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length > 0 && OTHERS.has(profile)){
			serverPerform(profile.getPlayer(), args, label);
			return;
		}
		Player player = profile.getPlayer();
		if(confirms.canUse(player)){
			confirms.use(player);
			player.sendMessage(M.msg + "Are you sure you want to clear your inventory? Type " + M.elm + label
					+ M.msg + " again to confirm.");
			return;
		}
		confirms.removeRecharge(player);
		player.getInventory().clear();
		player.sendMessage(M.msg + "Your inventory has been cleared.");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		target.getInventory().clear();
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 && OTHERS.has(profile) ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
}
