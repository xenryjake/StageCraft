package com.xenry.stagecraft.util;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WorldUtil {
	
	private WorldUtil(){}
	
	public static String getDefaultLevelName(){
		return (((CraftServer)Bukkit.getServer()).getServer()).propertyManager.getProperties().levelName;
	}
	
}
