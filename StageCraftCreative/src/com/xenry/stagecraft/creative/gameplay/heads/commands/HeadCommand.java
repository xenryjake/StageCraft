package com.xenry.stagecraft.creative.gameplay.heads.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HeadCommand extends PlayerCommand<Creative,GameplayManager> {
	
	public HeadCommand(GameplayManager manager) {
		super(manager, Rank.MEMBER, "head", "skull", "heads", "skulls");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		//todo open ui
		profile.sendMessage(M.msg + "There are " + manager.getHeadHandler().getAllHeads().size() + " heads.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
