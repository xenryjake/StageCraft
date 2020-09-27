package com.xenry.stagecraft.hologram.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.hologram.Hologram;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on July 26, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class HologramDeleteCommand extends Command<Core,HologramManager> {

	public HologramDeleteCommand(HologramManager manager){
		super(manager, Rank.ADMIN, "delete", "remove", "del", "rem");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/hologram " + label + " <name>"));
			return;
		}
		Hologram hologram = manager.getHologram(args[0]);
		if(hologram == null){
			sender.sendMessage(M.err + "There is no hologram with that name.");
			return;
		}
		manager.delete(hologram);
		sender.sendMessage(M.msg + "You have deleted hologram " + M.elm + hologram.getName() + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? manager.getAllHologramNames() : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? manager.getAllHologramNames() : Collections.emptyList();
	}

}
