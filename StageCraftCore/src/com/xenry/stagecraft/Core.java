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
	
	private String serverName = "none";
	
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
	}
	
	@Override
	public void onLoadPreManager() {
		saveDefaultConfig();
		serverName = getConfig().getString("server-name");
		if(serverName != null){
			serverName = serverName.toLowerCase();
		}
		if(serverName == null || serverName.equals("") || serverName.replaceAll("[^a-z0-9_-]","").equals("")){
			throw new IllegalArgumentException("Invalid server-name option in config");
		}
	}
	
	@Override
	public void loadManagers() {
		try{
			mongoManager = loadManager(this, MongoManager.class);
			serverManager = loadManager(this, ServerManager.class);
			integrationManager = loadManager(this, IntegrationManager.class);
			profileManager = loadManager(this, ProfileManager.class);
			commandManager = loadManager(this, CommandManager.class);
			punishmentManager = loadManager(this, PunishmentManager.class);
			hologramManager = loadManager(this, HologramManager.class);
			chatManager = loadManager(this, ChatManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the Core managers!");
		}
	}
	
	@Override
	protected void onServerEnabled(){
		Log.toCS("§a§lThe server has loaded.");
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
	
	public String getServerName() {
		return serverName;
	}
	
}
