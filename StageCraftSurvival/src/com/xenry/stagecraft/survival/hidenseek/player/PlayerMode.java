package com.xenry.stagecraft.survival.hidenseek.player;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public enum PlayerMode {
	
	HIDER("Hider", hiderColor.toString()),
	SEEKER("Seeker", seekerColor.toString()),
	SPECTATOR("Spectator", spectatorColor.toString());
	
	private final String name;
	private final String color;
	
	PlayerMode(String name, String color){
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getColoredName(){
		return color + name;
	}
	
}
