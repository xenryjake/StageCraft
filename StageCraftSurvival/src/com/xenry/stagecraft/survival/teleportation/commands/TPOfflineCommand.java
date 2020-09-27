package com.xenry.stagecraft.survival.teleportation.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPOfflineCommand extends Command<Survival,TeleportationManager> {
	
	public TPOfflineCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "tpoffline");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Profile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = getCore().getProfileManager().getProfileByLatestUsername(args[0]);
				/*try {
					target = manager.plugin.getProfileManager().getProfileByUUID(UUIDFetcher.getUUIDOf(args[0]).toString());
				} catch(Exception ex) {
					sender.sendMessage(M.error("That username is invalid."));
					return;
				}*/
			}
		}else{
			target = getCore().getProfileManager().getProfileByUUID(args[0]);
		}
		if(target == null){
			profile.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		if(target.isOnline()){
			profile.sendMessage(M.error("That player is online!"));
			return;
		}
		Vector3D coords = target.getLastLocation();
		if(coords.y < 1){
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
		manager.createAndExecuteTeleportation(player, player, player.getLocation(), new Location(world, coords.x, coords.y, coords.z), isAdmin ? Teleportation.Type.ADMIN : Teleportation.Type.WARP, !isAdmin);
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