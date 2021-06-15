package com.xenry.stagecraft.server;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.RawPlayerState;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.server.ServerManager;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.SerializationUtil;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 3/3/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PlayerStateUpdatePMSC extends PluginMessageSubChannel<Core,ServerManager> {
	
	public PlayerStateUpdatePMSC(ServerManager manager) {
		super("PlayerStateUpdate", manager);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void receive(ByteArrayDataInput in, Player receiver) {
		List<RawPlayerState> rawStates;
		try {
			rawStates = (List<RawPlayerState>)SerializationUtil.deserializeBase64(in.readUTF());
		} catch(Exception ex) {
			Log.warn("Failed to deserialize PlayerStateUpdatePMSC");
			return;
		}
		manager.setPlayerStates(rawStates);
	}
	
	public void send(Player sender, Player target, boolean afk, boolean vanished){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(target.getUniqueId().toString());
		out.writeBoolean(afk);
		out.writeBoolean(vanished);
		send(out.toByteArray(), sender);
	}
	
}
