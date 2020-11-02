package com.xenry.stagecraft.commands.general;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.commands.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
	
	public SudoCommand(CommandManager manager){
		super(manager, Rank.ADMIN, "sudo");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <command>"));
			return;
		}
		Player target;
		if(args[0].equals("**")){
			target = null;
		}else{
			target = Bukkit.getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}
		String targetName = target == null ? "everyone" : target.getName();
		String command = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		if(command.toLowerCase().startsWith("c:")){
			command = command.substring(2);
			sender.sendMessage(M.msg + "Forcing " + M.elm + targetName + M.msg + " to chat: " + M.WHITE + command);
			chat(target, command);
		}else{
			sender.sendMessage(M.msg + "Forcing " + M.elm + targetName + M.msg + " to execute command: " + M.WHITE
					+ "/" + command);
			chat(target, "/" + command);
		}
	}
	
	private void chat(Player target, String message){
		if(target == null){
			for(Player player : Bukkit.getOnlinePlayers()){
				player.chat(message);
			}
		}else{
			target.chat(message);
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			List<String> players = new ArrayList<>(allNetworkPlayers());
			players.add("**");
			return players;
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		if(args.length == 1){
			List<String> players = new ArrayList<>(allNetworkPlayers());
			players.add("**");
			return players;
		}else{
			return Collections.emptyList();
		}
	}
	
}
