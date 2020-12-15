package com.xenry.stagecraft.skyblock.gameplay;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.skyblock.SkyBlock;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class GameplayManager extends Manager<SkyBlock> {
	
	public GameplayManager(SkyBlock plugin) {
		super("Gameplay", plugin);
	}
	
	//todo move this to gameplay manager
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setGameMode(GameMode.SURVIVAL);
	}
	
}
