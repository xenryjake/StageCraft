package com.xenry.stagecraft.survival;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.survival.economy.EconomyManager;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Survival extends StageCraftPlugin {
	
	private GameplayManager gameplayManager;
	private EconomyManager economyManager;
	private BuilderManager builderManager;
	private HideNSeekManager hideNSeekManager;
	private TeleportationManager teleportationManager;
	private JailManager jailManager;
	
	public Survival() {
		super("Survival", Core.getInstance());
	}
	
	@Override
	public void onLoad(){
		try{
			gameplayManager = (GameplayManager) core.loadManager(this, GameplayManager.class);
			economyManager = (EconomyManager) core.loadManager(this, EconomyManager.class);
			builderManager = (BuilderManager) core.loadManager(this, BuilderManager.class);
			hideNSeekManager = (HideNSeekManager) core.loadManager(this, HideNSeekManager.class);
			teleportationManager = (TeleportationManager) core.loadManager(this, TeleportationManager.class);
			jailManager = (JailManager) core.loadManager(this, JailManager.class);
			//petManager = (PetManager) loadManager(PetManager.class);
		}catch(Exception ex){
			ex.printStackTrace();
			Log.severe("Something went wrong while loading the core managers!");
		}
	}
	
	public GameplayManager getGameplayManager() {
		return gameplayManager;
	}
	
	public EconomyManager getEconomyManager() {
		return economyManager;
	}
	
	public BuilderManager getBuilderManager() {
		return builderManager;
	}
	
	public HideNSeekManager getHideNSeekManager() {
		return hideNSeekManager;
	}
	
	public TeleportationManager getTeleportationManager() {
		return teleportationManager;
	}
	
	public JailManager getJailManager() {
		return jailManager;
	}
	
	/*public PetManager getPetManager() {
		return petManager;
	}*/
	
}
