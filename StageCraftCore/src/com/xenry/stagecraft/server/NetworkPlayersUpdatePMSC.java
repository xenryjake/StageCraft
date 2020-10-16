package com.xenry.stagecraft.server;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.SerializationUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class NetworkPlayersUpdatePMSC extends PluginMessageSubChannel<Core,ServerManager> {
	
	public NetworkPlayersUpdatePMSC(ServerManager manager) {
		super("NetworkPlayersUpdate", manager);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void receive(ByteArrayDataInput in, Player receiver) {
		HashMap<String,List<String>> map;
		try{
			map = (HashMap<String,List<String>>)SerializationUtil.deserializeBase64(in.readUTF());
		}catch(Exception ex){
			Log.warn("Failed to deserialize NetworkPlayersUpdatePMSC");
			return;
		}
		manager.setNetworkPlayers(map);
	}
	
}
