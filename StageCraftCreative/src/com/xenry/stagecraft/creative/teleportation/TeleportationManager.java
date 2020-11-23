package com.xenry.stagecraft.creative.teleportation;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.commands.*;
import com.xenry.stagecraft.creative.teleportation.commands.home.*;
import com.xenry.stagecraft.creative.teleportation.commands.warp.*;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TeleportationManager extends Manager<Creative> {
	
	private WarpHandler warpHandler;
	private HomeHandler homeHandler;
	
	private final HashMap<String,Teleportation> teleportations;
	private final HashMap<String,TeleportRequest> requestsTo;
	private final HashMap<String,Location> lastLocations;
	public final Cooldown cooldown;
	
	public TeleportationManager(Creative plugin){
		super("Teleportation", plugin);
		teleportations = new HashMap<>();
		requestsTo = new HashMap<>();
		lastLocations = new HashMap<>();
		cooldown = new Cooldown(3000, M.error("You must wait " + M.elm + "%t%" + M.err + " to teleport again."));
	}
	
	@Override
	protected void onEnable() {
		warpHandler = new WarpHandler(this);
		homeHandler = new HomeHandler(this);
		
		registerCommand(new TPCommand(this));
		registerCommand(new TPHereCommand(this));
		registerCommand(new TPAllCommand(this));
		registerCommand(new TPPositionCommand(this));
		registerCommand(new TopCommand(this));
		registerCommand(new TPOfflineCommand(this));
		registerCommand(new WorldCommand(this));
		registerCommand(new CenterCommand(this));
		registerCommand(new JumpCommand(this));
		
		registerCommand(new TPACommand(this)); //contains TPAHere command
		registerCommand(new TPAAllCommand(this));
		registerCommand(new TPAcceptCommand(this));
		registerCommand(new TPDenyCommand(this));
		registerCommand(new TPCancelCommand(this));
		
		registerCommand(new WarpCommand(this));
		registerCommand(new SetWarpCommand(this));
		registerCommand(new UpdateWarpCommand(this));
		registerCommand(new DeleteWarpCommand(this));
		registerCommand(new SpawnCommand(this));
		registerCommand(new SetSpawnCommand(this));
		registerCommand(new WarpInfoCommand(this));
		registerCommand(new SetWarpAliasCommand(this));
		registerCommand(new DeleteWarpAliasCommand(this));
		
		registerCommand(new HomeCommand(this));
		registerCommand(new SetHomeCommand(this));
		registerCommand(new DeleteHomeCommand(this));
		registerCommand(new ViewHomesCommand(this));
		registerCommand(new GoToHomeCommand(this));
		registerCommand(new DeleteHomeOtherCommand(this));
		
		registerCommand(new BackCommand(this));
		
		warpHandler.downloadWarps();
		homeHandler.downloadHomes();
	}
	
	@Override
	protected void onDisable(){
		warpHandler.saveAllWarpsSync();
		homeHandler.saveAllHomesSync();
	}
	
	public WarpHandler getWarpHandler() {
		return warpHandler;
	}
	
	public HomeHandler getHomeHandler() {
		return homeHandler;
	}
	
	public void createAndExecuteTeleportation(Player target, CommandSender teleporter, Location originalLocation,
											  Location location, Teleportation.Type type){
		createAndExecuteTeleportation(target, teleporter, originalLocation, location, type, true);
	}
	
	public void createAndExecuteTeleportation(Player target, CommandSender teleporter, Location originalLocation,
											  Location location, Teleportation.Type type, boolean safe){
		new Teleportation(this, target, teleporter, originalLocation, location, type, safe).execute();
	}
	
	void setTeleportation(Player player, Teleportation teleportation){
		if(getTeleportation(player) != null){
			cancelTeleportation(player, "You are teleporting somewhere else.");
		}
		teleportations.put(player.getUniqueId().toString(), teleportation);
	}
	
	public Teleportation getTeleportation(Player player){
		return teleportations.get(player.getUniqueId().toString());
	}
	
	public void cancelTeleportation(Player player, String message){
		Teleportation teleportation = teleportations.get(player.getUniqueId().toString());
		if(teleportation != null){
			teleportation.cancel(message);
			teleportations.remove(player.getUniqueId().toString());
		}
	}
	
	public void removeTeleportation(Player player){
		teleportations.remove(player.getUniqueId().toString());
	}
	
	public void createAndSendRequest(Player fromPlayer, Player toPlayer, boolean reverse){
		new TeleportRequest(this, fromPlayer, toPlayer, reverse).send();
	}
	
	void setRequestTo(Player player, TeleportRequest request){
		if(getRequestTo(player) != null){
			cancelRequestTo(player, false);
		}
		requestsTo.put(player.getUniqueId().toString(), request);
	}
	
	public TeleportRequest getRequestTo(Player player){
		return requestsTo.get(player.getUniqueId().toString());
	}
	
	public void cancelRequestTo(Player player, boolean notify){
		TeleportRequest teleportRequest = requestsTo.get(player.getUniqueId().toString());
		if(teleportRequest != null){
			teleportRequest.cancel(notify);
			removeRequestTo(player);
		}
	}
	
	public void removeRequestTo(Player player){
		requestsTo.remove(player.getUniqueId().toString());
	}
	
	public List<TeleportRequest> getRequestsFrom(Player player){
		List<TeleportRequest> requests = new ArrayList<>();
		for(TeleportRequest request : requestsTo.values()){
			if(request.fromPlayer == player){
				requests.add(request);
			}
		}
		return requests;
	}
	
	public void setLastLocation(Player player, Location location){
		lastLocations.remove(player.getUniqueId().toString());
		lastLocations.put(player.getUniqueId().toString(), location);
	}
	
	public Location getLastLocation(Player player){
		return lastLocations.getOrDefault(player.getUniqueId().toString(), null);
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		if(event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND
				|| event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN){
			setLastLocation(event.getPlayer(), event.getFrom());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		setLastLocation(event.getEntity(), event.getEntity().getLocation());
		ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/back");
		event.getEntity().spigot().sendMessage(new ComponentBuilder("Click here to go back to your last location.")
				.color(ChatColor.AQUA).underlined(true).event(ce).create());
	}
	
	//@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		Teleportation teleportation = getTeleportation(player);
		if(teleportation == null){
			return;
		}
		Location to = event.getTo();
		double distance = teleportation.originalLocation.getWorld() == to.getWorld()
				? to.distance(teleportation.originalLocation) : 1000000;
		if(distance > 2){
			cancelTeleportation(player, "You moved.");
		}
	}
	
	//@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getDamage() <= 0){
			return;
		}
		Entity entity = event.getEntity();
		if(!(entity instanceof Player)){
			return;
		}
		Player player = (Player)entity;
		Teleportation teleportation = getTeleportation(player);
		if(teleportation != null){
			cancelTeleportation(player, "You took damage.");
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		Warp spawn = warpHandler.getSpawn();
		if(spawn != null){
			event.setRespawnLocation(spawn.getLocation());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(player.hasPlayedBefore()) {
			return;
		}
		Warp spawn = warpHandler.getSpawn();
		if(spawn != null){
			player.teleport(spawn.getLocation());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		cancelTeleportation(event.getPlayer(), "You quit the server.");
		cancelRequestTo(event.getPlayer(), false);
	}
	
}
