package com.xenry.stagecraft.chat;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.event.CancellableEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PrivateMessageEvent extends CancellableEvent {
	
	private static final HandlerList handlerList = new HandlerList();
	
	@Nullable
	private final Profile from;
	private final String targetName;
	private String message;
	
	public PrivateMessageEvent(@Nullable Profile from, String toName, String message){
		this.from = from;
		this.targetName = toName;
		this.message = message;
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
	public @Nullable Profile getFrom() {
		return from;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
