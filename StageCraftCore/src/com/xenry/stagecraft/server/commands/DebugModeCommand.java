package com.xenry.stagecraft.server.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DebugModeCommand extends Command<Core,ServerManager> {
	
	public DebugModeCommand(ServerManager manager){
		super(manager, Rank.ADMIN, "scdebug", "debugmode");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		boolean enabled = Core.isDebugMode();
		if(args.length < 1){
			sender.sendMessage(M.msg + "Debug mode is currently " + (enabled ? "§aenabled" : "§cdisabled") + M.msg + ".");
			sender.sendMessage(M.usage("/" + label + " <on|off>"));
			return;
		}
		args[0] = args[0].toLowerCase();
		if(args[0].startsWith("on") || args[0].startsWith("en")){
			enabled = true;
		}else if(args[0].startsWith("off") || args[0].startsWith("dis")){
			enabled = false;
		}else if(args[0].startsWith("t")){
			enabled = !enabled;
		}else{
			sender.sendMessage(M.error("Invalid argument."));
			return;
		}
		manager.plugin.setDebugMode(enabled);
		sender.sendMessage(M.msg + "Debug mode is now " + (enabled ? "§aenabled" : "§cdisabled") + M.msg + ".");
		for(Player player : Bukkit.getOnlinePlayers()){
			if(access.has(player)){
				player.sendMessage(M.elm + sender.getName() + (enabled ? " §aenabled" : " §cdisabled")
						+ M.msg + " debug mode");
			}
		}
		Log.toCS(M.elm + sender.getName() + (enabled ? " §aenabled" : " §cdisabled") + M.msg + " debug mode.");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("on", "off", "toggle") : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("on", "off", "toggle") : Collections.emptyList();
	}
	
}
