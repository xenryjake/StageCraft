package com.xenry.stagecraft.server.commands;
import com.earth2me.essentials.Essentials;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ListCommand extends Command<Core,ServerManager> {
	
	//todo deal with vanished & afk
	
	public static final Access SEE_VANISHED = Rank.HEAD_MOD;
	
	public ListCommand(ServerManager manager){
		super(manager, Rank.MEMBER, "list", "who", "online", "ls");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		sendNetworkList(profile.getPlayer());
		//sendList(profile.getPlayer(), SEE_VANISHED.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sendNetworkList(sender);
		//sendList(sender, true);
	}
	
	private void sendNetworkList(CommandSender sender){
		Map<String,List<String>> playerServerList = manager.getNetworkPlayers();
		List<String> allPlayers = allNetworkPlayers();
		sender.sendMessage(M.msg + "Total online players: " + M.elm + allPlayers.size());
		for(Map.Entry<String,List<String>> entry : playerServerList.entrySet()){
			List<String> players = entry.getValue();
			String server = entry.getKey();
			ComponentBuilder cb = new ComponentBuilder("  " + server + " ").color(M.msg)
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go to " + server)));
			cb.append("(" + players.size() + ")").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null)
					.append(": ").color(ChatColor.DARK_GRAY);
			Iterator<String> it = players.iterator();
			while(it.hasNext()){
				String name = it.next();
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
	
	private void sendList(CommandSender sender, boolean seeVanished){
		StringBuilder sb = new StringBuilder();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		Essentials ess = getCore().getIntegrationManager().getEssentials();
		int amount = 0;
		for(Player player : players){
			boolean vanished = ess != null && ess.getUser(player).isVanished();
			boolean afk = ess != null && ess.getUser(player).isAfk();
			if(vanished && !seeVanished){
				continue;
			}
			sb.append(M.WHITE).append(player.getDisplayName());
			if(afk){
				sb.append(M.gry).append(" [AFK]");
			}
			if(vanished){
				sb.append(M.gry).append(" [VANISHED]");
			}
			sb.append(M.DGRAY).append(", ");
			amount++;
		}
		String playersString = sb.toString().trim();
		if(playersString.endsWith(",")){
			playersString = playersString.substring(0, playersString.length() - 1);
		}
		sender.sendMessage(M.msg + M.BOLD + "Online Players (" + M.elm + M.BOLD + amount + M.msg + M.BOLD + "):");
		sender.sendMessage(M.arrow(playersString));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
