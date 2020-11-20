package com.xenry.stagecraft.hologram.commands;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
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
public final class HologramUpdateCommand extends Command<Core,HologramManager> {
	
	public HologramUpdateCommand(HologramManager manager){
		super(manager, Rank.HEAD_MOD, "update", "download", "reload", "refresh");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Downloading holograms...");
		manager.updateAll();
		sender.sendMessage(M.msg + "Updated holograms from database.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
