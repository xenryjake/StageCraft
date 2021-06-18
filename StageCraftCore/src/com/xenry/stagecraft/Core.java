package com.xenry.stagecraft;
import com.xenry.stagecraft.chat.Channel;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.command.CommandManager;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.integration.IntegrationManager;
import com.xenry.stagecraft.mongo.MongoManager;
import com.xenry.stagecraft.pluginmessage.PluginMessageManager;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.ui.UIManager;
import com.xenry.stagecraft.util.Log;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 1/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class Core extends StageCraftPlugin {
	
	private static boolean debugMode = false;
	private static boolean betaFeaturesEnabled = false;
	private static Core instance;
	
	private String serverName = "none";
	
	private PluginMessageManager pluginMessageManager;
	private MongoManager mongoManager;
	private UIManager uiManager;
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
		Log.info("Setting server-name is: " + serverName);
		
		debugMode = getConfig().getBoolean("debug", false);
		Log.info("Setting debug is: " + debugMode);
		
		betaFeaturesEnabled = getConfig().getBoolean("beta-features", false);
		Log.info("Setting beta-features is: " + betaFeaturesEnabled);
	}
	
	@Override
	public void loadManagers() {
		try{
			pluginMessageManager = loadManager(PluginMessageManager.class);
			mongoManager = loadManager(MongoManager.class);
			uiManager = loadManager(UIManager.class);
			serverManager = loadManager(ServerManager.class);
			integrationManager = loadManager(IntegrationManager.class);
			profileManager = loadManager(ProfileManager.class);
			commandManager = loadManager(CommandManager.class);
			punishmentManager = loadManager(PunishmentManager.class);
			hologramManager = loadManager(HologramManager.class);
			chatManager = loadManager(ChatManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the Core managers!");
		}
	}
	
	@Override
	protected void onServerEnabled(){
		Log.toCS("\n\n\n§a§lThe server has loaded.\n\n\n");
	}
	
	public static Core getInstance() {
		return instance;
	}
	
	public PluginMessageManager getPluginMessageManager() {
		return pluginMessageManager;
	}
	
	public MongoManager getMongoManager() {
		return mongoManager;
	}
	
	public UIManager getUIManager() {
		return uiManager;
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
	
	public static boolean isDebugMode() {
		return debugMode;
	}
	
	public static boolean betaFeaturesEnabled() {
		return betaFeaturesEnabled;
	}
	
	public void setDebugMode(boolean enabled) {
		debugMode = enabled;
		getConfig().set("debug", enabled);
		saveConfig();
	}
	
	public void setBetaFeaturesEnabled(boolean enabled) {
		betaFeaturesEnabled = enabled;
		getConfig().set("beta-features", enabled);
		saveConfig();
	}
	
	public void reloadConfiguration(){
		reloadConfig();
		debugMode = getConfig().getBoolean("debug", false);
		Log.info("Setting debug is: " + debugMode);
		
		betaFeaturesEnabled = getConfig().getBoolean("beta-features", false);
		Log.info("Setting beta-features is: " + betaFeaturesEnabled);
		
		chatManager.setGlobalChatPrefix(getConfig().getString("global-chat-prefix", ""));
		
		try{
			chatManager.setPublicChannel(Channel.valueOf(getConfig().getString("public-chat-channel", Channel.GLOBAL.name())));
		}catch(Exception exception){
			Log.warn("Invalid public-chat-channel. Setting to " + Channel.GLOBAL.name() + ".");
			chatManager.setPublicChannel(Channel.GLOBAL);
		}
	}
	
}
