package com.xenry.stagecraft.survival.hidenseek.game;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerMode;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public enum GameType {
	
	HIDE_N_SEEK("Hide'N'Seek", PlayerMode.HIDER),
	SARDINES("Sardines", PlayerMode.SEEKER);
	
	private final String name;
	private final PlayerMode defaultPlayerMode;
	
	GameType(String name, PlayerMode defaultPlayerMode){
		this.name = name;
		this.defaultPlayerMode = defaultPlayerMode;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return name;
	}
	
	public PlayerMode getDefaultPlayerMode() {
		return defaultPlayerMode;
	}
	
}
