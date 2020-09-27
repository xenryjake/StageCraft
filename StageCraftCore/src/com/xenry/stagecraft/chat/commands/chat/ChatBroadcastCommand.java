package com.xenry.stagecraft.chat.commands.chat;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Jake on January 20, 2016
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class ChatBroadcastCommand extends Command<Core,ChatManager> {

	public ChatBroadcastCommand(ChatManager manager){
		super(manager, Rank.MOD, "broadcast", "bc", "bcast");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/chat " + label + " <message>"));
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(String str : args) {
			sb.append(str).append(" ");
		}
		String message = Emote.replaceEmotes(sb.toString().trim(), ChatColor.LIGHT_PURPLE);
		Bukkit.broadcastMessage("§b§l" + (sender instanceof Player ? sender.getName() : M.CONSOLE_NAME)
				+ "§8: §d" + message);
	}
	
}
