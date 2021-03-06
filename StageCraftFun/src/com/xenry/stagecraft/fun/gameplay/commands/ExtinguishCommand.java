package com.xenry.stagecraft.fun.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.fun.Fun;
import com.xenry.stagecraft.fun.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ExtinguishCommand extends Command<Fun,GameplayManager> {
	
	public ExtinguishCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "extinguish", "ext");
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		Player target = sender.getPlayer();
		if(args.length > 0){
			if(args[0].equals("**")){
				target = null;
			}else {
				target = Bukkit.getPlayer(args[0]);
				if(target == null){
					sender.sendMessage(M.playerNotFound(args[0]));
					return;
				}
			}
		}
		String targetName = target == null ? "everyone" : target.getName();
		if(target == null){
			for(Player player : Bukkit.getOnlinePlayers()){
				player.setFireTicks(0);
			}
		}else{
			target.setFireTicks(0);
		}
		sender.sendMessage(M.msg + "Extinguished " + (target == sender.getPlayer() ? "yourself." : M.elm
				+ targetName + M.msg + "."));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + ""));
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
		if(target == null){
			for(Player player : Bukkit.getOnlinePlayers()){
				player.setFireTicks(0);
			}
		}else{
			target.setFireTicks(0);
		}
		sender.sendMessage(M.msg + "Extinguished " + M.elm + targetName + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0], "**") : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0], "**") : Collections.emptyList();
	}
	
}
