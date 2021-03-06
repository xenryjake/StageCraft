package com.xenry.stagecraft.creative.teleportation.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BackCommand extends PlayerCommand<Creative,TeleportationManager> {
	
	public static final Access ACCESS = Rank.MEMBER;
	public static final Access NOTIFY_ON_DEATH = ACCESS;
	
	public BackCommand(TeleportationManager manager){
		super(manager, ACCESS, "back", "b", "backo", "bo");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		Location location = manager.getLastLocation(player);
		if(location == null){
			player.sendMessage(M.error("You have no location to go back to."));
			return;
		}
		
		boolean safe = true;
		if(label.endsWith("o")){
			if(!TPCommand.SELF_RANK.has(profile)){
				noPermission(profile);
				return;
			}
			safe = false;
		}
		
		manager.createAndExecuteTeleportation(player, player, player.getLocation(), location, safe ? Teleportation.Type.BACK : Teleportation.Type.ADMIN, safe);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
