package com.xenry.stagecraft.bungee.proxy.commands;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotSendCommand extends ProxyAdminCommand<ProxyManager> {
	
	public DotSendCommand(ProxyManager manager){
		super(manager, ".send");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/.send <player> <server>"));
			return;
		}
		
		List<ProxiedPlayer> targets = new ArrayList<>();
		if(args[0].equalsIgnoreCase("all")){
			targets.addAll(manager.plugin.getProxy().getPlayers());
		}else if(args[0].equalsIgnoreCase("current")){
			if(!(sender instanceof ProxiedPlayer)){
				sender.sendMessage(M.error("Cannot use \"current\" from console."));
				return;
			}
			targets.addAll(((ProxiedPlayer)sender).getServer().getInfo().getPlayers());
		}else{
			boolean matchedServer = false;
			ServerInfo server = manager.plugin.getProxy().getServerInfo(args[1]);
			if(server != null){
				matchedServer = true;
				targets.addAll(manager.plugin.getProxy().getPlayers());
			}
			if(!matchedServer){
				ProxiedPlayer player = manager.plugin.getProxy().getPlayer(args[0]);
				if(player == null){
					sender.sendMessage(M.playerNotFound(args[0]));
					return;
				}
				targets.add(player);
			}
		}
		ServerInfo server = manager.plugin.getProxy().getServerInfo(args[1]);
		if(server == null){
			sender.sendMessage(M.error("Server not found: " + args[1]));
			return;
		}
		
		sender.sendMessage(M.msg("Attempting to send " + M.elm + targets.size() + M.msg + " players to " + M.elm + server.getName() + M.msg + "..."));
		
		for(ProxiedPlayer player : targets){
			manager.getPlayerWillSwitchPMSC().send(player);
		}
		manager.plugin.getProxy().getScheduler().schedule(manager.plugin, () -> {
			for(ProxiedPlayer player : targets){
				if(player.isConnected()){
					player.connect(server);
				}
			}
		}, 1, TimeUnit.SECONDS);
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		switch(args.length){
			case 0:
			case 1:
				List<String> results = new ArrayList<>(allNetworkPlayers());
				results.add("current");
				results.add("all");
				results.addAll(manager.plugin.getProxy().getServers().keySet());
				return results;
			case 2:
				return manager.plugin.getProxy().getServers().keySet();
			default:
				return Collections.emptyList();
		}
	}
	
}
