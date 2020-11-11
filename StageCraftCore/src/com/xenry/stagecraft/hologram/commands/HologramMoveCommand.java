package com.xenry.stagecraft.hologram.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.hologram.Hologram;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on July 19, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class HologramMoveCommand extends Command<Core,HologramManager> {

	public HologramMoveCommand(HologramManager manager){
		super(manager, Rank.ADMIN, "move", "tphere", "movehere", "center");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/hologram " + label + " <name>"));
			return;
		}
		Hologram hologram = manager.getHologram(args[0]);
		if(hologram == null){
			profile.sendMessage(M.err + "There is no hologram with that name.");
			return;
		}
		Location location = label.equalsIgnoreCase("center") ? hologram.getLocation() : profile.getPlayer().getLocation();
		if(label.equalsIgnoreCase("center")){
			location.setX(location.getBlockX() + 0.5);
			location.setZ(location.getBlockZ() + 0.5);
		}
		hologram.setLocation(location);
		manager.save(hologram);
		manager.update(hologram);
		profile.sendMessage(M.msg + "Moved hologram " + M.elm + hologram.getName() + M.msg + " to x" + M.elm + location.getX() + M.msg + ", z" + M.elm + location.getZ() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getAllHologramNames() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}

}
