package com.xenry.stagecraft.survival.hidenseek.player;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameStatus;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class PlayerListener extends Handler<Survival,HideNSeekManager> {
	
	public PlayerListener(HideNSeekManager manager){
		super(manager);
	}
	
	public HideNSeekManager getManager() {
		return manager;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(manager.getStatus() == GameStatus.NONE || !(event.getEntity() instanceof Player)){
			return;
		}
		if(manager.getPlayerHandler().getPlayerMode((Player)event.getEntity()) == null){
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event){
		if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)){
			return;
		}
		Player damaged = (Player)event.getEntity();
		Player damager = (Player)event.getDamager();
		manager.getPlayerHandler().findPlayer(damager, damaged);
	}
	
	@SuppressWarnings("ConstantConditions")
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(!manager.getPlayerHandler().isInGame(player)){
			return;
		}
		Location from = event.getFrom();
		Location to = event.getTo();
		if(from == null || to == null){
			return;
		}
		if(from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()){
			return;
		}
		
		//block hider movement if applicable
		if(!manager.getSettings().isHiderMovementAllowed() && manager.getStatus() == GameStatus.SEEKING && manager.getPlayerHandler().getPlayerMode(player) == PlayerMode.HIDER){
			event.setCancelled(true);
			return;
		}
		
		//block lobby exit if 1) in pregame 2) seeker in hide mode
		if((manager.getStatus() == GameStatus.PRE_GAME || (manager.getStatus() == GameStatus.HIDING && manager.getPlayerHandler().getPlayerMode(player) == PlayerMode.SEEKER)) && !manager.getMap().getLobbyBoundary().isIn(to)){
			player.sendMessage(err + "You can't leave the lobby yet.");
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(manager.plugin, () -> player.teleport(manager.getMap().getLobbyPoint()));
			return;
		}
		
		//block map leave
		if(!manager.getMap().getGameBoundary().isIn(to) && !manager.getMap().getLobbyBoundary().isIn(to)){
			player.sendMessage(err + "You can't leave the map during a game.");
			event.setCancelled(true);
		}
		
		//todo teleport them to correct point in map if from is not in map? same with onTeleport
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();
		if(!manager.getPlayerHandler().isInGame(player)){
			return;
		}
		Location to = event.getTo();
		if(to == null){
			return;
		}
		
		//block hider movement if applicable
		if(!manager.getSettings().isHiderMovementAllowed() && manager.getStatus() == GameStatus.SEEKING && manager.getPlayerHandler().getPlayerMode(player) == PlayerMode.HIDER){
			event.setCancelled(true);
			return;
		}
		
		//block lobby exit if 1) in pregame 2) seeker in hide mode
		if((manager.getStatus() == GameStatus.PRE_GAME || (manager.getStatus() == GameStatus.HIDING && manager.getPlayerHandler().getPlayerMode(player) == PlayerMode.SEEKER)) && !manager.getMap().getLobbyBoundary().isIn(to)){
			player.sendMessage(err + "You can't leave the lobby yet.");
			event.setCancelled(true);
			return;
		}
		
		//block map leave
		if(!manager.getMap().getGameBoundary().isIn(to) && !manager.getMap().getLobbyBoundary().isIn(to)){
			player.sendMessage(err + "You can't leave the map during a game.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		manager.getPlayerHandler().removePlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		if(manager.getPlayerHandler().isInGame(event.getPlayer())){
			event.getPlayer().sendMessage(err + "You can't break blocks while in a Hide'N'Seek game.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		if(manager.getPlayerHandler().isInGame(event.getPlayer())){
			event.getPlayer().sendMessage(err + "You can't place blocks while in a Hide'N'Seek game.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event){
		if(manager.getPlayerHandler().isInGame(event.getPlayer()) && event.getInventory().getType() == InventoryType.ENDER_CHEST){
			event.getPlayer().sendMessage(err + "You can't open an enderchest while in a Hide'N'Seek game.");
			event.setCancelled(true);
		}
	}
	
}
