package com.xenry.stagecraft.creative.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.TeleportMenu;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPHereCommand extends PlayerCommand<Creative,TeleportationManager> {
	
	public TPHereCommand(TeleportationManager manager){
		super(manager, TPCommand.OTHER_RANK, "tphere", "tpohere");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			if(Bukkit.getOnlinePlayers().size() <= 1){
				profile.sendMessage(M.usage("/" + label + " [player] <player-to> "));
			}else{
				new TeleportMenu(manager, profile.getUUID(), label).open(profile.getPlayer());
			}
			return;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			profile.sendMessage(M.error("Player not found: " + args[0]));
			return;
		}
		doTeleport(profile.getPlayer(), target, profile.getPlayer(), !label.equalsIgnoreCase("tpohere"));
	}
	
	private void doTeleport(CommandSender teleporter, Player player, Player to, boolean safe){
		manager.createAndExecuteTeleportation(player, teleporter, player.getLocation(), to.getLocation(), Teleportation.Type.ADMIN, safe);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
