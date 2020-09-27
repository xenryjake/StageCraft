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
 * StageCraft created by Henry Jake on July 19, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public final class HologramCreateCommand extends Command<Core,HologramManager> {

	public HologramCreateCommand(HologramManager manager){
		super(manager, Rank.ADMIN, "create", "new");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 2){
			profile.sendMessage(M.usage("/hologram " + label + " <name> <text>"));
			return;
		}
		String name = args[0];
		if(name.replaceAll("[a-zA-Z0-9_]", "").equals(name)){
			profile.sendMessage(M.error("Hologram names can only contain alphanumeric characters and underscores."));
			return;
		}
		if(name.replaceAll("[0-9_]", "").isEmpty()){
			profile.sendMessage(M.error("Hologram names must contain at least one letter."));
			return;
		}
		Hologram hologram = manager.getHologram(name);
		if(hologram != null){
			profile.sendMessage(M.err + "A hologram with that name already exists!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++){
			sb.append(args[i]).append(" ");
		}
		String text = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
		hologram = new Hologram(name, profile.getPlayer().getLocation(), true);
		hologram.addLine(text);
		manager.addHologram(hologram);
		manager.save(hologram);
		profile.sendMessage(M.msg + "Added hologram with name " + M.elm + name + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}

}