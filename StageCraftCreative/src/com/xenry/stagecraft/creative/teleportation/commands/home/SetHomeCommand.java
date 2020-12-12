package com.xenry.stagecraft.creative.teleportation.commands.home;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Home;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
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
public final class SetHomeCommand extends PlayerCommand<Creative,TeleportationManager> {
	
	public static final int MAX_HOMES_PER_PLAYER = 50;
	
	public SetHomeCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "sethome", "addhome", "homeset", "homeadd");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <name>"));
			return;
		}
		String name = args[0];
		if(!name.matches("^[A-Za-z0-9_]+$")){
			profile.sendMessage(M.error("Home names can only contain alphanumeric characters and underscores."));
			return;
		}
		if(name.replaceAll("[0-9_]", "").isEmpty()){
			profile.sendMessage(M.error("Home names must contain at least one letter."));
			return;
		}
		if(name.length() > 32 || name.length() < 1){
			profile.sendMessage(M.error("Home names must be between 1 and 32 characters long."));
			return;
		}
		if(manager.getHomeHandler().getHome(profile, name) != null){
			profile.sendMessage(M.error("A home with that name already exists."));
			return;
		}
		if(manager.getHomeHandler().getHomes(profile).size() >= MAX_HOMES_PER_PLAYER){
			profile.sendMessage(M.error("You already reached the maximum number of homes."));
			return;
		}
		manager.getHomeHandler().addHome(new Home(profile, name, profile.getPlayer().getLocation()));
		profile.sendMessage(M.msg + "You created a new home: " + M.elm + name + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
