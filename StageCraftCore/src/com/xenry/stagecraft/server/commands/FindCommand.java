package com.xenry.stagecraft.server.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.CollectionUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class FindCommand extends Command<Core,ServerManager> {
	
	public FindCommand(ServerManager manager){
		super(manager, Rank.MOD, "find", "whereis");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		String name = CollectionUtil.findClosestMatchByStart(allNetworkPlayers(), args[0]);
		if(name == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		for(Map.Entry<String,List<String>> entry : manager.getNetworkPlayers().entrySet()){
			if(entry.getValue().contains(name)){
				sender.sendMessage(M.elm + name + M.msg + " is online at " + M.elm + entry.getKey() + M.msg + ".");
				return;
			}
		}
		sender.sendMessage(M.playerNotFound(args[0]));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? allNetworkPlayers() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? allNetworkPlayers() : Collections.emptyList();
	}
	
}
