package com.xenry.stagecraft.command.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.command.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
public final class CommandEnableDisableCommand extends Command<Core,CommandManager> {
	
	public static final Access ACCESS = Rank.HEAD_MOD;
	
	public CommandEnableDisableCommand(CommandManager manager){
		super(manager, ACCESS, "enable", "disable", "en", "dis");
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
		if(!command.canBeDisabled()){
			sender.sendMessage(M.error("This command cannot be disabled."));
			return;
		}
		boolean disable = label.startsWith("dis");
		command.setDisabled(disable);
		sender.sendMessage(M.elm + args[0] + M.msg + " has been " + (disable ? "§cdisabled" : "§aenabled") + M.msg
				+ " until the next server restart.");
		
		String senderName = sender instanceof Player ? sender.getName() : M.CONSOLE_NAME;
		String notification = M.elm + senderName + M.msg + (disable ? " §cdisabled" : " §aenabled") + M.msg + " the command "
				+ M.elm + args[0] + M.msg + ".";
		Log.toCS(notification);
		for(Profile profile : manager.plugin.getProfileManager().getOnlineProfiles()){
			if(ACCESS.has(profile) && profile.getPlayer() != sender){
				profile.sendMessage(notification);
			}
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
