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
 * StageCraft created by Henry Blasingame (Xenry) on 7/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RulesCommand extends PlayerCommand<SkyBlock,GameplayManager> {
	
	public RulesCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "rules", "rule");
		addSubCommand(new RulesAcceptCommand(manager));
		addSubCommand(new RulesDenyCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		SkyBlockProfile sp = manager.plugin.getSkyBlockProfileManager().getProfileByUUID(sender.getUUID());
		if(sp != null && !sp.hasAcceptedRules()){
			sender.sendMessage(M.elm + M.BOLD + "You must accept the rules before playing.");
		}
		sender.sendMessage(M.msg + "View the §a§lStage§9§lCraft" + M.msg + " server rules at " + M.elm + M.BOLD + "http://mc.xenry.com/rules");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
