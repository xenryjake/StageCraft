package com.xenry.stagecraft.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.Channel;
import com.xenry.stagecraft.chat.ChatEvent;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class AdminChatCommand extends Command<Core,ChatManager> {
	
	public AdminChatCommand(ChatManager manager){
		super(manager, Channel.ADMIN.access, "adminchat", "admc");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		handle(profile, profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		handle(null, sender, args, label);
	}
	
	private void handle(Profile profile, CommandSender sender, String[] args, String label){
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		String message = Joiner.on(' ').join(args);
		Player pluginMessageSender;
		if(sender instanceof Player){
			pluginMessageSender = (Player)sender;
			ChatEvent chatEvent = new ChatEvent(false, Channel.ADMIN, profile, message);
			manager.plugin.getServer().getPluginManager().callEvent(chatEvent);
			if(chatEvent.isCancelled()){
				return;
			}
			message = chatEvent.getMessage();
		}else{
			pluginMessageSender = PlayerUtil.getAnyPlayer();
			if(pluginMessageSender == null){
				sender.sendMessage(M.error("There are no players on this server. Please try again on another server or the proxy."));
				return;
			}
		}
		
		manager.sendChat(pluginMessageSender, Channel.ADMIN, Channel.ADMIN.format(manager, profile, message));
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
