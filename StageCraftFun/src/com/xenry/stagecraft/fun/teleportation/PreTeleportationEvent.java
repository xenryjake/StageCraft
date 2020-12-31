package com.xenry.stagecraft.fun.teleportation;
import com.xenry.stagecraft.util.event.CancellableEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PreTeleportationEvent extends CancellableEvent {
	
	private static final HandlerList handlerList = new HandlerList();
	
	private final Teleportation teleportation;
	private String cancellationMessage;
	
	public PreTeleportationEvent(Teleportation teleportation){
		this.teleportation = teleportation;
		cancellationMessage = "";
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
	
	public Teleportation getTeleportation() {
		return teleportation;
	}
	
	public String getCancellationMessage() {
		return cancellationMessage;
	}
	
	public void setCancellationMessage(String cancellationMessage) {
		this.cancellationMessage = cancellationMessage;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
