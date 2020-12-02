package com.xenry.stagecraft.hologram.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
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
 * StageCraft Created by Henry Jake (Xenry) on 5/14/17
 * The content in this file and all related files are
 * Copyright (C) 2017 Henry Jake.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class HologramInfoCommand extends Command<Core,HologramManager> {
	
	public HologramInfoCommand(HologramManager manager){
		super(manager, Rank.HEAD_MOD, "info", "i");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/hologram " + label));
			return;
		}
		Hologram holo = manager.getHologram(args[0]);
		if(holo == null){
			sender.sendMessage(M.error("There is no hologram with the name " + M.elm + args[0] + M.err + "."));
			return;
		}
		sender.sendMessage(M.msg + "Info about " + M.elm + holo.getName() + M.msg + ":");
		for(String line : holo.getLines()){
			sender.sendMessage(M.arrow(M.WHITE + line));
		}
		sender.sendMessage(M.arrow(M.msg + "World: " + M.elm + holo.getWorldName()));
		sender.sendMessage(M.arrow(M.msg + "X: " + M.elm + holo.getLocationArray()[0]));
		sender.sendMessage(M.arrow(M.msg + "Y: " + M.elm + holo.getLocationArray()[1]));
		sender.sendMessage(M.arrow(M.msg + "Z: " + M.elm + holo.getLocationArray()[2]));
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
