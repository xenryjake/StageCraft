package com.xenry.stagecraft.bungee.player;
import com.mongodb.annotations.Beta;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@Beta
public class Player {
	
	protected final ProxiedPlayer proxiedPlayer;
	
	private boolean afk = false;
	
	Player(ProxiedPlayer proxiedPlayer) {
		this.proxiedPlayer = proxiedPlayer;
	}
	
	public ProxiedPlayer getProxiedPlayer() {
		return proxiedPlayer;
	}
	
	public boolean isAFK() {
		return afk;
	}
	
	public void setAFK(boolean afk) {
		this.afk = afk;
	}
	
}
