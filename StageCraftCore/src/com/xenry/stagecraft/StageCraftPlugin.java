package com.xenry.stagecraft;
import com.xenry.stagecraft.util.Log;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

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
	
	private final Random random;
	private final HashMap<Manager<?>,StageCraftPlugin> managers;
	
	public StageCraftPlugin(String name, Core core){
		this.name = name;
		this.core = core;
		
		managers = new LinkedHashMap<>();
		random = new Random();
	}
	
	public final boolean isCore(){
		return this instanceof Core;
	}
	
	public Core getCore() {
		return core;
	}
	
	protected void onLoadPreManager(){}
	
	protected void loadManagers(){}
	
	protected void onLoadPostManager(){}
	
	protected void onEnablePreManager(){}
	
	protected void onEnablePostManager(){}
	
	protected void onServerEnabled(){}
	
	protected void onDisablePreManager(){}
	
	protected void onDisablePostManager(){}
	
	@Override
	public final void onLoad() {
		Log.info("Loading StageCraftPlugin: " + name + "...");
		onLoadPreManager();
		Log.info("Loading " + name + " managers...");
		loadManagers();
		Log.info(name + " managers loaded.");
		onLoadPostManager();
		Log.info("StageCraftPlugin " + name + " loaded.");
	}
	
	@Override
	public final void onEnable() {
		Log.info("Enabling StageCraftPlugin: " + name + "...");
		onEnablePreManager();
		Log.info("Enabling " + name + " managers...");
		try{
			for(Manager<?> manager : managers.keySet()){
				enable(manager);
			}
			for(Manager<?> manager : managers.keySet()){
				postEnable(manager);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while enabling the managers!");
			return;
		}
		Log.info(name + " managers enabled.");
		onEnablePostManager();
		Log.info("StageCraftPlugin " + name + " enabled.");
		getServer().getScheduler().runTaskLater(this, this::onServerEnabled, 1L);
	}
	
	@Override
	public final void onDisable() {
		Log.info("Disabling StageCraftPlugin: " + name + "...");
		onDisablePreManager();
		Log.info("Disabling " + name + " managers...");
		try{
			ArrayList<Manager<?>> reversed = new ArrayList<>(managers.keySet());
			Collections.reverse(reversed);
			for(Manager<?> manager : reversed){
				disable(manager);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while disabling the managers!");
			return;
		}
		Log.info(name + " managers disabled.");
		onDisablePostManager();
		Log.info("StageCraftPlugin " + name + " disabled.");
	}
	
	protected <M extends Manager<?>> M loadManager(StageCraftPlugin plugin, Class<M> clazz){
		try{
			M manager = clazz.getConstructor(plugin.getClass()).newInstance(plugin);
			managers.put(manager, plugin);
			return manager;
		}catch(Exception ex){
			Log.severe("Failed to load manager \"" + clazz + "\":");
			ex.printStackTrace();
			return null;
		}
	}
	
	private void enable(Manager<?> manager){
		if(manager == null){
			Log.severe("Cannot enable null manager.");
			return;
		}
		try{
			long time = System.currentTimeMillis();
			manager.enable();
			Log.info("Enabled manager: " + manager.name + ". Took " + (System.currentTimeMillis()-time) + "ms");
		}catch(Exception ex){
			Log.severe("Failed to enable manager \"" + manager.name + "\":");
			ex.printStackTrace();
		}
	}
	
	private void postEnable(Manager<?> manager){
		if(manager == null){
			Log.severe("Cannot PostEnable null manager.");
			return;
		}
		if(!manager.isEnabled()){
			Log.warn("Cannot PostEnable disabled manager: " + manager.getClass().getName());
			return;
		}
		try{
			manager.postEnable();
		}catch(Exception ex){
			Log.severe("Failed to PostEnable manager \"" + manager.name + "\":");
			ex.printStackTrace();
		}
	}
	
	private void disable(Manager<?> manager){
		if(manager == null){
			Log.severe("Cannot disable null manager.");
			return;
		}
		try{
			manager.disable();
		}catch(Exception ex){
			Log.severe("Failed to disable manager \"" + manager.name + "\":");
			ex.printStackTrace();
		}
	}
	
	public Random getRandom() {
		return random;
	}
	
}
