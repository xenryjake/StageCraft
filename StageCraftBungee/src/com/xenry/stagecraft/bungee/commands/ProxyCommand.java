package com.xenry.stagecraft.bungee.commands;
import com.xenry.stagecraft.bungee.Manager;
import com.xenry.stagecraft.bungee.util.M;
import com.xenry.stagecraft.bungee.util.PlayerUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class ProxyCommand<T extends Manager> extends Command implements TabExecutor {
	
	protected final T manager;
	
	public ProxyCommand(T manager, String label, String permission, String...aliases){
		super(label, permission, aliases);
		this.manager = manager;
	}
	
	protected final void onlyForPlayers(CommandSender sender){
		sender.sendMessage(M.error("This command is only available for players."));
	}
	
	protected final void notForPlayers(ProxiedPlayer player){
		player.sendMessage(M.error("This command is not available to players."));
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return Collections.emptyList();
	}
	
	protected final List<String> allNetworkPlayers(){
		return PlayerUtil.getOnlinePlayerNames();
	}
	
}
