package com.xenry.stagecraft.server;
import com.xenry.stagecraft.RawPlayerState;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 3/3/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerState {
	
	private final RawPlayerState raw;
	
	public PlayerState(RawPlayerState raw) {
		this.raw = raw;
	}
	
	public RawPlayerState getRaw() {
		return raw;
	}
	
	public String getUUID(){
		return raw.getUUID();
	}
	
	public String getName(){
		return raw.getName();
	}
	
	public String getServerName(){
		return raw.getServerName();
	}
	
	public boolean isAFK(){
		return raw.isAFK();
	}
	
	public boolean isVanished(){
		return raw.isVanished();
	}
	
}
