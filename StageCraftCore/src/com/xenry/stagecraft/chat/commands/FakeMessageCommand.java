package com.xenry.stagecraft.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class FakeMessageCommand extends Command<Core,ChatManager> {
	
	public FakeMessageCommand(ChatManager manager){
		super(manager, Rank.ADMIN, "fm", "lfm");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length < 1){
			player.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		if(label.startsWith("l")){
			doLocal(player, profile, args);
		}else{
			doGlobal(player, profile, args);
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		if(label.startsWith("l")){
			doLocal(sender, null, args);
		}else{
			doGlobal(sender, null, args);
		}
	}
	
	private void doGlobal(CommandSender sender, Profile profile, String[] args){
		if(args.length < 1){
			sender.sendMessage(M.usage("/fm <message>"));
			return;
		}
		
		String message = Joiner.on(' ').join(args).replaceAll("\\\\n", "\n");
		if(profile == null){
			message = ChatColor.translateAlternateColorCodes('&', message);
			message = Emote.replaceAllEmotes(message, ChatColor.WHITE);
		}else{
			if(ChatManager.COLOR_ACCESS.has(profile)){
				message = ChatColor.translateAlternateColorCodes('&', message);
			}
			message = Emote.replaceEmotes(message, ChatColor.WHITE, profile);
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
		Log.toCS(message);
		manager.getBroadcastPMSC().send(pluginMessageSender, TextComponent.fromLegacyText(message, ChatColor.WHITE));
	}
	
	private void doLocal(CommandSender sender, Profile profile, String[] args){
		if(args.length < 1){
			sender.sendMessage(M.usage("/lfm <message>"));
			return;
		}
		
		String message = Joiner.on(' ').join(args).replaceAll("\\\\n", "\n");
		if(profile == null){
			message = ChatColor.translateAlternateColorCodes('&', message);
			message = Emote.replaceAllEmotes(message, ChatColor.WHITE);
		}else{
			if(ChatManager.COLOR_ACCESS.has(profile)){
				message = ChatColor.translateAlternateColorCodes('&', message);
			}
			message = Emote.replaceEmotes(message, ChatColor.WHITE, profile);
		}
		Bukkit.broadcastMessage(message);
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
