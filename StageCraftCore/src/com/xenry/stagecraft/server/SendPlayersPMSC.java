package com.xenry.stagecraft.server;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.util.Log;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class SendPlayersPMSC extends PluginMessageSubChannel<Core,ServerManager> {
	
	public SendPlayersPMSC(ServerManager manager) {
		super("SendPlayers", manager);
	}
	
	public void send(Player sender, String target, String server){
		send(sender, Collections.singletonList(target), server);
	}
	
	public void send(Player sender, List<String> targets, String server){
		if(targets.isEmpty()){
			Log.warn("Attempted to send SendPlayersPMSC with no targets");
			return;
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = targets.iterator();
		while(it.hasNext()){
			sb.append(it.next());
			if(it.hasNext()){
				sb.append(",");
			}
		}
		String serialized = sb.toString();
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(serialized);
		out.writeUTF(server);
		send(out.toByteArray(), sender);
	}
	
}
