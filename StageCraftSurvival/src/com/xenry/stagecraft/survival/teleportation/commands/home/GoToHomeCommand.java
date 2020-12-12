package com.xenry.stagecraft.survival.teleportation.commands.home;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.Home;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.survival.teleportation.commands.TPCommand;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GoToHomeCommand extends PlayerCommand<Survival,TeleportationManager> {
	
	public GoToHomeCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "gotohome");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <home>"));
			return;
		}
		Profile profile;
		if(Bukkit.getPlayer(args[0]) != null){
			profile = getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
		}else{
			profile = getCore().getProfileManager().getProfileByLatestUsername(args[0]);
		}
		if(profile == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		Home home = manager.getHomeHandler().getHome(profile, args[1]);
		if(home == null){
			sender.sendMessage(M.error(M.elm + profile.getLatestUsername() + M.err + " doesn't have a home named " + M.elm + args[1] + M.err + "."));
			return;
		}
		boolean isAdmin = TPCommand.SELF_RANK.has(sender);
		manager.createAndExecuteTeleportation(sender.getPlayer(), sender.getPlayer(), sender.getPlayer().getLocation(), home.getLocation(), isAdmin ? Teleportation.Type.ADMIN : Teleportation.Type.WARP, !isAdmin);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
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
