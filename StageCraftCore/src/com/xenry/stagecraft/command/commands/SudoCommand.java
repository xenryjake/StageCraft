package com.xenry.stagecraft.command.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.command.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SudoCommand extends Command<Core,CommandManager> {
	
	public static final Access PLAYER_CHAT = Rank.HEAD_MOD;
	public static final Access PLAYER_CMD = Rank.ADMIN;
	public static final Access CONSOLE_CMD = Rank.ADMIN;
	
	public SudoCommand(CommandManager manager){
		super(manager, Rank.HEAD_MOD, "sudo");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		handle(profile.getPlayer(), args, PLAYER_CHAT.has(profile), PLAYER_CMD.has(profile), CONSOLE_CMD.has(profile),
				profile.getPlayer());
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		Player pluginMessageSender = PlayerUtil.getAnyPlayer();
		if(pluginMessageSender == null){
			sender.sendMessage(M.error("There are no players on this server. Please try again on another server or the proxy."));
			return;
		}
		handle(sender, args, true, true, true, pluginMessageSender);
	}
	
	private void handle(CommandSender sender, String[] args, boolean playerChat, boolean playerCmd,
						boolean consoleCmd, Player pluginMessageSender) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/sudo <target> <command>"));
			return;
		}
		String target = args[0];
		String content = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		boolean isConsole = target.startsWith("#");
		if(content.toLowerCase().startsWith("sudo")){
			sender.sendMessage(M.error("You can't create sudo loops."));
			return;
		}
		boolean isChat = false;
		if(content.toLowerCase().startsWith("c:")){
			content = content.substring(2);
			isChat = true;
		}else if(!isConsole){
			content = "/" + content;
		}
		if(isConsole){
			if(!consoleCmd){
				sender.sendMessage(M.error("You can't run console commands."));
				return;
			}
			if(isChat){
				sender.sendMessage(M.error("Only players can send chat. Use the " + M.elm + "say" + M.msg + " command."));
				return;
			}
		}else{
			if(isChat){
				if(!playerChat){
					sender.sendMessage(M.error("You can't run player chats."));
					return;
				}
			}else{
				if(!playerCmd){
					sender.sendMessage(M.error("You can't run player commands."));
					return;
				}
			}
		}
		
		boolean localConsole;
		if(target.equals("**") || target.equals("#**") || target.equalsIgnoreCase("#proxy")){
			localConsole = false;
		}else if(target.equalsIgnoreCase("#console")){
			localConsole = true;
		}else if(target.startsWith("#")){
			localConsole = false;
			target = target.substring(1);
			boolean found = false;
			for(String name : manager.plugin.getServerManager().getServerNames()){
				if(name.equalsIgnoreCase(target)){
					target = "#" + name;
					found = true;
					break;
				}
			}
			if(!found){
				sender.sendMessage(M.error("Invalid server: " + target));
				return;
			}
		}else{
			localConsole = false;
			boolean found = false;
			for(String name : allNetworkPlayers()){
				if(name.equalsIgnoreCase(target)){
					target = name;
					found = true;
					break;
				}
			}
			if(!found){
				sender.sendMessage(M.playerNotFound(args[0]) + " (use exact username)");
				return;
			}
		}
		
		sender.sendMessage(M.msg + "Forcing " + M.elm + target + M.msg +
				(isChat ? " to chat: " : " to execute command: ") + M.WHITE + content);
		if(sender instanceof Player){
			Log.toCS(M.msg + "Handling sudo for " + M.elm + target + M.msg + " by " + M.elm + sender.getName() + M.msg
					+ " on: " + M.WHITE + content);
		}
		
		if(localConsole){
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), content);
			return;
		}
		manager.getSudoPMSC().send(pluginMessageSender, sender.getName(), target, content);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			List<String> players = new ArrayList<>(allNetworkPlayers());
			players.add("**");
			if(CONSOLE_CMD.has(profile)){
				players.add("#console");
				players.add("#proxy");
				players.add("#**");
				for(String server : manager.plugin.getServerManager().getServerNames()){
					players.add("#" + server);
				}
			}
			return players;
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		if(args.length == 1){
			List<String> players = new ArrayList<>(allNetworkPlayers());
			players.add("**");
			players.add("#console");
			players.add("#proxy");
			players.add("#**");
			for(String server : manager.plugin.getServerManager().getServerNames()){
				players.add("#" + server);
			}
			return players;
		}else{
			return Collections.emptyList();
		}
	}
	
}
