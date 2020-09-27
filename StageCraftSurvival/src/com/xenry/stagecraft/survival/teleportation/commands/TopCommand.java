package com.xenry.stagecraft.survival.teleportation.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TopCommand extends Command<Survival,TeleportationManager> {
	
	public TopCommand(TeleportationManager manager){
		super(manager, Rank.ADMIN, "top");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		Location location = player.getLocation();
		World world = location.getWorld();
		if(world == null){
			throw new IllegalArgumentException("Location#getWorld cannot be null");
		}
		location.setY(world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 1);
		location = LocationUtil.getSafeTeleportDestination(location, player);
		manager.createAndExecuteTeleportation(player, player, player.getLocation(), location, Teleportation.Type.ADMIN, false);
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
