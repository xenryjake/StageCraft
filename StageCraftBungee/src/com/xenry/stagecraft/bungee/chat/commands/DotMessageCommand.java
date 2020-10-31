package com.xenry.stagecraft.bungee.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.bungee.chat.ChatManager;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotMessageCommand extends ProxyAdminCommand<ChatManager> {
	
	public DotMessageCommand(ChatManager manager){
		super(manager, ".message", ".msg", ".m", ".tell", ".t", ".whisper", ".w", ".privatemessage",
				".pmsg", ".pm");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(M.error("Console support coming soon."));
			return;
		}
		if(args.length < 2){
			sender.sendMessage(M.usage("/.msg <player> <message>"));
			return;
		}
		ProxiedPlayer target = manager.plugin.getProxy().getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		BaseComponent[] msg = TextComponent.fromLegacyText(Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length)));
		manager.handlePrivateMessage((ProxiedPlayer)sender, target, msg);
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return allNetworkPlayers();
	}
	
}
