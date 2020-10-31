package com.xenry.stagecraft.bungee.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.bungee.chat.ChatManager;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotSayCommand extends ProxyAdminCommand<ChatManager> {

	public DotSayCommand(ChatManager manager){
		super(manager, ".say");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/.say <message>"));
			return;
		}
		String message = ChatColor.translateAlternateColorCodes('&', Joiner.on(' ').join(args));
		//message = Emote.replaceEmotes(message, ChatColor.WHITE);
		String name = sender instanceof ProxiedPlayer ? sender.getName() : M.CONSOLE_NAME;
		manager.plugin.getProxy().broadcast(TextComponent.fromLegacyText("§e§l" + name + "§8:§r " + message));
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return allNetworkPlayers();
	}
	
}
