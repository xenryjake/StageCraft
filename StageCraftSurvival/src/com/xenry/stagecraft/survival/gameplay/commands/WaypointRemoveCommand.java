package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WaypointRemoveCommand extends Command<Survival,GameplayManager> {
	
	public WaypointRemoveCommand(GameplayManager manager){
		super(manager, Rank.MOD, "delete", "remove", "del", "rem");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/waypoint " + label + " <name>"));
			return;
		}
		String name = args[0];
		profile.getPlayer().chat("/dmarker delete " + name + " set:waypoints");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/waypoint " + label + " <name>"));
			return;
		}
		String name = args[0];
		manager.plugin.getServer().dispatchCommand(sender, "dmarker delete " + name + " set:waypoints");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
