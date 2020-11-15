package com.xenry.stagecraft.bungee.player;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerStateUpdatePMSC extends PluginMessageSubChannel<PlayerManager> {
	
	public PlayerStateUpdatePMSC(PlayerManager manager) {
		super("PlayerStateUpdate", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver) {
		String uuid = in.readUTF();
		String name = in.readUTF();
		boolean afk = in.readBoolean();
		//boolean vanished = in.readBoolean();
		PlayerState state = manager.getPlayerState(uuid);
		if(state == null){
			return;
		}
		state.setAFK(afk);
		//state.setVanished(vanished);
		//todo send players update
	}
	
}
