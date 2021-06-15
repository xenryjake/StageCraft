package com.xenry.stagecraft.bungee.player;
import com.xenry.stagecraft.RawPlayerState;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerState {
	
	private final ProxiedPlayer proxiedPlayer;
	private final RawPlayerState raw;
	
	PlayerState(ProxiedPlayer proxiedPlayer) {
		this.proxiedPlayer = proxiedPlayer;
		raw = new RawPlayerState(proxiedPlayer.getUniqueId().toString(), proxiedPlayer.getName(), "nowhere");
	}
	
	public ProxiedPlayer getProxiedPlayer() {
		return proxiedPlayer;
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
	
	public void setServerName(String serverName){
		raw.setServerName(serverName);
	}
	
	public boolean isAFK(){
		return raw.isAFK();
	}
	
	public void setAFK(boolean afk){
		raw.setAFK(afk);
	}
	
	public boolean isVanished(){
		return raw.isVanished();
	}
	
	public void setVanished(boolean vanished){
		raw.setVanished(vanished);
	}
	
}
