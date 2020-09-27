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
public final class BuilderAdminRemoveCommand extends Command<Survival,BuilderManager> {
	
	public BuilderAdminRemoveCommand(BuilderManager manager){
		super(manager, Rank.ADMIN, "remove");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/ba " + label + " <area-name>"));
			return;
		}
		BuildArea area = manager.getBuildArea(args[0]);
		if(area == null){
			sender.sendMessage(M.error("That area does not exist."));
			return;
		}
		manager.removeBuildArea(area);
		sender.sendMessage(M.msg + "Removed build area " + M.elm + area.getName() + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? manager.getBuildAreaNames() : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? manager.getBuildAreaNames() : Collections.emptyList();
	}
	
}
