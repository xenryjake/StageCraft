package com.xenry.stagecraft.commands.general;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.commands.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/30/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class CommandCommand extends Command<Core,CommandManager> {
	
	public CommandCommand(CommandManager manager){
		super(manager, Rank.ADMIN, "commands", "command", "cmds", "cmd");
		addSubCommand(new CommandEnableDisableCommand(manager));
		addSubCommand(new CommandInfoCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "§lCommand Management Commands:");
		sender.sendMessage(M.help(label + " enable <command-label>", "Enable a command."));
		sender.sendMessage(M.help(label + " disable <command-label>", "Disable a command."));
		sender.sendMessage(M.help(label + " info <command-label>", "View info about a command."));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("enable", "disable", "info") : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("enable", "disable", "info") : Collections.emptyList();
	}
}
