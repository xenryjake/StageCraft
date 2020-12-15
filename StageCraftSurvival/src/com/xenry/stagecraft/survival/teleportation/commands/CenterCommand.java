package com.xenry.stagecraft.survival.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.LocationUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class CenterCommand extends PlayerCommand<Survival,TeleportationManager> {
	
	public CenterCommand(TeleportationManager manager){
		super(manager, Rank.HEAD_MOD, "center");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		manager.createAndExecuteTeleportation(player, player, player.getLocation(),
				LocationUtil.center(player.getLocation()), Teleportation.Type.ADMIN, false);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
