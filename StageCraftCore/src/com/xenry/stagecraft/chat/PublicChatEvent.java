package com.xenry.stagecraft.chat;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.CancellableEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PublicChatEvent extends CancellableEvent {
	
	private static final HandlerList handlerList = new HandlerList();
	
	private final Profile profile;
	private String prefix, displayName, message;
	
	public PublicChatEvent(boolean async, Profile profile, String prefix, String displayName, String message){
		super(async);
		this.profile = profile;
		this.prefix = prefix;
		this.displayName = displayName;
		this.message = message;
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean prefixContainsText(){
		return !ChatColor.stripColor(prefix).isEmpty();
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
