package com.xenry.stagecraft.bungee.proxy;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/31/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PunishmentRemovePMSC extends PluginMessageSubChannel<ProxyManager> {
	
	public PunishmentRemovePMSC(ProxyManager manager){
		super("PunishmentRemove", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(in.readUTF()); //originServerName
		out.writeUTF(in.readUTF()); //modName
		out.writeUTF(in.readUTF()); //targetName
		out.writeUTF(in.readUTF()); //typeName
		sendToAll(out.toByteArray());
	}
	
}
