package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HNSKickCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSKickCommand(HideNSeekManager manager){
		super(manager, Rank.ADMIN, "kick");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(err + "Please specify a player.");
			return;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(err + "Could not find player: " + args[0]);
			return;
		}
		
		if(manager.getPlayerHandler().getPlayerMode(target) == null){
			sender.sendMessage(err + target.getName() + " isn't in a game.");
			return;
		}
		if(!manager.getPlayerHandler().removePlayer(target)){
			sender.sendMessage(err + "Failed to kick " + target.getName() + " from the game.");
			return;
		}
		sender.sendMessage(err + "Kicked " + target.getName() + " from the game.");
		target.sendMessage(err + "You have been removed from the game.");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return PlayerUtil.getOnlinePlayerNames();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return PlayerUtil.getOnlinePlayerNames();
	}
	
}
