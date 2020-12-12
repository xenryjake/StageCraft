package com.xenry.stagecraft.survival.teleportation.commands.home;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.Home;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/30/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DeleteHomeOtherCommand extends Command<Survival,TeleportationManager> {
	
	public DeleteHomeOtherCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "deletehomeother", "removehomeother", "delhomeother", "homedeleteother", "homeremoveother", "homedelother");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <home>"));
			return;
		}
		Profile target;
		if(Bukkit.getPlayer(args[0]) != null){
			target = getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
		}else{
			target = getCore().getProfileManager().getProfileByLatestUsername(args[0]);
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		Home home = manager.getHomeHandler().getHome(target, args[1]);
		if(home == null){
			sender.sendMessage(M.error(M.elm + target.getLatestUsername() + M.err + " doesn't have a home named " + M.elm + args[1] + M.err + "."));
			return;
		}
		manager.getHomeHandler().deleteHome(home);
		sender.sendMessage(M.msg + "Deleted " + M.elm + target.getLatestUsername() + M.msg + "'s home named " + M.elm + home.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return serverTabComplete(profile.getPlayer(), args, label);
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 1:
				return localPlayers(args[0]);
			case 2:{
				Player player = Bukkit.getPlayer(args[0]);
				if(player == null){
					return Collections.emptyList();
				}
				Profile target = getCore().getProfileManager().getProfile(player);
				if(target == null){
					return Collections.emptyList();
				}
				return filter(manager.getHomeHandler().getHomeNameList(target), args[1]);
			}
			default:
				return Collections.emptyList();
		}
	}
	
}
