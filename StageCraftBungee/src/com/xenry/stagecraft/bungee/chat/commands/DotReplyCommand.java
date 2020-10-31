package com.xenry.stagecraft.bungee.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.bungee.chat.ChatManager;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotReplyCommand extends ProxyAdminCommand<ChatManager> {
	
	public DotReplyCommand(ChatManager manager){
		super(manager, ".reply", ".respond", ".r");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(M.error("Console support coming soon."));
			return;
		}
		if(args.length < 1){
			sender.sendMessage(M.usage("/.msg <message>"));
			return;
		}
		BaseComponent[] msg = TextComponent.fromLegacyText(Joiner.on(' ').join(args));
		manager.handlePrivateMessage((ProxiedPlayer)sender, ChatManager.PM_REPLY_KEY, msg);
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return allNetworkPlayers();
	}
	
}
