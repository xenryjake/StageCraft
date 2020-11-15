package com.xenry.stagecraft.bungee.proxy;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.bungee.util.Log;
import com.xenry.stagecraft.util.SerializationUtil;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class NetworkPlayersUpdatePMSC extends PluginMessageSubChannel<ProxyManager> {
	
	public NetworkPlayersUpdatePMSC(ProxyManager manager) {
		super("NetworkPlayersUpdate", manager);
	}
	
	public void send(){
		HashMap<String,List<String>> playerMap = new HashMap<>();
		for(Map.Entry<String,ServerInfo> entry : manager.plugin.getProxy().getServers().entrySet()){
			List<String> names = new ArrayList<>();
			for(ProxiedPlayer player : entry.getValue().getPlayers()){
				names.add(player.getName());
			}
			playerMap.put(entry.getKey(), names);
		}
		String serialized;
		try{
			serialized = SerializationUtil.serializeBase64(playerMap);
		} catch(Exception ex) {
			Log.warn("Failed to serialize NetworkPlayersUpdatePMSC");
			return;
		}
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(serialized);
		sendToAll(out.toByteArray());
	}
	
}
