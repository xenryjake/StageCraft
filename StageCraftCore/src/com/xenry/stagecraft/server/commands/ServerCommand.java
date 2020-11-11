package com.xenry.stagecraft.server.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.BungeeUtil;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ServerCommand extends Command<Core,ServerManager> {
	
	public ServerCommand(ServerManager manager){
		super(manager, Rank.MEMBER, "server", "servers", "goto");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Set<String> servers = manager.getServerNames();
		if(args.length < 1){
			ComponentBuilder cb = new ComponentBuilder("Available servers: ").color(M.msg);
			if(servers.isEmpty()){
				cb.append("none").color(ChatColor.RED);
			}else{
				Iterator<String> it = servers.iterator();
				while(it.hasNext()){
					String server = it.next();
					ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server);
					HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go to " + server));
					cb.append(server).color(M.WHITE).event(ce).event(he);
					if(it.hasNext()){
						cb.append(", ").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null);
					}
				}
			}
			profile.sendMessage(cb.create());
			return;
		}
		for(String server : servers){
			if(server.equalsIgnoreCase(args[0])){
				profile.sendMessage(M.msg + "Sending you to " + M.elm + server + M.msg + "...");
				manager.plugin.getProfileManager().save(profile);
				manager.plugin.getServer().getScheduler().runTaskLater(manager.plugin,
						() -> BungeeUtil.connect(profile.getPlayer(), server), 20L);
				return;
			}
		}
		profile.sendMessage(M.error("Server not found: " + args[0]));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? new ArrayList<>(manager.getNetworkPlayers().keySet()) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
