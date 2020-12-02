package com.xenry.stagecraft.survival.teleportation.commands.warp;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
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
public final class SetSpawnCommand extends Command<Survival,TeleportationManager> {
	
	public SetSpawnCommand(TeleportationManager manager){
		super(manager, Rank.ADMIN, "setspawn");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Location location = profile.getPlayer().getLocation();
		if(LocationUtil.isBlockUnsafe(location)){
			profile.sendMessage(M.err + "Warning! The spawn point you set is considered unsafe.");
		}
		manager.getWarpHandler().setSpawn(location);
		profile.sendMessage(M.msg + "Spawn point set.");
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
