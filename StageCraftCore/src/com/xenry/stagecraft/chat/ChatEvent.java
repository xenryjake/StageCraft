package com.xenry.stagecraft.chat;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.event.CancellableEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ChatEvent extends CancellableEvent {
	
	private static final HandlerList handlerList = new HandlerList();
	
	private final Channel channel;
	private final Profile profile;
	private String message;
	
	public ChatEvent(boolean async, Channel channel, Profile profile, String message){
		super(async);
		this.channel = channel;
		this.profile = profile;
		this.message = message;
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public Profile getProfile() {
		return profile;
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
