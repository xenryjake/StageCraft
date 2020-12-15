package com.xenry.stagecraft.server.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class StopCommand extends Command<Core,ServerManager> {
	
	public StopCommand(ServerManager manager){
		super(manager, Rank.ADMIN, "stop", "shutdown");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <seconds> [reason]"));
			return;
		}
		if(args[0].equalsIgnoreCase("cancel")){
			if(manager.getShutdownTask() == null){
				sender.sendMessage(M.error("A shutdown has not been initiated."));
				return;
			}
			sender.sendMessage(M.msg + "Cancelling server shutdown...");
			manager.cancelShutdown();
			return;
		}
		if(args[0].equalsIgnoreCase("now") || args[0].equals("0")){
			manager.doShutdown(0, args.length <= 1 ? null : Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length)));
			return;
		}
		if(manager.getShutdownTask() != null){
			sender.sendMessage(M.error("A shutdown is already in progress."));
			return;
		}
		int seconds;
		try{
			seconds = Integer.parseInt(args[0]);
		}catch(Exception ex){
			sender.sendMessage(M.error("Invalid integer: " + args[0]));
			return;
		}
		if(seconds < 0){
			sender.sendMessage(M.error("Time cannot be negative."));
			return;
		}
		String reason = args.length <= 1 ? null : Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		manager.doShutdown(seconds, reason);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
