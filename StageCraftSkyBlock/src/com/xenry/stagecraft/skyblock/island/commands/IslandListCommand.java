package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandListCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandListCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "list");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		List<Island> islands = manager.getJoinedIslands(profile);
		if(islands.size() < 1){
			profile.sendMessage(M.error("You haven't joined any islands."));
			return;
		}
		profile.sendMessage(M.msg + "Your islands:");
		for(Island island : islands){
			profile.sendMessage(M.arrow(M.elm + island.getID() + M.gry + ": " + M.WHITE + island.getName()));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
