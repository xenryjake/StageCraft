package com.xenry.stagecraft.skyblock.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.teleportation.TeleportRequest;
import com.xenry.stagecraft.skyblock.teleportation.TeleportationManager;
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
public final class TPDenyCommand extends PlayerCommand<SkyBlock,TeleportationManager> {
	
	public TPDenyCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "tpdeny", "tpno");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		TeleportRequest request = manager.getRequestTo(profile.getPlayer());
		if(request == null){
			profile.sendMessage(M.error("You don't have any incoming teleport requests."));
			return;
		}
		request.deny();
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
