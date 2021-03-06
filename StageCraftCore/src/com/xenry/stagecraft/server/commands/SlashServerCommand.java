package com.xenry.stagecraft.server.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.BungeeUtil;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SlashServerCommand extends PlayerCommand<Core,ServerManager> {
	
	public SlashServerCommand(ServerManager manager){
		super(manager, Rank.MEMBER, "survival", "sv", "creative", "cr", "fun");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		String serverName;
		switch(label){
			case "survival":
			case "sv":
				serverName = "survival";
				break;
			case "creative":
			case "cr":
				serverName = "creative";
				break;
			case "fun":
				serverName = "fun";
				break;
			default:
				profile.sendMessage(M.error("Invalid server."));
				return;
		}
		for(String server : manager.getServerNames()){
			if(server.equalsIgnoreCase(serverName)){
				profile.sendMessage(M.msg + "Sending you to " + M.elm + server + M.msg + "...");
				manager.plugin.getProfileManager().save(profile);
				manager.plugin.getServer().getScheduler().runTaskLater(manager.plugin,
						() -> BungeeUtil.connect(profile.getPlayer(), server), 20L);
				return;
			}
		}
		profile.sendMessage(M.error("Server not found: " + label));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
