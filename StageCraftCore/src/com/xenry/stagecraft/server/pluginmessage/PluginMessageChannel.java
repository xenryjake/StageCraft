package com.xenry.stagecraft.server.pluginmessage;
/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class PluginMessageChannel {
	
	private final String name;
	
	public PluginMessageChannel(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	protected abstract void receive(byte[] content);
	
	protected void send(byte[] content){}
	
}
