package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BurnCommand extends Command<Creative,GameplayManager> {
	
	public BurnCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "burn");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <ticks>"));
			return;
		}
		Player target = null;
		if(!args[0].equals("**")){
			target = Bukkit.getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}
		int ticks;
		try{
			ticks = Integer.parseInt(args[1]);
		}catch(Exception ex){
			sender.sendMessage(M.error("Invalid integer: " + args[1]));
			return;
		}
		String targetName = target == null ? "everyone" : target.getName();
		if(target == null){
			for(Player player : Bukkit.getOnlinePlayers()){
				player.setFireTicks(ticks);
			}
		}else{
			target.setFireTicks(ticks);
		}
		sender.sendMessage(M.msg + "Set " + M.elm + targetName + M.msg + " on fire for " + M.elm + ticks
				+ " ticks" + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			List<String> players = new ArrayList<>(allLocalPlayers());
			players.add("**");
			return filter(players, args[0]);
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		if(args.length == 1){
			List<String> players = new ArrayList<>(allLocalPlayers());
			players.add("**");
			return filter(players, args[0]);
		}else{
			return Collections.emptyList();
		}
	}
	
}
