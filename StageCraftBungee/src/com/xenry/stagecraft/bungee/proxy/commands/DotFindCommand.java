package com.xenry.stagecraft.bungee.proxy.commands;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotFindCommand extends ProxyAdminCommand<ProxyManager> {
	
	public DotFindCommand(ProxyManager manager){
		super(manager, ".find");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/.find <player>"));
			return;
		}
		ProxiedPlayer player = manager.plugin.getProxy().getPlayer(args[0]);
		if(player == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		Server server = player.getServer();
		if(server == null){
			sender.sendMessage(M.error(args[0] + " is not online."));
			return;
		}
		sender.sendMessage(M.msg(M.elm + player.getName() + M.msg + " is online at " + M.elm + server.getInfo().getName() + M.msg + "."));
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return allNetworkPlayers();
	}
	
}
