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
 * StageCraft Created by Henry Jake (Xenry) on 5/14/17
 * The content in this file and all related files are
 * Copyright (C) 2017 Henry Jake.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class HologramListCommand extends Command<Core,HologramManager> {
	
	public HologramListCommand(HologramManager manager){
		super(manager, Rank.ADMIN, "list");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "List of holograms on " + M.elm + manager.plugin.getServerName() + M.msg + ":");
		StringBuilder sb = new StringBuilder();
		for(Hologram hologram : manager.getHolograms()){
			sb.append(M.elm).append(hologram.getName()).append(M.msg).append(", ");
		}
		sender.sendMessage(M.arrow(sb.toString().trim()));
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
