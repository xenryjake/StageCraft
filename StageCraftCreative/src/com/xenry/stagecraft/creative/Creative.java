package com.xenry.stagecraft.creative;

import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.util.Log;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/3/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class Creative extends StageCraftPlugin {
	
	private GameplayManager gameplayManager;
	
	public Creative(){
		super("Creative", Core.getInstance());
	}
	
	@Override
	public void loadManagers() {
		try{
			gameplayManager = loadManager(GameplayManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the Creative managers!");
		}
	}
	
	public GameplayManager getGameplayManager() {
		return gameplayManager;
	}
	
}
