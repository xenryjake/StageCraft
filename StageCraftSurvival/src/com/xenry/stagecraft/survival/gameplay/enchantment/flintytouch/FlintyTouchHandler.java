package com.xenry.stagecraft.survival.gameplay.enchantment.flintytouch;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class FlintyTouchHandler extends Handler<Survival,GameplayManager> {
	
	public FlintyTouchHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockBreakEvent event){
	}
	
}
