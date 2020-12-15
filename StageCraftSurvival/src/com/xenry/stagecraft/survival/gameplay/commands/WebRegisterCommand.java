package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/30/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WebRegisterCommand extends PlayerCommand<Survival,GameplayManager> {
	
	public WebRegisterCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "webregister", "webreg");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		player.chat("/dynmap webregister");
		player.sendMessage(M.msg + "Please go to " + M.elm + M.BOLD + "http://mc.xenry.com/map" + M.msg
				+ " to register with the online map.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
