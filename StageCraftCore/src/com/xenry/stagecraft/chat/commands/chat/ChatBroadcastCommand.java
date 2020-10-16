package com.xenry.stagecraft.chat.commands.chat;

import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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
		String senderName = (sender instanceof Player ? sender.getName() : M.CONSOLE_NAME);
		String message = Joiner.on(' ').join(args);
		if(ChatManager.COLOR_ACCESS.has(sender)){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(Emote.EMOTE_ACCESS.has(sender)){
			message = Emote.replaceEmotes(message, ChatColor.LIGHT_PURPLE);
		}
		BaseComponent[] components = new ComponentBuilder(senderName).color(ChatColor.AQUA).bold(true)
				.append(": ").color(ChatColor.DARK_GRAY).bold(false)
				.append(TextComponent.fromLegacyText(message, ChatColor.LIGHT_PURPLE)).color(ChatColor.LIGHT_PURPLE).bold(false).create();
		//todo BungeeUtil.messageRawAll(sender, components);
		
		Bukkit.broadcastMessage("§b§l" + senderName + "§8: §d" + message);
	}
	
}
