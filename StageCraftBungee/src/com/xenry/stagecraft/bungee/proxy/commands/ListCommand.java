package com.xenry.stagecraft.bungee.proxy.commands;
import com.xenry.stagecraft.bungee.commands.ProxyCommand;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import net.md_5.bungee.api.CommandSender;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ListCommand extends ProxyCommand<ProxyManager> {
	
	public ListCommand(ProxyManager manager){
		super(manager, "list", null, "who", "online", "ls");
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] strings) {
	
	}
	
}
