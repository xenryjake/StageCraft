package com.xenry.stagecraft.skyblock;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfileManager;
import com.xenry.stagecraft.util.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	
	public SkyBlock(){
		super("SkyBlock", Core.getInstance());
	}
	
	@Override
	public void loadManagers() {
		try{
			skyBlockProfileManager = loadManager(SkyBlockProfileManager.class);
			islandManager = loadManager(IslandManager.class);
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
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
		return new VoidGenerator();
	}
	
	//todo move this to gameplay manager
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setGameMode(GameMode.SURVIVAL);
	}
	
}
