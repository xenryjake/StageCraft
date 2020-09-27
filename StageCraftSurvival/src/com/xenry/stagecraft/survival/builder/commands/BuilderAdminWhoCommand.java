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
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderAdminWhoCommand extends Command<Survival,BuilderManager> {
	
	public BuilderAdminWhoCommand(BuilderManager manager){
		super(manager, Rank.MOD, "who");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		StringBuilder sb = new StringBuilder();
		sb.append(M.msg).append("Players in builder mode: ");
		String message;
		if(manager.getBuilders().size() == 0){
			sb.append("§cnone");
			message = sb.toString();
		}else{
			for(String name : manager.getBuilders()){
				sb.append(M.elm).append(name).append(M.gry).append(", ");
			}
			message = sb.toString();
			message = message.substring(0, message.length() - 2);
		}
		sender.sendMessage(message);
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
