package com.xenry.stagecraft.bungee;
import net.md_5.bungee.api.plugin.Listener;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/5/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class Handler<T extends Manager> implements Listener {

	protected final T manager;
	
	public Handler(T manager){
		this.manager = manager;
	}
	
	public T getManager() {
		return manager;
	}
	
	public Bungee getPlugin(){
		return manager.plugin;
	}
	
}
