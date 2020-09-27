package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SudoCommand extends Command<Survival,GameplayManager> {
	
	public SudoCommand(GameplayManager manager){
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
		
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++){
			sb.append(args[i]).append(" ");
		}
		String command = sb.toString().trim();
		if(command.toLowerCase().startsWith("c:")){
			command = command.substring(2);
			chat(target, command);
			sender.sendMessage(M.msg + "Forced " + M.elm + targetName + M.msg + " to chat: " + M.WHITE + command);
		}else{
			chat(target, "/" + target);
			sender.sendMessage(M.msg + "Forced " + M.elm + targetName + M.msg + " to execute command: " + M.WHITE
					+ "/" + command);
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
			List<String> players = PlayerUtil.getOnlinePlayerNames();
			players.add("**");
			return players;
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		if(args.length == 1){
			List<String> players = PlayerUtil.getOnlinePlayerNames();
			players.add("**");
			return players;
		}else{
			return Collections.emptyList();
		}
	}
	
}
