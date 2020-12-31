package com.xenry.stagecraft.fun.teleportation;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.Warmup;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Teleportation {
	
	public static final long TELEPORT_WARMUP_SECONDS = 3;
	public static final double MAX_MOVE_DISTANCE = 2;
	
	public final TeleportationManager manager;
	public final Player target;
	public final CommandSender teleporter;
	public final Location originalLocation;
	public final Location location;
	public final Type type;
	public final boolean safe;
	
	private boolean executed = false;
	private Warmup warmup;
	
	Teleportation(TeleportationManager manager, Player target, CommandSender teleporter, Location originalLocation, Location location, Type type) {
		this(manager, target, teleporter, originalLocation, location, type, true);
	}
	
	Teleportation(TeleportationManager manager, Player target, CommandSender teleporter, Location originalLocation, Location location, Type type, boolean safe) {
		if(!safe && type != Type.ADMIN){
			throw new IllegalArgumentException("Non-safe Teleportations must be Type.ADMIN");
		}
		
		this.manager = manager;
		this.target = target;
		this.teleporter = teleporter;
		this.originalLocation = originalLocation;
		this.location = location;
		this.type = type;
		this.safe = safe;
	}
	
	public enum Type {
		ADMIN, WARP, HOME, BACK, REQUEST
	}
	
	public void execute(){
		if(executed){
			return;
		}
		PreTeleportationEvent event = new PreTeleportationEvent(this);
		manager.plugin.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()){
			cancel(event.getCancellationMessage());
			return;
		}
		manager.setTeleportation(target, this);
		if(type == Type.ADMIN){
			teleport();
			executed = true;
			return;
		}
		if(!manager.cooldown.canUse(target, true)){
			return;
		}
		target.sendMessage(M.msg + "Teleporting you in " + M.elm + TELEPORT_WARMUP_SECONDS + " seconds" + M.msg + "...");
		sendTeleporterMessageIfNotSame(M.elm + target.getName() + M.msg + " is teleporting in " + M.elm + TELEPORT_WARMUP_SECONDS + " seconds" + M.msg + "...");
		warmup = new Warmup(TELEPORT_WARMUP_SECONDS * 20, this::teleport);
		warmup.use();
		executed = true;
	}
	
	private void teleport(){
		manager.removeTeleportation(target);
		if(type != Type.ADMIN){
			if(originalLocation.getWorld() != target.getLocation().getWorld() || originalLocation.distance(target.getLocation()) > MAX_MOVE_DISTANCE){
				cancel("You moved too far!");
				return;
			}
		}
		Location finalLocation = location;
		if(safe && LocationUtil.isBlockUnsafe(location, target)){
			finalLocation = LocationUtil.getSafeTeleportDestination(location, target);
			if(finalLocation == null){
				if(warmup == null){
					target.sendMessage(M.error("Could not find a safe location."));
					sendTeleporterMessageIfNotSame(M.error("Could not teleport " + M.elm + target.getName() + M.err + ": no safe location found."));
				}
				return;
			}
		}
		TeleportationEvent event = new TeleportationEvent(this);
		manager.plugin.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()){
			cancel(event.getCancellationMessage());
			return;
		}
		target.sendMessage(M.msg + "Teleporting...");
		if(type == Type.ADMIN){
			sendTeleporterMessageIfNotSame(M.msg + "Teleporting " + M.elm + target.getName() + M.msg + "...");
		}
		if(target.getAllowFlight() && LocationUtil.shouldFlyOnTeleport(finalLocation)){
			target.setFlying(true);
		}
		target.teleport(finalLocation);
		manager.setLastLocation(target, originalLocation);
		manager.cooldown.use(target, false);
	}
	
	public void cancel(String message){
		if(warmup != null){
			warmup.cancel();
			target.sendMessage(M.msg + "Teleportation cancelled" + (message == null || message.isEmpty() ? "." : ": " + message));
		}
	}
	
	private void sendBothMessage(String message){
		target.sendMessage(message);
		sendTeleporterMessageIfNotSame(message);
	}
	
	private void sendTeleporterMessageIfNotSame(String message){
		if(target != teleporter && teleporter != null){
			teleporter.sendMessage(message);
		}
	}
	
}
