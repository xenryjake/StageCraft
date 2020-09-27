package com.xenry.stagecraft.survival.builder.commands;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuildArea;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderAdminNBICommand extends Command<Survival,BuilderManager> {
	
	public BuilderAdminNBICommand(BuilderManager manager){
		super(manager, Rank.ADMIN, "nbi");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/ba " + label + " <area-name> [enable|disable]"));
			return;
		}
		BuildArea area = manager.getBuildArea(args[0]);
		if(area == null){
			sender.sendMessage(M.error("Build Area not found: " + args[0]));
			return;
		}
		if(args.length < 2){
			sender.sendMessage(M.msg + "Non-builder interaction is currently " + (area.allowsNonBuilderInteraction() ? "§aenabled" : "§cdisabled") + M.msg + " in " + M.elm + area.getName() + M.msg + ".");
			return;
		}
		boolean state;
		String arg = args[1].toLowerCase();
		if(arg.startsWith("on") || arg.startsWith("e")){
			state = true;
		}else if(arg.startsWith("off") || arg.startsWith("d")){
			state = false;
		}else if(arg.startsWith("t")){
			state = !area.allowsNonBuilderInteraction();
		}else{
			sender.sendMessage(M.error("Invalid state specified."));
			return;
		}
		area.setAllowNonBuilderInteraction(state);
		sender.sendMessage(M.msg + "Non-builder interaction has been " + (state ? "§aenabled" : "§cdisabled")
				+ M.msg + " in " + M.elm + area.getName() + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return manager.getBuildAreaNames();
			case 2:
				return Arrays.asList("enable", "disable");
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return manager.getBuildAreaNames();
			case 2:
				return Arrays.asList("enable", "disable");
			default:
				return Collections.emptyList();
		}
	}
	
}
