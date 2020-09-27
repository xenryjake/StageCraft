package com.xenry.stagecraft.survival.builder.commands;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderAdminEnableDisableCommand extends Command<Survival,BuilderManager> {
	
	public BuilderAdminEnableDisableCommand(BuilderManager manager){
		super(manager, Rank.MOD, "enable", "disable", "on", "off");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		boolean state;
		if(label.equalsIgnoreCase("enable") || label.equalsIgnoreCase("on")){
			if(manager.isBuilderModeEnabled()){
				sender.sendMessage(M.error("Builder mode is already enabled."));
				return;
			}
			manager.setBuilderModeEnabled(true);
			state = true;
		}else if(label.equalsIgnoreCase("disable") || label.equalsIgnoreCase("off")){
			if(!manager.isBuilderModeEnabled()){
				sender.sendMessage(M.error("Builder mode is already disabled."));
				return;
			}
			manager.setBuilderModeEnabled(false);
			state = false;
		}else{
			sender.sendMessage(M.error("Invalid mode."));
			return;
		}
		sender.sendMessage(M.msg + "Builder mode has been " + (state ? "§aenabled" : "§cdisabled") + M.msg + ".");
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
