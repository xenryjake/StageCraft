package com.xenry.stagecraft.survival.teleportation.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPACommand extends Command<Survival,TeleportationManager> {
	
	public TPACommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "tpa", "tpahere");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		boolean reverse = label.equalsIgnoreCase("tpahere");
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Player to = Bukkit.getPlayer(args[0]);
		if(to == null){
			profile.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		manager.createAndSendRequest(profile.getPlayer(), to, reverse);
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
}