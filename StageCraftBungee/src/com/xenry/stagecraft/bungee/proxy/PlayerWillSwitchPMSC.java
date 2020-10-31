package com.xenry.stagecraft.bungee.proxy;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PlayerWillSwitchPMSC extends PluginMessageSubChannel<ProxyManager> {
	
	public PlayerWillSwitchPMSC(ProxyManager manager) {
		super("PlayerWillSwitch", manager);
	}
	
	public void send(ProxiedPlayer player){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(player.getUniqueId().toString());
		player.getServer().sendData("BungeeCord", out.toByteArray());
	}
	
}
