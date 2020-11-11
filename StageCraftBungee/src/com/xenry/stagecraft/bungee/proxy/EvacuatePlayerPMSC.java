package com.xenry.stagecraft.bungee.proxy;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EvacuatePlayerPMSC extends PluginMessageSubChannel<ProxyManager> {
	
	public EvacuatePlayerPMSC(ProxyManager manager){
		super("EvacuatePlayer", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver) {
		for(ServerInfo server : manager.plugin.getProxy().getServers().values()){
			if(receiver.getServer().getInfo().getName().equals(server.getName())){
				continue;
			}
			receiver.connect(server, ServerConnectEvent.Reason.SERVER_DOWN_REDIRECT);
		}
	}
	
}
