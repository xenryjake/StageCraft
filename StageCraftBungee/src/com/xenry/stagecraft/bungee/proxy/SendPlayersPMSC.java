package com.xenry.stagecraft.bungee.proxy;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.bungee.util.Log;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SendPlayersPMSC extends PluginMessageSubChannel<ProxyManager> {
	
	public SendPlayersPMSC(ProxyManager manager){
		super("SendPlayers", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver) {
		String serialized = in.readUTF();
		String serverName = in.readUTF();
		ServerInfo server = manager.plugin.getProxy().getServerInfo(serverName);
		if(server == null){
			Log.warn("Received SendPlayersPMSC with invalid server.");
			return;
		}
		List<String> targetNames = Arrays.asList(serialized.split(","));
		if(targetNames.size() < 1){
			Log.warn("Received SendPlayersPMSC with no targets.");
		}
		List<ProxiedPlayer> players = new ArrayList<>();
		if(targetNames.contains("ALL")){
			players.addAll(manager.plugin.getProxy().getPlayers());
		}else{
			for(String target : targetNames){
				ProxiedPlayer player = manager.plugin.getProxy().getPlayer(target);
				if(player != null){
					players.add(player);
				}
			}
		}
		for(ProxiedPlayer player : players){
			manager.getPlayerWillSwitchPMSC().send(player);
		}
		manager.plugin.getProxy().getScheduler().schedule(manager.plugin, () -> {
			for(ProxiedPlayer player : players){
				if(player.isConnected()){
					player.connect(server);
				}
			}
		}, 1, TimeUnit.SECONDS);
	}
	
}
