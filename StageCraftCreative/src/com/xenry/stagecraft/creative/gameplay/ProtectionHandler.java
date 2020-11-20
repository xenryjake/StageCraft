package com.xenry.stagecraft.creative.gameplay;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.creative.Creative;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProtectionHandler extends Handler<Creative,GameplayManager> {
	
	public ProtectionHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(EntityExplodeEvent event){
		if(!event.blockList().isEmpty()){
			event.setCancelled(true);
			World world = event.getLocation().getWorld();
			if(world != null){
				world.createExplosion(event.getLocation(), 0);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockExplodeEvent event){
		if(!event.blockList().isEmpty()){
			event.setCancelled(true);
			event.getBlock().getWorld().createExplosion(event.getBlock().getLocation(), 0);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockBurnEvent event){
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(BlockFadeEvent event){
		// cancel if not frosted ice, EXPLICITLY ALLOW if frosted ice
		event.setCancelled(event.getBlock().getType() != Material.FROSTED_ICE);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(EntityBlockFormEvent event){
		event.setCancelled(true);
	}
	
}
