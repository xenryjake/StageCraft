package com.xenry.stagecraft.bungee.player;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.RawPlayerState;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.bungee.util.Log;
import com.xenry.stagecraft.util.SerializationUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PlayerStateUpdatePMSC extends PluginMessageSubChannel<PlayerManager> {
	
	public PlayerStateUpdatePMSC(PlayerManager manager) {
		super("PlayerStateUpdate", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver) {
		String uuid = in.readUTF();
		boolean afk = in.readBoolean();
		boolean vanished = in.readBoolean();
		PlayerState state = manager.getPlayerState(uuid);
		if(state == null){
			return;
		}
		state.setAFK(afk);
		state.setVanished(vanished);
		manager.update();
	}
	
	public void send() {
		List<RawPlayerState> states = new ArrayList<>();
		for(PlayerState state : manager.getPlayerStates().values()){
			states.add(state.getRaw());
		}
		String serialized;
		try{
			serialized = SerializationUtil.serializeBase64(states);
		}catch(Exception ex) {
			Log.warn("Failed to serialize PlayerStateUpdatePMSC");
			return;
		}
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(serialized);
		sendToAll(out.toByteArray());
	}
	
}
