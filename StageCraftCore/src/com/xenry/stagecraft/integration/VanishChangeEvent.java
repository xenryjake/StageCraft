package com.xenry.stagecraft.integration;
import com.xenry.stagecraft.profile.Profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 3/3/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class VanishChangeEvent extends Event {
	
	private static final HandlerList handlerList = new HandlerList();
	
	public final Profile profile;
	public final boolean state;
	
	public VanishChangeEvent(boolean isAsync, Profile profile, boolean state) {
		super(isAsync);
		this.profile = profile;
		this.state = state;
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public boolean isState() {
		return state;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
