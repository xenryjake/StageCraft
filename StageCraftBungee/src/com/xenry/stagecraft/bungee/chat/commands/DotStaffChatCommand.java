package com.xenry.stagecraft.bungee.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.bungee.chat.ChatManager;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotStaffChatCommand extends ProxyAdminCommand<ChatManager> {
	
	public DotStaffChatCommand(ChatManager manager) {
		super(manager, ".sc", ".staffchat");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/.sc <message>"));
			return;
		}
		
		String message = Joiner.on(' ').join(args);
		message = ChatColor.translateAlternateColorCodes('&', message);
		//message = Emote.replaceEmotes(message, ChatColor.GRAY);
		
		manager.getStaffChatPMSC().send(sender instanceof ProxiedPlayer ? sender.getName() : M.CONSOLE_NAME,
				ComponentSerializer.toString(TextComponent.fromLegacyText(message, ChatColor.GRAY)));
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return allNetworkPlayers();
	}
	
}
