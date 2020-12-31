package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
import com.xenry.stagecraft.util.M;
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
public final class IslandLeaveCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandLeaveCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "leave");
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
		if(island.getOwnerUUID().equals(profile.getUUID())){
			profile.sendMessage(M.error("You can't leave an island you own."));
			return;
		}
		//todo remove member, switch active profile, switch inventory, etc
		island.setOwnerUUID(profile.getUUID());
		profile.sendMessage(M.msg + "You have left island " + M.elm + island.getID() + M.msg + ".");
		manager.sendMessageToIsland(island, M.elm + profile.getLatestUsername() + M.msg + " has left island " + M.elm + island.getID() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
