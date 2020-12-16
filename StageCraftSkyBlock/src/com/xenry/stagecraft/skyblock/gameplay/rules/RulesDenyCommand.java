package com.xenry.stagecraft.skyblock.gameplay.rules;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.gameplay.GameplayManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RulesDenyCommand extends PlayerCommand<SkyBlock,GameplayManager> {
	
	public RulesDenyCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "deny", "reject", "no", "decline");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		SkyBlockProfile sp = manager.plugin.getSkyBlockProfileManager().getProfileByUUID(profile.getUUID());
		if(sp == null){
			throw new NullPointerException("No SurvivalProfile exists for " + profile.getLatestUsername());
		}
		sp.setHasAcceptedRules(false);
		profile.getPlayer().kickPlayer(M.err + "You cannot play on StageCraft if you do not accept the rules.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
