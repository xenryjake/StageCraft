package com.xenry.stagecraft.hologram.commands;

import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.hologram.Hologram;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on July 20, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class HologramLineEditCommand extends Command<Core,HologramManager> {

	public HologramLineEditCommand(HologramManager manager){
		super(manager, Rank.HEAD_MOD, "lineedit", "editline", "linedit", "linemod", "modline");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 3){
			sender.sendMessage(M.usage("/hologram " + label + " <name> <line#> <text>"));
			return;
		}
		Hologram hologram = manager.getHologram(args[0]);
		if(hologram == null){
			sender.sendMessage(M.error("There is no hologram with that name."));
			return;
		}
		int line;
		try{
			line = Integer.parseInt(args[1]);
		}catch(Exception ex){
			sender.sendMessage(M.error("Invalid line number provided (must be an integer)."));
			return;
		}
		if(line < 1 || line > hologram.getLines().size()){
			sender.sendMessage(M.error("Invalid line number provided (must be between " + M.elm + 1 + M.err + " and " + M.elm + hologram.getLines().size() + M.err + ", inclusive)."));
			return;
		}
		String text = ChatColor.translateAlternateColorCodes('&', Joiner.on(' ').join(Arrays.copyOfRange(args, 2, args.length)));
		hologram.editLine(line-1, text);
		manager.save(hologram);
		hologram.spawn();
		sender.sendMessage(M.msg + "Modified line " + M.elm + line + M.msg + " in hologram " + M.elm + hologram.getName() + M.msg + " to text \"§r" + text + M.msg + "\".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getHologramNames(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getHologramNames(args[0]) : Collections.emptyList();
	}

}
