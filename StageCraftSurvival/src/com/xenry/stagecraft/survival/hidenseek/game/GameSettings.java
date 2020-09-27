package com.xenry.stagecraft.survival.hidenseek.game;
/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GameSettings {
	
	private int hideSeconds;
	private int seekSeconds;
	private int minimumPlayers;
	private boolean seekersSeeHiderDistance;
	private boolean hiderMovementAllowed;
	
	public GameSettings(int hideSeconds, int seekSeconds, int minimumPlayers, boolean seekersSeeHiderDistance, boolean hiderMovementAllowed) {
		this.hideSeconds = hideSeconds;
		this.seekSeconds = seekSeconds;
		this.minimumPlayers = minimumPlayers;
		this.seekersSeeHiderDistance = seekersSeeHiderDistance;
		this.hiderMovementAllowed = hiderMovementAllowed;
	}
	
	public int getHideSeconds() {
		return hideSeconds;
	}
	
	public void setHideSeconds(int hideSeconds) {
		this.hideSeconds = hideSeconds;
	}
	
	public int getSeekSeconds() {
		return seekSeconds;
	}
	
	public void setSeekSeconds(int seekSeconds) {
		this.seekSeconds = seekSeconds;
	}
	
	public int getMinimumPlayers() {
		return minimumPlayers;
	}
	
	public void setMinimumPlayers(int minimumPlayers) {
		this.minimumPlayers = minimumPlayers;
	}
	
	public boolean canSeekersSeeHiderDistance() {
		return seekersSeeHiderDistance;
	}
	
	public void setSeekersSeeHiderDistance(boolean seekersSeeHiderDistance) {
		this.seekersSeeHiderDistance = seekersSeeHiderDistance;
	}
	
	public boolean isHiderMovementAllowed() {
		return hiderMovementAllowed;
	}
	
	public void setHiderMovementAllowed(boolean hiderMovementAllowed) {
		this.hiderMovementAllowed = hiderMovementAllowed;
	}
	
}
