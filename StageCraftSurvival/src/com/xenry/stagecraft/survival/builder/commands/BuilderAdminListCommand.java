package com.xenry.stagecraft.survival.builder.commands;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuildArea;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderAdminListCommand extends Command<Survival,BuilderManager> {
	
	public BuilderAdminListCommand(BuilderManager manager){
		super(manager, Rank.ADMIN, "list");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "List of Build Areas:");
		for(BuildArea area : manager.getBuildAreas()){
			sender.sendMessage(M.elm + area.getName() + "§6 (" + area.getAX() + "," + area.getAZ() + ") -> ("
					+ area.getBX() + "," + area.getBZ() + ") [" + area.getWorld() + "] " + M.gry + "nbi: "
					+ (area.allowsNonBuilderInteraction() ? "§aenabled" : "§cdisabled"));
		}
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
