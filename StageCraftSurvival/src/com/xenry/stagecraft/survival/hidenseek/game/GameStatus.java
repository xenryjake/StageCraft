package com.xenry.stagecraft.survival.hidenseek.game;
/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public enum GameStatus {
	
	NONE("None"),
	PRE_GAME("Pre-Game"),
	HIDING("Hiding"),
	SEEKING("Seeking");
	
	private final String name;
	
	GameStatus(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
