package com.xenry.stagecraft.fun;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.fun.gameplay.GameplayManager;
import com.xenry.stagecraft.fun.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.Log;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/30/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class Fun extends StageCraftPlugin {
	
	private GameplayManager gameplayManager;
	private TeleportationManager teleportationManager;
	
	public Fun() {
		super("Fun", Core.getInstance());
	}
	
	@Override
	public void loadManagers() {
		try{
			gameplayManager = loadManager(GameplayManager.class);
			teleportationManager = loadManager(TeleportationManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the SkyBlock managers!");
		}
	}
	
	public GameplayManager getGameplayManager() {
		return gameplayManager;
	}
	
	public TeleportationManager getTeleportationManager() {
		return teleportationManager;
	}
	
}
