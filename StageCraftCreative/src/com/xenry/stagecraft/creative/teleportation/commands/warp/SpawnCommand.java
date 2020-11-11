package com.xenry.stagecraft.creative.teleportation.commands.warp;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.creative.teleportation.Warp;
import com.xenry.stagecraft.creative.teleportation.commands.TPCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
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
public final class SpawnCommand extends Command<Creative,TeleportationManager> {
	
	public SpawnCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "spawn", "spawno");
		setCanBeDisabled(true);
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
		if(args.length >= 1 && canDoOther){
			serverPerform(profile.getPlayer(), args, label);
			return;
		}
		Warp spawn = manager.getWarpHandler().getSpawn();
		if(spawn == null){
			profile.sendMessage(M.error("The spawn is not set."));
			return;
		}
		manager.createAndExecuteTeleportation(profile.getPlayer(), profile.getPlayer(), profile.getPlayer().getLocation(), spawn.getLocation(), safe ? Teleportation.Type.WARP : Teleportation.Type.ADMIN, safe);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/spawn <player>"));
			return;
		}
		Warp spawn = manager.getWarpHandler().getSpawn();
		if(spawn == null){
			sender.sendMessage(M.error("The spawn is not set."));
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		manager.createAndExecuteTeleportation(player, sender, player.getLocation(), spawn.getLocation(), Teleportation.Type.ADMIN, !label.startsWith("o"));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 && TPCommand.OTHER_RANK.has(profile) ? allLocalPlayers() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? allLocalPlayers() : Collections.emptyList();
	}
	
}
