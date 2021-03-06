package com.xenry.stagecraft.survival.gameplay.rules;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.profile.SurvivalProfile;
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
public final class RulesDenyCommand extends PlayerCommand<Survival,GameplayManager> {
	
	public RulesDenyCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "deny", "reject", "no", "decline");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		SurvivalProfile sp = manager.plugin.getSurvivalProfileManager().getProfileByUUID(profile.getUUID());
		if(sp == null){
			throw new NullPointerException("No SurvivalProfile exists for " + profile.getLatestUsername());
		}
		sp.setHasAcceptedRules(false);
		profile.getPlayer().kickPlayer(M.err + "You cannot play on " + M.SERVER_NAME_RAW + " if you do not accept the rules.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
