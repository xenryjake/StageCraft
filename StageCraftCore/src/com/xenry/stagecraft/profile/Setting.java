package com.xenry.stagecraft.profile;
/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum Setting {
	
	//SOCIAL_SPY("social_spy", false),
	SURVIVAL_PVP_ENABLED("survival_pvp", true),
	SKYBLOCK_PVP_ENABLED("skyblock_pvp", true),
	CREATIVE_PVP_ENABLED("creative_pvp", true),
	FUN_PVP_ENABLED("fun_pvp", true);
	
	private final String id;
	private final boolean defaultValue;
	
	Setting(String id, boolean defaultValue) {
		this.id = id;
		this.defaultValue = defaultValue;
	}
	
	public String getID() {
		return id;
	}
	
	public boolean getDefaultValue() {
		return defaultValue;
	}
	
	public static Setting getByID(String id){
		for(Setting setting : values()) {
			if(setting.getID().equalsIgnoreCase(id)) {
				return setting;
			}
		}
		return null;
	}
	
}
