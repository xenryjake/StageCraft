package com.xenry.stagecraft.bungee.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.bungee.chat.ChatManager;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotBroadcastCommand extends ProxyAdminCommand<ChatManager> {
	
	public DotBroadcastCommand(ChatManager manager){
		super(manager, ".broadcast", ".bcast", ".bc");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/.bc <message>"));
			return;
		}
		manager.plugin.getProxy().broadcast(
				new ComponentBuilder(sender instanceof ProxiedPlayer ? sender.getName() : M.CONSOLE_NAME)
				.color(ChatColor.AQUA).bold(true)
				.append(": ").color(ChatColor.DARK_GRAY).bold(false)
				.append(TextComponent.fromLegacyText(Joiner.on(' ').join(args), ChatColor.LIGHT_PURPLE))
				.color(ChatColor.LIGHT_PURPLE).bold(false).create());
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return allNetworkPlayers();
	}
	
	
}
