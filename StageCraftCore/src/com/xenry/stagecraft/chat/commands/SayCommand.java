package com.xenry.stagecraft.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
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
 * StageCraft created by Henry Blasingame (Xenry) on 7/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SayCommand extends Command<Core,ChatManager> {
	
	public SayCommand(ChatManager manager){
		super(manager, Rank.MEMBER, "say");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/say <message>"));
			return;
		}
		profile.getPlayer().chat(Joiner.on(' ').join(args));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/say <message>"));
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
		
		String message = ChatColor.translateAlternateColorCodes('&', Joiner.on(' ').join(args));
		message = Emote.replaceEmotes(message, ChatColor.WHITE);
		BaseComponent[] components = new ComponentBuilder(M.CONSOLE_NAME).color(ChatColor.YELLOW).bold(true)
				.append(": ").color(ChatColor.DARK_GRAY).bold(false)
				.append(TextComponent.fromLegacyText(message)).create();
		//Bukkit.broadcastMessage("§e§lServer§8:§r " + message);
		manager.getBroadcastPMSC().send(pluginMessageSender, components);
		Log.toCS(components);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return allNetworkPlayers();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return allNetworkPlayers();
	}
	
}
