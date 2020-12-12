package com.xenry.stagecraft.hologram.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on July 16, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class HologramCommand extends Command<Core,HologramManager> {

	public HologramCommand(HologramManager manager){
		super(manager, Rank.HEAD_MOD, "hologram", "holo");
		addSubCommand(new HologramCreateCommand(manager));
		addSubCommand(new HologramMoveCommand(manager));
		addSubCommand(new HologramLineAddCommand(manager));
		addSubCommand(new HologramLineDeleteCommand(manager));
		addSubCommand(new HologramLineEditCommand(manager));
		addSubCommand(new HologramLineDeleteCommand(manager));
		addSubCommand(new HologramDeleteCommand(manager));
		addSubCommand(new HologramUpdateCommand(manager));
		addSubCommand(new HologramListCommand(manager));
		addSubCommand(new HologramInfoCommand(manager));
		addSubCommand(new HologramDeSpawnCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Hologram Commands:");
		sender.sendMessage(M.help(label + " create", "Create a hologram at your current location."));
		sender.sendMessage(M.help(label + " move", "Move a hologram to your location."));
		sender.sendMessage(M.help(label + " center", "Move a hologram to the center of it's current block."));
		sender.sendMessage(M.help(label + " lineadd", "Add a line of text to a hologram."));
		sender.sendMessage(M.help(label + " linedel", "Delete a line of text from a hologram."));
		sender.sendMessage(M.help(label + " lineedit", "Edit a line of text in a hologram."));
		sender.sendMessage(M.help(label + " delete", "Delete a hologram."));
		sender.sendMessage(M.help(label + " update", "Update holograms from the database."));
		sender.sendMessage(M.help(label + " list", "List holograms on this server type."));
		sender.sendMessage(M.help(label + " info", "View information about a hologram."));
		sender.sendMessage(M.help(label + " despawn", "Despawn holograms from a world."));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("create", "move", "center", "lineadd", "linedel", "lineedit", "delete", "update", "list", "info"),args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("create", "move", "center", "lineadd", "linedel", "lineedit", "delete", "update", "list", "info"),args[0]) : Collections.emptyList();
	}

}
