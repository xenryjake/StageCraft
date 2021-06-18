package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
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
public final class PlaytimeCommand extends PlayerCommand<Core,ProfileManager> {
	
	public PlaytimeCommand(ProfileManager manager){
		super(manager, Rank.MEMBER, "playtime");
	}
	
	@Override
	protected void playerPerform(@NotNull Profile sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Your playtime:");
		sender.sendMessage(M.arrow("Total: " + M.WHITE + TimeUtil.simplerString(sender.getTotalPlaytime())));
		sender.sendMessage(M.arrow(manager.plugin.getServerName() + ": " + M.WHITE + TimeUtil.simplerString(sender.getPlaytime(manager.plugin.getServerName()))));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
