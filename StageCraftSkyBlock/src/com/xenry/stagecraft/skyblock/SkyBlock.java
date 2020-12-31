package com.xenry.stagecraft.skyblock;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.skyblock.gameplay.GameplayManager;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfileManager;
import com.xenry.stagecraft.skyblock.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.Log;

/**
 * SCSkyBlock created by Henry Blasingame (Xenry) on 9/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SkyBlock extends StageCraftPlugin {
	
	private SkyBlockProfileManager skyBlockProfileManager;
	private IslandManager islandManager;
	private GameplayManager gameplayManager;
	private TeleportationManager teleportationManager;
	
	public SkyBlock(){
		super("SkyBlock", Core.getInstance());
	}
	
	@Override
	protected void onLoadPreManager() {
		saveDefaultConfig();
	}
	
	@Override
	public void loadManagers() {
		try{
			skyBlockProfileManager = loadManager(SkyBlockProfileManager.class);
			islandManager = loadManager(IslandManager.class);
			gameplayManager = loadManager(GameplayManager.class);
			teleportationManager = loadManager(TeleportationManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the SkyBlock managers!");
		}
	}
	
	public SkyBlockProfileManager getSkyBlockProfileManager() {
		return skyBlockProfileManager;
	}
	
	public IslandManager getIslandManager() {
		return islandManager;
	}
	
	public GameplayManager getGameplayManager() {
		return gameplayManager;
	}
	
	public TeleportationManager getTeleportationManager() {
		return teleportationManager;
	}
	
}
