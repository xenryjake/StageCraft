package com.xenry.stagecraft.bungee.util;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerUtil {
	
	private PlayerUtil(){}
	
	public static List<String> getOnlinePlayerNames(){
		List<String> names = new ArrayList<>();
		for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
			names.add(player.getName());
		}
		return names;
	}
	
}
