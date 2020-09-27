package com.xenry.stagecraft.server.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/20/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class MOTDSetCommand extends Command<Core,ServerManager> {
	
	public MOTDSetCommand(ServerManager manager){
		super(manager, Rank.ADMIN, "set");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/motd " + label + " <new-motd>"));
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(String arg : args){
			sb.append(arg).append(" ");
		}
		String newMOTD = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
		manager.getSettingsHandler().setMOTD(newMOTD);
		sender.sendMessage(M.msg + "You set the MOTD to: §e" + newMOTD);
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
