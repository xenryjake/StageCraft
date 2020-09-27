package com.xenry.stagecraft.survival.builder.handlers;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerLimitHandler extends Handler<Survival,BuilderManager> {
	
	public PlayerLimitHandler(BuilderManager manager){
		super(manager);
	}
	
	@EventHandler
	public void on(InventoryOpenEvent event){
		HumanEntity he = event.getPlayer();
		if(!(he instanceof Player)){
			return;
		}
		Player player = (Player)he;
		if(!manager.isBuilder(player)){
			return;
		}
		if(event.getInventory().getType() == InventoryType.ENDER_CHEST){
			event.setCancelled(true);
			player.sendMessage(M.err + "You can't open an enderchest while in builder mode.");
		}
	}
	
	@EventHandler
	public void on(PlayerMoveEvent event){
		if(event.getTo() == null) return;
		Player player = event.getPlayer();
		if(!manager.isBuilder(player)){
			return;
		}
		if(!manager.isInBuildArea(event.getTo())){
			manager.removeBuilder(player);
			player.sendMessage(M.err + "Your builder mode was deactivated because you left the build area.");
		}
	}
	
	@EventHandler
	public void on(PlayerTeleportEvent event){
		if(event.getTo() == null) return;
		Player player = event.getPlayer();
		if(!manager.isBuilder(player)){
			return;
		}
		if(!manager.isInBuildArea(event.getTo())){
			manager.removeBuilder(player);
			player.sendMessage(M.err + "Your builder mode was deactivated because you left the build area.");
		}
	}
	
	@EventHandler
	public void on(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(manager.isBuilder(player)){
			manager.removeBuilder(player);
		}
	}
	
	@EventHandler
	public void on(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(manager.isBuilder(player)){
			event.setCancelled(true);
			player.sendMessage(M.err + "You can't drop items in builder mode.");
		}
	}
	
}
