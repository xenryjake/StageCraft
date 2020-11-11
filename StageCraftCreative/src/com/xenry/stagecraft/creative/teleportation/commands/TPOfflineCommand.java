package com.xenry.stagecraft.creative.teleportation.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.profile.CreativeProfile;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.LocationVector;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
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
public final class TPOfflineCommand extends Command<Creative,TeleportationManager> {
	
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
		CreativeProfile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.plugin.getCreativeProfileManager().getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = manager.plugin.getCreativeProfileManager().getProfileByLatestUsername(args[0]);
			}
		}else{
			target = manager.plugin.getCreativeProfileManager().getProfileByUUID(args[0]);
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
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
