package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
import com.xenry.stagecraft.skyblock.teleportation.Teleportation;
import com.xenry.stagecraft.util.LocationVector;
import com.xenry.stagecraft.util.M;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandHomeCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandHomeCommand(IslandManager manager) {
		super(manager, Rank.MEMBER, "home", "spawn");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		SkyBlockProfile sbProfile = manager.plugin.getSkyBlockProfileManager().getProfile(profile);
		if(sbProfile == null){
			profile.sendMessage(M.error("Could not find your SkyBlock profile."));
			return;
		}
		Island island = sbProfile.getActiveIsland(manager);
		if(island == null){
			profile.sendMessage(M.error("You do not have an active island."));
			return;
		}
		LocationVector vector = island.getHomeVector();
		Location location = new Location(manager.getWorld(), vector.x, vector.y, vector.z, vector.yaw, vector.pitch);
		manager.plugin.getTeleportationManager().createAndExecuteTeleportation(profile.getPlayer(), profile.getPlayer(), profile.getPlayer().getLocation(), location, Teleportation.Type.HOME, true);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
