package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
import com.xenry.stagecraft.util.LocationVector;
import com.xenry.stagecraft.util.M;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandSetHomeCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandSetHomeCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "sethome", "setspawn");
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
		if(!island.getOwnerUUID().equals(profile.getUUID())){
			profile.sendMessage(M.error("You are not the owner of this island."));
			return;
		}
		Location location = profile.getPlayer().getLocation();
		if(location.getWorld() != manager.getWorld()){
			profile.sendMessage(M.error("You aren't in the SkyBlock world."));
			return;
		}
		if(!island.isInBuildArea(location)){
			profile.sendMessage(M.error("You can't set the island home here."));
			return;
		}
		LocationVector vector = new LocationVector(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		island.setHome(vector);
		profile.sendMessage(M.msg + "Set island home to your current location.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
