package com.xenry.stagecraft.survival.gameplay.rules;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.profile.SurvivalProfile;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RulesDenyCommand extends Command<Survival,GameplayManager> {
	
	public RulesDenyCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "deny", "reject", "no", "decline");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		SurvivalProfile sp = manager.plugin.getSurvivalProfileManager().getProfileByUUID(profile.getUUID());
		if(sp == null){
			throw new NullPointerException("No SurvivalProfile exists for " + profile.getLatestUsername());
		}
		sp.setHasAcceptedRules(false);
		profile.getPlayer().kickPlayer(M.err + "You cannot play on StageCraft if you do not accept the rules.");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
