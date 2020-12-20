package com.xenry.stagecraft.skyblock.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
import com.xenry.stagecraft.skyblock.teleportation.Teleportation;
import com.xenry.stagecraft.skyblock.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.LocationVector;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
public final class TPOfflineCommand extends PlayerCommand<SkyBlock,TeleportationManager> {
	
	public TPOfflineCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "tpoffline");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		SkyBlockProfile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.plugin.getSkyBlockProfileManager().getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = manager.plugin.getSkyBlockProfileManager().getProfileByLatestUsername(args[0]);
			}
		}else{
			target = manager.plugin.getSkyBlockProfileManager().getProfileByUUID(args[0]);
		}
		if(target == null){
			profile.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		if(target.isOnline()){
			profile.sendMessage(M.error("That player is online!"));
			return;
		}
		LocationVector vector = target.getLastLocationVector();
		if(vector.y < 0){
			profile.sendMessage(M.error("Invalid last location."));
			return;
		}
		World world = Bukkit.getWorld(target.getLastLocationWorldName());
		if(world == null){
			profile.sendMessage(M.error("The world doesn't exist."));
			return;
		}
		
		Player player = profile.getPlayer();
		boolean isAdmin = TPCommand.SELF_RANK.has(profile);
		manager.createAndExecuteTeleportation(player, player, player.getLocation(), new Location(world, vector.x, vector.y, vector.z, vector.yaw, vector.pitch), isAdmin ? Teleportation.Type.ADMIN : Teleportation.Type.WARP, !isAdmin);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
