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
public final class RulesAcceptCommand extends PlayerCommand<Survival,GameplayManager> {
	
	public RulesAcceptCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "accept");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		SurvivalProfile sp = manager.plugin.getSurvivalProfileManager().getProfileByUUID(profile.getUUID());
		if(sp == null){
			throw new NullPointerException("No SurvivalProfile exists for " + profile.getLatestUsername());
		}
		if(sp.hasAcceptedRules()){
			profile.sendMessage(M.error("You've already accepted the rules!"));
			return;
		}
		sp.setHasAcceptedRules(true);
		profile.sendMessage(M.msg + "Thanks for accepting the rules! You may now break and place blocks!");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
