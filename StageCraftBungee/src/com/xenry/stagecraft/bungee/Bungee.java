package com.xenry.stagecraft.bungee;
import com.xenry.stagecraft.bungee.chat.ChatManager;
import com.xenry.stagecraft.bungee.mongo.MongoManager;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageManager;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import com.xenry.stagecraft.bungee.util.Log;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class Bungee extends Plugin {
	
	private static boolean debugMode = false;
	private static boolean betaFeaturesEnabled = false;
	private static Bungee instance;
	
	private final Random random;
	private final List<Manager> managers;
	private final ProxyConfiguration configuration;
	
	private PluginMessageManager pluginMessageManager;
	private MongoManager mongoManager;
	private ProxyManager proxyManager;
	private ChatManager chatManager;
	
	public Bungee(){
		instance = this;
		
		managers = new ArrayList<>();
		random = new Random();
		configuration = new ProxyConfiguration(this);
	}
	
	@Override
	public void onLoad() {
		debugMode = configuration.isDebug();
		betaFeaturesEnabled = configuration.betaFeaturesEnabled();
		
		Log.info("Loading managers...");
		try{
			pluginMessageManager = loadManager(PluginMessageManager.class);
			mongoManager = loadManager(MongoManager.class);
			proxyManager = loadManager(ProxyManager.class);
			chatManager = loadManager(ChatManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the managers!");
			return;
		}
		Log.info("Managers loaded.");
	}
	
	@Override
	public final void onEnable() {
		Log.info("Enabling managers...");
		try{
			for(Manager manager : managers){
				enable(manager);
			}
			for(Manager manager : managers){
				postEnable(manager);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while enabling the managers!");
			return;
		}
		Log.info("Managers enabled.");
	}
	
	@Override
	public final void onDisable() {
		Log.info("Disabling  managers...");
		try{
			ArrayList<Manager> reversed = new ArrayList<>(managers);
			Collections.reverse(reversed);
			for(Manager manager : reversed){
				disable(manager);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while disabling the managers!");
			return;
		}
		Log.info("Managers disabled.");
	}
	
	public static Bungee getInstance() {
		return instance;
	}
	
	public static boolean isDebugMode() {
		return debugMode;
	}
	
	public void setDebugMode(boolean enabled){
		debugMode = enabled;
		configuration.getConfiguration().set("debug", enabled);
		configuration.save();
	}
	
	public void setBetaFeaturesEnabled(boolean enabled){
		betaFeaturesEnabled = enabled;
		configuration.getConfiguration().set("beta-features", enabled);
		configuration.save();
	}
	
	public void reloadConfig(){
		configuration.load();
		debugMode = configuration.isDebug();
		betaFeaturesEnabled = configuration.betaFeaturesEnabled();
	}
	
	public static boolean betaFeaturesEnabled() {
		return betaFeaturesEnabled;
	}
	
	public Random getRandom() {
		return random;
	}
	
	protected <M extends Manager> M loadManager(Class<M> clazz){
		try{
			M manager = clazz.getConstructor(this.getClass()).newInstance(this);
			managers.add(manager);
			return manager;
		}catch(Exception ex){
			Log.severe("Failed to load manager \"" + clazz + "\":");
			ex.printStackTrace();
			return null;
		}
	}
	
	private void enable(Manager manager){
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
	
	private void postEnable(Manager manager){
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
	
	private void disable(Manager manager){
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
	
	public ProxyConfiguration getConfiguration() {
		return configuration;
	}
	
	public PluginMessageManager getPluginMessageManager() {
		return pluginMessageManager;
	}
	
	public MongoManager getMongoManager() {
		return mongoManager;
	}
	
	public ProxyManager getProxyManager() {
		return proxyManager;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}
	
}
