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
		super(manager, Rank.HEAD_MOD, "fm", "lfm");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		doFakeMessage(profile.getPlayer(), args, label, Emote.EMOTE_ACCESS.has(profile), ChatManager.COLOR_ACCESS.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		doFakeMessage(sender, args, label, true, true);
	}
	
	private void doFakeMessage(CommandSender sender, String[] args, String label, boolean emotes, boolean colors){
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		
		String message = Joiner.on(' ').join(args).replaceAll("\\\\n", "\n");
		if(colors){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(emotes){
			message = Emote.replaceEmotes(message, ChatColor.WHITE);
		}
		if(label.startsWith("l")){
			Bukkit.broadcastMessage(message);
		}else{
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
