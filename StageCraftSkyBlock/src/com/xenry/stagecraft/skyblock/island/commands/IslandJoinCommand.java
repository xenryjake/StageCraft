package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Invite;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
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
public final class IslandJoinCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandJoinCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "join");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/island join <island-id>"));
			return;
		}
		Island island = manager.getIsland(args[0]);
		if(island == null){
			profile.sendMessage(M.error("That island doesn't exist."));
			return;
		}
		Invite invite = manager.getInvite(profile.getUUID(), island.getID());
		if(invite == null){
			profile.sendMessage(M.error("You aren't invited to " + M.elm + island.getID() + M.err + "."));
			return;
		}
		manager.removeInvite(invite);
		island.addMember(profile.getUUID());
		//todo switch active profile, switch inventory, etc
		manager.sendMessageToIsland(island, M.elm + profile.getLatestUsername() + M.msg + " has joined island " + M.elm + island.getID() + M.msg + "!");
		profile.sendMessage(M.msg + "You have joined island " + M.elm + island.getID() + M.msg + "!");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getIslandIDs(args[0]) : Collections.emptyList();
	}
	
}
