package com.xenry.stagecraft.profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * MineTogether created by Henry Blasingame (Xenry) on 3/18/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProfileRanksUpdateEvent extends Event {
	
	private static final HandlerList handlerList = new HandlerList();
	
	private final Profile profile;
	private final Rank targetRank;
	private final Action action;
	
	public ProfileRanksUpdateEvent(Profile profile, Rank targetRank, Action action) {
		this.profile = profile;
		this.targetRank = targetRank;
		this.action = action;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public Rank getTargetRank() {
		return targetRank;
	}
	
	public Action getAction() {
		return action;
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
	public enum Action {
		ADD("added", "to"),
		ADJUST("adjusted", "for"),
		REMOVE("removed", "from"),
		EXPIRE("expired", "from");
		
		public final String pastTenseVerb;
		public final String toFrom;
		
		Action(String pastTenseVerb, String toFrom) {
			this.pastTenseVerb = pastTenseVerb;
			this.toFrom = toFrom;
		}
	}
	
}
