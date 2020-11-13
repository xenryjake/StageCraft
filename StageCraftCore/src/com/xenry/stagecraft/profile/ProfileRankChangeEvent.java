package com.xenry.stagecraft.profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProfileRankChangeEvent extends Event {
	
	private static final HandlerList handlerList = new HandlerList();
	
	private final Profile profile;
	private final Rank oldRank;
	private final Rank newRank;
	
	public ProfileRankChangeEvent(Profile profile, Rank oldRank, Rank newRank) {
		this.profile = profile;
		this.oldRank = oldRank;
		this.newRank = newRank;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public Rank getOldRank() {
		return oldRank;
	}
	
	public Rank getNewRank() {
		return newRank;
	}
	
	public static HandlerList getHandlerList() {
		return handlerList;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
