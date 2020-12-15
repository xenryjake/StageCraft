package com.xenry.stagecraft.command.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.command.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/30/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class CommandInfoCommand extends Command<Core,CommandManager> {
	
	public CommandInfoCommand(CommandManager manager){
		super(manager, Rank.HEAD_MOD, false, "info");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/command " + label + " <command-label>"));
			return;
		}
		Command<?,?> command = manager.getCommand(args[0]);
		if(command == null){
			sender.sendMessage(M.error("No command found with label: " + args[0]));
			return;
		}
		String accessString = command.getAccess() instanceof Rank ? ((Rank)command.getAccess()).getColoredName()
				: M.WHITE + command.getAccess().toString();
		StringBuilder sb = new StringBuilder();
		for(String l : command.getLabels()){
			sb.append(M.WHITE).append(l).append(M.DGRAY).append(", ");
		}
		String labelsString = sb.toString().trim();
		
		sender.sendMessage(M.msg + "Command info: " + M.elm + args[0]);
		sender.sendMessage(M.arrow("Plugin: " + M.elm + command.getPlugin().name));
		sender.sendMessage(M.arrow("Manager: " + M.elm + command.getManager().name));
		sender.sendMessage(M.arrow("Access: " + accessString));
		sender.sendMessage(M.arrow("Labels: " + labelsString));
		sender.sendMessage(M.arrow("Can be disabled: " + (command.canBeDisabled ? "§atrue" : "§cfalse")));
		if(command.canBeDisabled){
			sender.sendMessage(M.arrow("Is disabled: " + (command.isDisabled() ? "§atrue" : "§cfalse")));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getCommandLabels(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getCommandLabels(args[0]) : Collections.emptyList();
	}
	
}
