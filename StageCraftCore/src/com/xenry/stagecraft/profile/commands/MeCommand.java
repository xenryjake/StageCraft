package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.command.CommandSender;
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
public final class MeCommand extends Command<Core,ProfileManager> {
	
	public MeCommand(ProfileManager manager){
		super(manager, Rank.MEMBER, "me", "myprofile");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.sendMessage(M.msg + "Profile of " + M.elm + profile.getLatestUsername() + M.msg + ":");
		profile.sendMessage(M.arrow("UUID: " + M.WHITE + profile.getUUID()));
		profile.sendMessage(M.arrow("Rank: " + profile.getRank().getColoredName()));
		profile.sendMessage(M.arrow("Total Playtime: " + M.WHITE + TimeUtil.simplerString(profile.getTotalPlaytime())));
		profile.sendMessage(M.arrow("Status: " + (profile.isOnline() ? "§aOnline " + M.msg + "(" + M.elm + TimeUtil.simplerString(profile.getSecondsSinceLastLogin(manager.plugin.getServerName())) + M.msg + ")" : "§cOffline " + M.msg + "(" + M.elm + TimeUtil.simplerString(profile.getSecondsSinceLastLogout(manager.plugin.getServerName())) + M.msg + ")")));
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
