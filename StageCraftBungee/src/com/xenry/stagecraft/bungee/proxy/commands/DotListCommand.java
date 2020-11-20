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

import java.util.Collection;
import java.util.Iterator;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DotListCommand extends ProxyAdminCommand<ProxyManager> {
	
	public DotListCommand(ProxyManager manager){
		super(manager, ".list", ".who", "glist");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(M.msg("Total online players: " + M.elm + manager.plugin.getProxy().getOnlineCount()));
		for(ServerInfo info : manager.plugin.getProxy().getServers().values()){
			Collection<ProxiedPlayer> players = info.getPlayers();
			ComponentBuilder cb = new ComponentBuilder("  " + info.getName() + " ").color(M.msg)
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + info.getName()))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go to " + info.getName())));
			cb.append("(" + players.size() + ")").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null)
					.append(": ").color(ChatColor.DARK_GRAY);
			Iterator<ProxiedPlayer> it = players.iterator();
			while(it.hasNext()){
				String name = it.next().getName();
				ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name + " ");
				HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Message " + name));
				cb.append(name).color(ChatColor.WHITE).event(ce).event(he);
				if(it.hasNext()){
					cb.append(", ").color(ChatColor.DARK_GRAY).event((ClickEvent)null).event((HoverEvent)null);
				}
			}
			sender.sendMessage(cb.create());
		}
	}
	
}
