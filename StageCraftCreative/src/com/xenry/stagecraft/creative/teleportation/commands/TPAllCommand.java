package com.xenry.stagecraft.creative.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPAllCommand extends PlayerCommand<Creative,TeleportationManager> {
	
	public TPAllCommand(TeleportationManager manager){
		super(manager, TPCommand.OTHER_RANK, "tpall", "tpoall");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		boolean safe = !label.contains("o");
		Player teleporter = profile.getPlayer();
		Location location = teleporter.getLocation();
		for(Player player : Bukkit.getOnlinePlayers()){
			manager.createAndExecuteTeleportation(player, teleporter, player.getLocation(), location, Teleportation.Type.ADMIN, safe);
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
