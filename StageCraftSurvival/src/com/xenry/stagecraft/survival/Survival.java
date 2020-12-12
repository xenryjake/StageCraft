package com.xenry.stagecraft.survival;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.survival.economy.EconomyManager;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.survival.profile.SurvivalProfileManager;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class Survival extends StageCraftPlugin {
	
	private SurvivalProfileManager survivalProfileManager;
	private GameplayManager gameplayManager;
	private EconomyManager economyManager;
	private TeleportationManager teleportationManager;
	private JailManager jailManager;
	
	public Survival() {
		super("Survival", Core.getInstance());
	}
	
	@Override
	public void loadManagers(){
		try{
			survivalProfileManager = loadManager(SurvivalProfileManager.class);
			gameplayManager = loadManager(GameplayManager.class);
			economyManager = loadManager(EconomyManager.class);
			teleportationManager =  loadManager(TeleportationManager.class);
			jailManager = loadManager(JailManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the Survival managers!");
		}
	}
	
	public SurvivalProfileManager getSurvivalProfileManager() {
		return survivalProfileManager;
	}
	
	public GameplayManager getGameplayManager() {
		return gameplayManager;
	}
	
	public EconomyManager getEconomyManager() {
		return economyManager;
	}
	
	public TeleportationManager getTeleportationManager() {
		return teleportationManager;
	}
	
	public JailManager getJailManager() {
		return jailManager;
	}
	
}
