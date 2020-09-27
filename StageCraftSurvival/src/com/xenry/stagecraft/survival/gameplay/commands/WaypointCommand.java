package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/30/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WaypointCommand extends Command<Survival,GameplayManager> {
	
	public WaypointCommand(GameplayManager manager){
		super(manager, Rank.MOD, "waypoint", "wayp", "wp");
		addSubCommand(new WaypointAddCommand(manager));
		addSubCommand(new WaypointRemoveCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "§lWaypoint Commands:");
		sender.sendMessage(M.help(label + " add <name> [icon]",
				"Add a new waypoint where you're standing."));
		sender.sendMessage(M.help(label + " remove <name>", "Remove a waypoint."));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Arrays.asList("add", "remove");
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Arrays.asList("add", "remove");
	}
}
