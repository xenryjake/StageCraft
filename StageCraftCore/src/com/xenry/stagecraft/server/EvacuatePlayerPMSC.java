package com.xenry.stagecraft.server;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class EvacuatePlayerPMSC extends PluginMessageSubChannel<Core,ServerManager> {
	
	public EvacuatePlayerPMSC(ServerManager manager){
		super("EvacuatePlayer", manager);
	}
	
	public void send(Player player){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		send(out.toByteArray(), player);
	}
	
}
