package com.xenry.stagecraft;
import org.bukkit.event.Listener;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public abstract class Handler<P extends StageCraftPlugin, M extends Manager<P>> implements Listener {
	
	protected final M manager;
	
	public Handler(M manager){
		this.manager = manager;
	}
	
	public M getManager() {
		return manager;
	}
	
	public Core getCore(){
		return manager.getCore();
	}
	
	public P getPlugin(){
		return manager.plugin;
	}
	
}
