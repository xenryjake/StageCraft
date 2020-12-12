package com.xenry.stagecraft.chat.commands;

import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * StageCraft created by Henry Jake on January 20, 2016
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class BroadcastCommand extends Command<Core,ChatManager> {

	public BroadcastCommand(ChatManager manager){
		super(manager, Rank.MOD, "broadcast", "bc", "bcast");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		Player pluginMessageSender;
		if(sender instanceof Player){
			pluginMessageSender = (Player)sender;
		}else{
			pluginMessageSender = PlayerUtil.getAnyPlayer();
			if(pluginMessageSender == null){
				sender.sendMessage(M.error("There are no players on this server. Please try again on another server or the proxy."));
				return;
			}
		}
		
		String message = Joiner.on(' ').join(args);
		if(ChatManager.COLOR_ACCESS.has(sender)){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(Emote.EMOTE_ACCESS.has(sender)){
			message = Emote.replaceEmotes(message, ChatColor.RED);
		}
		BaseComponent[] components = new ComponentBuilder(sender instanceof Player ? sender.getName() : M.CONSOLE_NAME)
				.color(ChatColor.YELLOW).bold(true)
				.append(": ").color(ChatColor.DARK_GRAY).bold(false)
				.append(TextComponent.fromLegacyText(message, ChatColor.RED))
				.color(ChatColor.RED).bold(false).create();
		//BungeeUtil.messageRawAll(sender.getPlayer(), components);
		//Bukkit.broadcastMessage("§b§l" + player.getName() + "§8: §d" + message);
		
		manager.getBroadcastPMSC().send(pluginMessageSender, components);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return networkPlayers(args[args.length-1]);
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return networkPlayers(args[args.length-1]);
	}
	
}
