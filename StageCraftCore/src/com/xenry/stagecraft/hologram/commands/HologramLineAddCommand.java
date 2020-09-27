package com.xenry.stagecraft.hologram.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.hologram.Hologram;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on July 20, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class HologramLineAddCommand extends Command<Core,HologramManager> {

	public HologramLineAddCommand(HologramManager manager){
		super(manager, Rank.ADMIN, "lineadd", "addline", "newline", "linenew");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/hologram " + label + " <name> <text>"));
			return;
		}
		Hologram hologram = manager.getHologram(args[0]);
		if(hologram == null){
			sender.sendMessage(M.err + "There is no hologram with that name.");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++){
			sb.append(args[i]).append(" ");
		}
		String text = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
		hologram.addLine(text);
		manager.save(hologram);
		hologram.spawn();
		sender.sendMessage(M.msg + "Added line \"§r" + text + M.msg + "\" to hologram " + M.elm + hologram.getName() + M.msg + ".");
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
