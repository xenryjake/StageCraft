package com.xenry.stagecraft.skyblock.island.commands.admin;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandAdminCommand extends Command<SkyBlock,IslandManager> {
	
	public IslandAdminCommand(IslandManager manager){
		super(manager, Rank.ADMIN, "islandadmin", "islandsadmin", "isa");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
	
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
	
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
