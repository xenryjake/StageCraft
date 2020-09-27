package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RestCommand extends Command<Survival,GameplayManager> {
	
	public RestCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "rest");
		setCanBeDisabled(true);
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
		rest(target);
		sender.sendMessage(M.msg + "Rested " + M.elm + targetName + M.msg + ".");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
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
		rest(target);
		sender.sendMessage(M.msg + "Rested " + M.elm + targetName + M.msg + ".");
	}
	
	private void rest(Player player){
		if(player == null){
			for(Player target : Bukkit.getOnlinePlayers()){
				target.setStatistic(Statistic.TIME_SINCE_REST, 0);
			}
		}else{
			player.setStatistic(Statistic.TIME_SINCE_REST, 0);
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
