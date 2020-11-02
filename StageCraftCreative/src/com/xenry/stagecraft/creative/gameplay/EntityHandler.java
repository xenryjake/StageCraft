package com.xenry.stagecraft.creative.gameplay;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.creative.Creative;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EntityHandler extends Handler<Creative,GameplayManager> {
	
	//todo something better than this
	
	public EntityHandler(GameplayManager manager){
		super(manager);
	}
	
	/**
	 * events to handle
	 * PAPER
	 * 	ThrownEggHatchEvent
	 * 	TurtleLayEggEvent
	 * 	TurtleStartDiggingEvent
	 * 	WitchThrowPotionEvent
	 * BUKKIT
	 * 	CreatureSpawnEvent
	 * 	EntityCombustEvent
	 * 	EntityInteractEvent
	 * 	EntitySpawnEvent
	 *
	 */
	//@EventHandler
	public void on(CreatureSpawnEvent event){
		event.setCancelled(true);
	}
	
}
