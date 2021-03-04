package com.xenry.stagecraft;
import java.io.Serializable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class RawPlayerState implements Serializable {
	
	private static final long serialVersionUID = 6198141608802028452L;
	
	private final String uuid;
	private final String name;
	private String serverName;
	private boolean afk = false;
	private boolean vanished = false;
	
	public RawPlayerState(String uuid, String name, String serverName) {
		this.uuid = uuid;
		this.name = name;
		this.serverName = serverName;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public boolean isAFK() {
		return afk;
	}
	
	public void setAFK(boolean afk) {
		this.afk = afk;
	}
	
	public boolean isVanished() {
		return vanished;
	}
	
	public void setVanished(boolean vanished) {
		this.vanished = vanished;
	}
	
}
