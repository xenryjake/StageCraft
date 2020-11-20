package com.xenry.stagecraft.creative.teleportation.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WorldCommand extends Command<Creative,TeleportationManager> {
	
	public WorldCommand(TeleportationManager manager){
		super(manager, Rank.HEAD_MOD, "world");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <world>"));
			return;
		}
		World world = Bukkit.getWorld(args[0]);
		if(world == null){
			sender.sendMessage(M.error("Invalid world: " + args[0]));
			return;
		}
		Player player = sender.getPlayer();
		manager.createAndExecuteTeleportation(player, player, player.getLocation(), world.getSpawnLocation(), Teleportation.Type.ADMIN, false);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return LocationUtil.getAllWorldNames();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return LocationUtil.getAllWorldNames();
	}
	
}
