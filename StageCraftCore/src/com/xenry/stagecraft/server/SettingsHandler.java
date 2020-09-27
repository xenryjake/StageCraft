package com.xenry.stagecraft.server;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.Core;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

import static com.xenry.stagecraft.server.ServerSetting.Type;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SettingsHandler extends Handler<Core,ServerManager> {
	
	private final DBCollection collection;
	private final List<ServerSetting> settings;
	
	public SettingsHandler(ServerManager manager){
		super(manager);
		collection = manager.plugin.getMongoManager().getCoreCollection("serverSettings");
		collection.setObjectClass(ServerSetting.class);
		settings = new ArrayList<>();
	}
	
	public void downloadSettings(){
		settings.clear();
		DBCursor c = collection.find();
		while(c.hasNext()) {
			settings.add((ServerSetting)c.next());
		}
		handleSettingsUpdate();
	}
	
	public void saveAllSettings(){
		for(ServerSetting setting : settings){
			saveSetting(setting);
		}
	}
	
	private void saveSetting(ServerSetting setting){
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> collection.save(setting));
	}
	
	public void saveAllSettingsSync(){
		for(ServerSetting setting : settings){
			saveSettingSync(setting);
		}
	}
	
	private void saveSettingSync(ServerSetting setting){
		collection.save(setting);
	}
	
	private ServerSetting getSetting(String key){
		for(ServerSetting setting : settings){
			if(setting.getKey().equals(key)){
				return setting;
			}
		}
		return null;
	}
	
	private void addSetting(ServerSetting setting){
		settings.add(setting);
		saveSetting(setting);
	}
	
	public void handleSettingsUpdate(){
		Core.debugMode = getDebugMode();
		Core.betaFeaturesEnabled = getBetaFeaturesEnabled();
	}
	
	public boolean getDebugMode(){
		ServerSetting setting = getSetting("debugMode");
		if(setting == null){
			setting = new ServerSetting("debugMode", Type.BOOLEAN).setBooleanValue(false);
			addSetting(setting);
		}
		if(setting.getBooleanValue() == null){
			setting.setBooleanValue(false);
		}
		return setting.getBooleanValue();
	}
	
	public void setDebugMode(boolean debugMode){
		ServerSetting setting = getSetting("debugMode");
		if(setting == null){
			setting = new ServerSetting("debugMode", Type.BOOLEAN);
			addSetting(setting);
		}
		setting.setBooleanValue(debugMode);
		Core.debugMode = debugMode;
		saveSetting(setting);
	}
	
	public boolean getBetaFeaturesEnabled(){
		ServerSetting setting = getSetting("betaFeaturesEnabled");
		if(setting == null){
			setting = new ServerSetting("betaFeaturesEnabled", Type.BOOLEAN).setBooleanValue(false);
			addSetting(setting);
		}
		if(setting.getBooleanValue() == null){
			setting.setBooleanValue(false);
		}
		return setting.getBooleanValue();
	}
	
	public void setBetaFeaturesEnabled(boolean betaFeaturesEnabled){
		ServerSetting setting = getSetting("betaFeaturesEnabled");
		if(setting == null){
			setting = new ServerSetting("betaFeaturesEnabled", Type.BOOLEAN);
			addSetting(setting);
		}
		setting.setBooleanValue(betaFeaturesEnabled);
		Core.betaFeaturesEnabled = betaFeaturesEnabled;
		saveSetting(setting);
	}
	
	public String getMOTD(){
		ServerSetting setting = getSetting("motd");
		if(setting == null){
			setting = new ServerSetting("motd", Type.STRING).setStringValue("Edition 3 - Join Now!");
			addSetting(setting);
		}
		if(setting.getStringValue() == null){
			setting.setStringValue("Edition 3 - Join Now!");
		}
		return setting.getStringValue();
	}
	
	public void setMOTD(String motd){
		ServerSetting setting = getSetting("motd");
		if(setting == null){
			setting = new ServerSetting("motd", Type.STRING);
			addSetting(setting);
		}
		setting.setStringValue(motd);
		saveSetting(setting);
	}
	
}
