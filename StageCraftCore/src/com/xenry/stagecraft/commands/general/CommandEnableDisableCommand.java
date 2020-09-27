package com.xenry.stagecraft.commands.general;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.commands.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

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
	
	public CommandEnableDisableCommand(CommandManager manager){
		super(manager, Rank.ADMIN, "enable", "disable", "en", "dis");
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
		sender.sendMessage(M.msg + "Command " + M.elm + args[0] + M.msg + " has been " + (disable ? "§cdisabled"
				: "§aenabled") + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? manager.getAllCommandLabels() : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? manager.getAllCommandLabels() : Collections.emptyList();
	}
	
}
