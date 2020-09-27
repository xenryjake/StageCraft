package com.xenry.stagecraft;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class StageCraftPlugin extends JavaPlugin {
	
	public final String name;
	protected Core core;
	
	public StageCraftPlugin(String name, Core core){
		this.name = name;
		this.core = core;
	}
	
	public final boolean isCore(){
		return this instanceof Core;
	}
	
	public Core getCore() {
		return core;
	}
	
}
