package com.xenry.stagecraft.chat.commands.chat;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on January 20, 2016
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class ChatCommand extends Command<Core,ChatManager> {

	public ChatCommand(ChatManager manager){
		super(manager, Rank.MOD, "chat", "ch");
		setCanBeDisabled(true);
		addSubCommand(new ChatSilenceCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length > 0 && (args[0].equalsIgnoreCase("broadcast")
				|| args[0].equalsIgnoreCase("bcast")
				|| args[0].equalsIgnoreCase("bc"))){
			sender.sendMessage(M.elm + "Notice: " + M.msg + " This feature has been moved to /broadcast");
			return;
		}
		sender.sendMessage(M.msg + "Available Commands:");
		sender.sendMessage(M.help("broadcast", "Broadcast a message in chat"));
		sender.sendMessage(M.help(label + " silence", "Silence the chat to a rank"));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("bc", "silence") : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("bc", "silence") : Collections.emptyList();
	}
	
}
