package com.xenry.stagecraft.bungee.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.bungee.chat.ChatManager;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotFakeMessageCommand extends ProxyAdminCommand<ChatManager> {
	
	public DotFakeMessageCommand(ChatManager manager){
		super(manager, ".fm");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/.fm <message>"));
			return;
		}
		manager.plugin.getProxy().broadcast(TextComponent.fromLegacyText(
				ChatColor.translateAlternateColorCodes('&', Joiner.on(' ').join(args))));
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return allNetworkPlayers();
	}
	
}
