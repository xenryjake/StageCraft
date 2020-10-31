package com.xenry.stagecraft.bungee.proxy.commands;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotServerCommand extends ProxyAdminCommand<ProxyManager> {
	
	public DotServerCommand(ProxyManager manager){
		super(manager, ".server", ".servers");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			onlyForPlayers(sender);
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer)sender;
		Map<String,ServerInfo> servers = manager.plugin.getProxy().getServers();
		if(args.length < 1){
			ComponentBuilder cb = new ComponentBuilder("Available servers: ").color(M.msg);
			if(servers.isEmpty()){
				cb.append("none").color(ChatColor.RED);
			}else{
				Iterator<String> it = servers.keySet().iterator();
				while(it.hasNext()){
					String server = it.next();
					ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/.server " + server);
					HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go to " + server + " (proxy command)"));
					cb.append(server).color(M.WHITE).event(ce).event(he);
					if(it.hasNext()){
						cb.append(", ").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null);
					}
				}
			}
			sender.sendMessage(cb.create());
			return;
		}
		for(Map.Entry<String,ServerInfo> entry : servers.entrySet()){
			if(entry.getKey().equalsIgnoreCase(args[0])){
				sender.sendMessage(M.msg("Sending you to " + M.elm + entry.getKey() + M.msg + "..."));
				manager.getPlayerWillSwitchPMSC().send(player);
				manager.plugin.getProxy().getScheduler().schedule(manager.plugin,
						() -> player.connect(entry.getValue()), 1, TimeUnit.SECONDS);
				return;
			}
		}
		sender.sendMessage(M.error("Server not found: " + args[0]));
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		return args.length <= 1 ? manager.plugin.getProxy().getServers().keySet() : Collections.emptyList();
	}
	
}
