package com.xenry.stagecraft.survival.teleportation.commands.home;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.Home;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DeleteHomeCommand extends PlayerCommand<Survival,TeleportationManager> {
	
	public DeleteHomeCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "deletehome", "removehome", "delhome", "homedelete", "homeremove", "homedel");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <name>"));
			return;
		}
		Home home = manager.getHomeHandler().getHome(profile, args[0]);
		if(home == null){
			profile.sendMessage(M.error("That home does not exist: " + args[0]));
			return;
		}
		manager.getHomeHandler().deleteHome(home);
		profile.sendMessage(M.msg + "Deleted home " + M.elm + home.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getHomeHandler().getHomeNameList(profile, args[0]) : Collections.emptyList();
	}
	
}
