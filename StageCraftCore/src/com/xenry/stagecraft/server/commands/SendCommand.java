package com.xenry.stagecraft.server.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.CollectionUtil;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SendCommand extends Command<Core,ServerManager> {
	
	public SendCommand(ServerManager manager){
		super(manager, Rank.MOD, "send");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <server>"));
			return;
		}
		Player pluginMessageSender;
		if(sender instanceof Player){
			pluginMessageSender = (Player)sender;
		}else{
			pluginMessageSender = PlayerUtil.getAnyPlayer();
			if(pluginMessageSender == null){
				sender.sendMessage(M.error("There are no players on this server. Please try again on another server or the proxy."));
				return;
			}
		}
		
		List<String> targets = new ArrayList<>();
		if(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("**")){
			targets.addAll(allNetworkPlayers());
		}else if(args[0].equalsIgnoreCase("current")){
			targets.addAll(PlayerUtil.getOnlinePlayerNames());
		}else{
			boolean matchedServer = false;
			for(Map.Entry<String,List<String>> entry : manager.getNetworkPlayers().entrySet()){
				if(args[0].equalsIgnoreCase(entry.getKey())){
					targets.addAll(entry.getValue());
					matchedServer = true;
					break;
				}
			}
			if(!matchedServer){
				String name = CollectionUtil.findClosestMatchByStart(allNetworkPlayers(), args[0]);
				if(name == null){
					sender.sendMessage(M.playerNotFound(args[0]));
					return;
				}
				targets.add(name);
			}
		}
		String serverName = CollectionUtil.findClosestMatchByStart(manager.getNetworkPlayers().keySet(), args[1]);
		if(serverName == null){
			sender.sendMessage(M.error("Server not found: " + args[1]));
			return;
		}
		
		sender.sendMessage(M.msg + "Attempting to send " + M.elm + targets.size() + M.msg + " players to " + M.elm + serverName + M.msg + "...");
		manager.getSendPlayersPMSC().send(pluginMessageSender, targets, serverName);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				List<String> results = new ArrayList<>(allNetworkPlayers());
				results.add("current");
				results.add("all");
				results.addAll(manager.getNetworkPlayers().keySet());
				return filter(results, args[0]);
			case 2:
				return filter(new ArrayList<>(manager.getNetworkPlayers().keySet()), args[1]);
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 1:
				List<String> results = new ArrayList<>(allNetworkPlayers());
				results.add("current");
				results.add("all");
				results.addAll(manager.getNetworkPlayers().keySet()); //servers
				return filter(results, args[0]);
			case 2:
				return filter(new ArrayList<>(manager.getNetworkPlayers().keySet()), args[1]); //servers
			default:
				return Collections.emptyList();
		}
	}
	
}
