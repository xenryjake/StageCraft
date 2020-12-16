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
public final class RulesAcceptCommand extends PlayerCommand<SkyBlock,GameplayManager> {
	
	public RulesAcceptCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "accept");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		SkyBlockProfile sp = manager.plugin.getSkyBlockProfileManager().getProfileByUUID(profile.getUUID());
		if(sp == null){
			throw new NullPointerException("No SkyBlockProfile exists for " + profile.getLatestUsername());
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
