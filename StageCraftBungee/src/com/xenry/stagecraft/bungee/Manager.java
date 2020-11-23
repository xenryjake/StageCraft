package com.xenry.stagecraft.bungee;
import com.xenry.stagecraft.bungee.commands.ProxyCommand;
import net.md_5.bungee.api.plugin.Listener;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/5/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("EmptyMethod")
public abstract class Manager implements Listener {
	
	public final String name;
	public final Bungee plugin;
	
	private final boolean registerAsListener;
	private boolean enabled = false;
	
	public Manager(String name, Bungee plugin, boolean registerAsListener){
		this.name = name;
		this.plugin = plugin;
		this.registerAsListener = registerAsListener;
	}
	
	public Manager(String name, Bungee plugin){
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
		enabled = false;
	}
	
	protected void onEnable(){}
	
	protected void onPostEnable(){}
	
	protected void onDisable(){}
	
	protected void registerListener(Listener listener){
		plugin.getProxy().getPluginManager().registerListener(plugin, listener);
	}
	
	protected void registerCommand(ProxyCommand<?> command){
		plugin.getProxy().getPluginManager().registerCommand(plugin, command);
	}
	
	public final boolean isEnabled() {
		return enabled;
	}
	
}
