package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ClearInventoryCommand extends Command<Survival,GameplayManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	private final Cooldown confirms;

	public ClearInventoryCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "clearinventory", "clear", "ci");
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
			player.sendMessage(M.msg + "Are you sure you want to clear your inventory? Type " + M.elm + "/" + label
					+ M.msg + " again to confirm.");
			return;
		}
		confirms.removeRecharge(player);
		player.getInventory().clear();
		player.sendMessage(M.msg + "Your inventory has been cleared.");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length != 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Player target = Bukkit.getPlayerExact(args[0]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[0]) + " (please specify an exact player name)");
			return;
		}
		target.getInventory().clear();
		sender.sendMessage(M.msg + "You cleared " + M.elm + target.getName() + M.msg + "'s inventory.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 && OTHERS.has(profile) ? localPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
