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
	private boolean afk;
	//private boolean vanished;
	
	public RawPlayerState(String uuid, String name, boolean afk) {
		this.uuid = uuid;
		this.name = name;
		this.afk = afk;
		//this.vanished = vanished;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAFK() {
		return afk;
	}
	
	public void setAFK(boolean afk) {
		this.afk = afk;
	}
	
	/*public boolean isVanished() {
		return vanished;
	}
	
	public void setVanished(boolean vanished) {
		this.vanished = vanished;
	}*/
	
}
