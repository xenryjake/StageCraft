package com.xenry.stagecraft.bungee.proxy.commands;

import com.xenry.stagecraft.bungee.Bungee;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import com.xenry.stagecraft.bungee.util.Log;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.Collections;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProxyDebugModeCommand extends ProxyAdminCommand<ProxyManager> {
	
	public ProxyDebugModeCommand(ProxyManager manager){
		super(manager, "proxydebugmode", "proxydebug");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		boolean enabled = Bungee.betaFeaturesEnabled();
		if(args.length < 1){
			sender.sendMessage(M.msg("Proxy debug mode is currently " + M.enabledDisabled(enabled) + M.msg + "."));
			sender.sendMessage(M.usage("/proxydebugmode <on|off>"));
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
		sender.sendMessage(M.msg("Proxy debug mode is now " + M.enabledDisabled(enabled) + M.msg + "."));
		for(ProxiedPlayer player : manager.plugin.getProxy().getPlayers()){
			if(hasPermission(player) && player != sender){
				player.sendMessage(M.msg(M.elm + sender.getName() + M.enabledDisabled(enabled)
						+ M.msg + " proxy debug mode"));
			}
		}
		Log.info(M.elm + sender.getName() + M.enabledDisabled(enabled) + M.msg + " proxy debug mode.");
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		return args.length == 1 ? Arrays.asList("on", "off") : Collections.emptyList();
	}
	
}
