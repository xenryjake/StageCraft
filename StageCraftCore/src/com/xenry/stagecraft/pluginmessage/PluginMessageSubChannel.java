package com.xenry.stagecraft.pluginmessage;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.StageCraftPlugin;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class PluginMessageSubChannel<P extends StageCraftPlugin, M extends Manager<P>> {
	
	public static final String CHANNEL_NAME = "StageCraft";
	
	public final String subChannelName;
	protected final M manager;
	
	public PluginMessageSubChannel(String subChannelName, M manager){
		this.subChannelName = subChannelName;
		this.manager = manager;
	}
	
	protected void receive(ByteArrayDataInput in, Player receiver){}
	
	protected final void send(byte[] data, Player sender){
		sender.sendPluginMessage(manager.getCore(), "BungeeCord", data);
	}
	
}
