package com.xenry.stagecraft.fun.teleportation.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.fun.Fun;
import com.xenry.stagecraft.fun.teleportation.Teleportation;
import com.xenry.stagecraft.fun.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SpawnCommand extends Command<Fun,TeleportationManager> {
	
	public SpawnCommand(TeleportationManager manager){
		super(manager, Rank.ADMIN, "spawn", "spawno");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		boolean safe = true;
		if(label.endsWith("o")){
			if(!TPCommand.SELF_RANK.has(profile)){
				noPermission(profile);
				return;
			}
			safe = false;
		}
		boolean canDoOther = TPCommand.OTHER_RANK.has(profile);
		if(args.length >= 1 && canDoOther) {
			serverPerform(profile.getPlayer(), args, label);
			return;
		}
		Location location = profile.getPlayer().getWorld().getSpawnLocation();
		manager.createAndExecuteTeleportation(profile.getPlayer(), profile.getPlayer(), profile.getPlayer().getLocation(), location, safe ? Teleportation.Type.WARP : Teleportation.Type.ADMIN, safe);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/spawn <player>"));
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		Location location = player.getWorld().getSpawnLocation();
		manager.createAndExecuteTeleportation(player, sender, player.getLocation(), location, Teleportation.Type.ADMIN, !label.startsWith("o"));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 && TPCommand.OTHER_RANK.has(profile) ? localPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
