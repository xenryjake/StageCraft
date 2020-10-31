package com.xenry.stagecraft.bungee.pluginmessage;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.bungee.Manager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class PluginMessageSubChannel<T extends Manager> {
	
	public static final String CHANNEL_NAME = "StageCraft";
	
	public final String subChannelName;
	protected final T manager;
	
	public PluginMessageSubChannel(String subChannelName, T manager){
		this.subChannelName = subChannelName;
		this.manager = manager;
	}
	
	protected void sendToAll(byte[] data){
		sendToAll(data, false);
	}
	
	protected void sendToAll(byte[] data, boolean queue){
		for(ServerInfo server : manager.plugin.getProxy().getServers().values()){
			server.sendData("BungeeCord", data, queue);
		}
	}
	
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver){}
	
	protected List<ProxiedPlayer> getSendersForAllServers(){
		List<ProxiedPlayer> senders = new ArrayList<>();
		for(Map.Entry<String,ServerInfo> entry : manager.plugin.getProxy().getServers().entrySet()){
			List<ProxiedPlayer> players = new ArrayList<>(entry.getValue().getPlayers());
			if(players.size() > 0){
				senders.add(players.get(0));
			}
		}
		return senders;
	}
	
}
