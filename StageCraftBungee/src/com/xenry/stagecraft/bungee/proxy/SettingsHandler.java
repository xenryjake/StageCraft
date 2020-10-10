package com.xenry.stagecraft.bungee.proxy;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.bungee.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/7/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SettingsHandler extends Handler<ProxyManager> {
	
	private final DBCollection collection;
	private final List<ProxySetting> settings;
	
	public SettingsHandler(ProxyManager manager){
		super(manager);
		collection = manager.plugin.getMongoManager().getCoreCollection("proxySettings");
		collection.setObjectClass(ProxySetting.class);
		settings = new ArrayList<>();
	}
	
	public void downloadSettings(){
		settings.clear();
		DBCursor c = collection.find();
		while(c.hasNext()) {
			settings.add((ProxySetting)c.next());
		}
		//handleSettingsUpdate();
	}
	
	public void saveAllSettings(){
		for(ProxySetting setting : settings){
			saveSetting(setting);
		}
	}
	
	private void saveSetting(ProxySetting setting){
		manager.plugin.getProxy().getScheduler().runAsync(manager.plugin, () -> collection.save(setting));
	}
	
	public void saveAllSettingsSync(){
		for(ProxySetting setting : settings){
			saveSettingSync(setting);
		}
	}
	
	private void saveSettingSync(ProxySetting setting){
		collection.save(setting);
	}
	
	private ProxySetting getSetting(String key){
		for(ProxySetting setting : settings){
			if(setting.getKey().equals(key)){
				return setting;
			}
		}
		return null;
	}
	
	private void addSetting(ProxySetting setting){
		settings.add(setting);
		saveSetting(setting);
	}
	
}
