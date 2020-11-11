package com.xenry.stagecraft.hologram.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.hologram.Hologram;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on July 20, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class HologramLineDeleteCommand extends Command<Core,HologramManager> {

	public HologramLineDeleteCommand(HologramManager manager){
		super(manager, Rank.ADMIN, "linedel", "delline", "remline", "linerem", "removeline", "lineremove");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/hologram " + label + " <name> <line#>"));
			return;
		}
		Hologram hologram = manager.getHologram(args[0]);
		if(hologram == null){
			sender.sendMessage(M.err + "There is no hologram with that name.");
			return;
		}
		int line;
		try{
			line = Integer.parseInt(args[1]);
		}catch(Exception ex){
			sender.sendMessage(M.err + "Invalid line number provided (must be an integer).");
			return;
		}
		if(line < 1 || line > hologram.getLines().size()){
			sender.sendMessage(M.err + "Invalid line number provided (must be between " + M.elm + 1 + M.err + " and " + M.elm + hologram.getLines().size() + M.err + ", inclusive).");
			return;
		}
		hologram.removeLine(line-1);
		manager.save(hologram);
		hologram.spawn();
		sender.sendMessage(M.msg + "Removed line " + M.elm + line + M.msg + " from hologram " + M.elm + hologram.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getAllHologramNames() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getAllHologramNames() : Collections.emptyList();
	}

}
