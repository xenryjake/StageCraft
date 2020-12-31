package com.xenry.stagecraft.fun.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.fun.Fun;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.fun.teleportation.TeleportRequest;
import com.xenry.stagecraft.fun.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.M;
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
public final class TPCancelCommand extends PlayerCommand<Fun,TeleportationManager> {
	
	public TPCancelCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "tpcancel");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		List<TeleportRequest> requests = manager.getRequestsFrom(profile.getPlayer());
		if(requests.isEmpty()){
			profile.sendMessage(M.error("You don't have any outgoing teleport requests."));
			return;
		}
		for(TeleportRequest request : requests){
			request.cancel(true);
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
