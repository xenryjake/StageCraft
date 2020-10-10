package com.xenry.stagecraft.server.pluginmessage;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PluginMessageHandler extends Handler<Core,ServerManager> implements PluginMessageListener {
	
	private final List<PluginMessageChannel> channels;
	
	public PluginMessageHandler(ServerManager manager){
		super(manager);
		channels = new ArrayList<>();
	}
	
	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] content) {
		if(!channel.equals("BungeeCord")){
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(content);
		String subChannel = in.readUTF();
		switch(subChannel){
			case "IP":
			case "IPOther":
			case "PlayerCount":
			case "PlayerList":
			case "GetServers":
			case "GetServer":
			case "UUID":
			case "UUIDOther":
			case "ServerIP":
				return;
		}
		for(PluginMessageChannel chan : channels){
			if(subChannel.equals(chan.getName())){
				try{
					chan.receive(content);
				}catch(Exception ex){
					Log.warn("Failed to process PluginMessageChannel: " + subChannel);
					ex.printStackTrace();
				}
				return;
			}
		}
	}
	
}
