package com.xenry.stagecraft.bungee.proxy;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.bungee.util.Log;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public class SudoPMSC extends PluginMessageSubChannel<ProxyManager> {
	
	public SudoPMSC(ProxyManager manager){
		super("Sudo", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver) {
		String originServerName = in.readUTF();
		String sudoer = in.readUTF();
		String target = in.readUTF();
		String content = in.readUTF();
		
		Log.toCS(TextComponent.fromLegacyText(M.msg + "Executing remote sudo for " + M.elm + target + M.msg
				+ " by " + M.elm + sudoer + M.msg + " on " + M.elm + originServerName + M.msg + ": " + M.WHITE
				+ content));
		
		if(target.equalsIgnoreCase("#proxy")){
			manager.plugin.getProxy().getPluginManager().dispatchCommand(manager.plugin.getProxy().getConsole(),
					content);
			return;
		}
		
		List<ServerInfo> servers = new ArrayList<>();
		if(target.startsWith("#")){
			String name = target.substring(1);
			if(name.equals("**")){
				servers.addAll(manager.plugin.getProxy().getServers().values());
			}else{
				for(Map.Entry<String,ServerInfo> entry : manager.plugin.getProxy().getServers().entrySet()){
					if(entry.getKey().equalsIgnoreCase(name)){
						servers.add(entry.getValue());
					}
				}
				if(servers.isEmpty()){
					Log.warn("Received SudoPMSC with invalid target server.");
					return;
				}
			}
			target = "#console";
		}else if(target.equals("**")){
			servers.addAll(manager.plugin.getProxy().getServers().values());
		}else{
			ProxiedPlayer player = manager.plugin.getProxy().getPlayer(target);
			if(player == null){
				Log.warn("Received SudoPMSC with invalid target player.");
				return;
			}
			servers.add(player.getServer().getInfo());
		}
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(originServerName);
		out.writeUTF(sudoer);
		out.writeUTF(target);
		out.writeUTF(content);
		byte[] data = out.toByteArray();
		for(ServerInfo server : servers){
			server.sendData("BungeeCord", data, false);
		}
	}
	
}
