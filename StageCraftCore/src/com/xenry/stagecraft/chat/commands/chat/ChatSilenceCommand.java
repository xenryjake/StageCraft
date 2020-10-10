package com.xenry.stagecraft.chat.commands.chat;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on January 21, 2016
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class ChatSilenceCommand extends Command<Core,ChatManager> {

	public ChatSilenceCommand(ChatManager manager){
		super(manager, Rank.MOD, "silence", "mute", "lock", "s", "m", "l");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			Rank rank = manager.getChatRank();
			sender.sendMessage(M.msg + "The chat is currently silenced to ranks below "
					+ rank.getColoredName() + M.msg + ".");
			return;
		}
		args[0] = args[0].toUpperCase();
		Rank rank;
		try{
			rank = Rank.valueOf(args[0]);
		}catch(Exception ex){
			if(args[0].startsWith("OFF") || args[0].startsWith("DISABLE")){
				rank = Rank.MEMBER;
			}else{
				sender.sendMessage(M.error(args[0] + " is not a valid rank."));
				return;
			}
		}
		manager.setChatRank(rank);
		Bukkit.broadcastMessage(M.msg + "The chat is now " + (rank == Rank.MEMBER ? "unsilenced" :
				"silenced to ranks below " + rank.getColoredName()) + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Rank.getRankNames() : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Rank.getRankNames() : Collections.emptyList();
	}
}
