package com.xenry.stagecraft;
import com.xenry.stagecraft.commands.Command;
import org.bukkit.event.Listener;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 2/3/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public abstract class Manager<T extends StageCraftPlugin> implements Listener {
	
	public final String name;
	public final T plugin;
	
	private final boolean registerAsListener;
	private boolean enabled = false;
	
	public Manager(String name, T plugin, boolean registerAsListener){
		this.name = name;
		this.plugin = plugin;
		this.registerAsListener = registerAsListener;
	}
	
	public Manager(String name, T plugin){
		this(name, plugin, true);
	}
	
	public final void enable(){
		if(enabled){
			return;
		}
		this.onEnable();
		enabled = true;
	}
	
	public final void postEnable(){
		if(!enabled){
			return;
		}
		if(registerAsListener){
			registerListener(this);
		}
		this.onPostEnable();
	}
	
	public final void disable(){
		if(!enabled){
			return;
		}
		this.onDisable();
	}
	
	protected void onEnable(){}
	
	protected void onPostEnable(){}
	
	protected void onDisable(){}
	
	protected void registerListener(Listener listener){
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	protected void registerCommand(Command<?,?> command){
		plugin.getCore().getCommandManager().register(command);
	}
	
	public final boolean isEnabled() {
		return enabled;
	}
	
	public Core getCore(){
		return plugin.getCore();
	}
	
}
