package com.xenry.stagecraft;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.commands.CommandManager;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.integration.IntegrationManager;
import com.xenry.stagecraft.mongo.MongoManager;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 1/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class Core extends StageCraftPlugin {
	
	public static boolean debugMode = false;
	public static boolean betaFeaturesEnabled = false;
	private static Core instance;
	
	private final Random random;
	private final HashMap<Manager<?>,StageCraftPlugin> managers;
	private String serverName = "NONE";
	
	private MongoManager mongoManager;
	private ServerManager serverManager;
	private IntegrationManager integrationManager;
	private ProfileManager profileManager;
	private CommandManager commandManager;
	private PunishmentManager punishmentManager;
	private HologramManager hologramManager;
	private ChatManager chatManager;
	
	public Core(){
		super("Core", null);
		core = this;
		instance = this;
		managers = new HashMap<>();
		random = new Random();
	}
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		serverName = getConfig().getString("server-name");
		if(serverName != null){
			serverName = serverName.toUpperCase();
		}
		if(serverName == null || serverName.equals("") || serverName.replaceAll("[^A-Z0-9_-]","").equals("")){
			throw new IllegalArgumentException("Invalid server-name option in config");
		}
		
		try{
			mongoManager = (MongoManager) loadManager(this, MongoManager.class);
			serverManager = (ServerManager) loadManager(this, ServerManager.class);
			integrationManager = (IntegrationManager) loadManager(this, IntegrationManager.class);
			profileManager = (ProfileManager) loadManager(this, ProfileManager.class);
			commandManager = (CommandManager) loadManager(this, CommandManager.class);
			punishmentManager = (PunishmentManager) loadManager(this, PunishmentManager.class);
			hologramManager = (HologramManager) loadManager(this, HologramManager.class);
			chatManager = (ChatManager) loadManager(this, ChatManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the core managers!");
		}
	}
	
	@Override
	public void onEnable() {
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
		
		Log.info("Plugin successfully enabled.");
		
		getServer().getScheduler().runTaskLater(this, () -> Log.toCS("§a§lThe server has loaded."), 1L);
	}
	
	@Override
	public void onDisable() {
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
		
		Log.info("Plugin successfully disabled!");
	}
	
	public static Core getInstance() {
		return instance;
	}
	
	public MongoManager getMongoManager() {
		return mongoManager;
	}
	
	public ServerManager getServerManager() {
		return serverManager;
	}
	
	public IntegrationManager getIntegrationManager() {
		return integrationManager;
	}
	
	public ProfileManager getProfileManager() {
		return profileManager;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public PunishmentManager getPunishmentManager() {
		return punishmentManager;
	}
	
	public HologramManager getHologramManager() {
		return hologramManager;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}
	
	public Manager<?> loadManager(StageCraftPlugin plugin, Class<? extends Manager<?>> clazz){
		Manager<?> manager;
		try{
			manager = clazz.getConstructor(plugin.getClass()).newInstance(plugin);
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
			Log.warn("Cannot PostEnable disabled manager.");
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
	
	public String getServerName() {
		return serverName;
	}
	
}
