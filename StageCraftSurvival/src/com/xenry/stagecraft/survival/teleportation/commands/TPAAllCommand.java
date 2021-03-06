package com.xenry.stagecraft.survival.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPAAllCommand extends PlayerCommand<Survival,TeleportationManager> {
	
	public TPAAllCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "tpaall");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player from = profile.getPlayer();
		for(Player to : Bukkit.getOnlinePlayers()){
			if(from == to){
				continue;
			}
			manager.createAndSendRequest(from, to, true);
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
