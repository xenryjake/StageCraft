package com.xenry.stagecraft.creative.gameplay.rules;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.creative.profile.CreativeProfile;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
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
public final class RulesAcceptCommand extends Command<Creative,GameplayManager> {
	
	public RulesAcceptCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "accept");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		CreativeProfile cp = manager.plugin.getCreativeProfileManager().getProfileByUUID(profile.getUUID());
		if(cp == null){
			throw new NullPointerException("No CreativeProfile exists for " + profile.getLatestUsername());
		}
		if(cp.hasAcceptedRules()){
			profile.sendMessage(M.error("You've already accepted the rules!"));
			return;
		}
		cp.setHasAcceptedRules(true);
		profile.sendMessage(M.msg + "Thanks for accepting the rules! You may now break and place blocks!");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
