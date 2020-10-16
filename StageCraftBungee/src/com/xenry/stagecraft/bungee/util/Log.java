package com.xenry.stagecraft.bungee.util;
import com.xenry.stagecraft.bungee.Bungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 1/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class Log {
	
	private Log(){}
	
	public static void info(String message){
		Bungee.getInstance().getLogger().info(message);
	}
	
	public static void warn(String message){
		Bungee.getInstance().getLogger().warning(message);
	}
	
	public static void severe(String message){
		Bungee.getInstance().getLogger().severe(message);
	}
	
	public static void debug(String message){
		if(Bungee.isDebugMode()) {
			Bungee.getInstance().getLogger().info("[DEBUG] " + message);
		}
	}
	
	@Deprecated
	public static void toCS(String message){
		ProxyServer.getInstance().getConsole().sendMessage(message);
	}
	
	public static void toCS(BaseComponent...components) {
		ProxyServer.getInstance().getConsole().sendMessage(components);
	}
	
}
