package com.xenry.stagecraft.bungee.proxy.commands;
import com.xenry.stagecraft.bungee.commands.ProxyCommand;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.CommandSender;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProxyConfigReloadCommand extends ProxyCommand<ProxyManager> {
	
	public ProxyConfigReloadCommand(ProxyManager manager){
		super(manager, "proxyconfigreload", "stagecraft.admin");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		manager.plugin.reloadConfig();
		sender.sendMessage(M.msg("Reloaded proxy configuration."));
	}
	
}
