package com.xenry.stagecraft.bungee.chat;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.bungee.util.Log;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public class StaffChatPMSC extends PluginMessageSubChannel<ChatManager> {
	
	public StaffChatPMSC(ChatManager manager) {
		super("StaffChat", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver){
		String name = in.readUTF();
		String json = in.readUTF();
		BaseComponent[] text;
		try {
			text = ComponentSerializer.parse(json);
		}catch(Exception ex){
			Log.warn("Received malformed json in StaffChatPMSC");
			return;
		}
		Log.toCS(new ComponentBuilder("[SC] " + name).color(ChatColor.DARK_PURPLE)
				.append(": ").color(ChatColor.DARK_GRAY)
				.append(text).color(ChatColor.GRAY).create());
		
		List<ProxiedPlayer> senders = getSendersForAllServers();
		if(senders.isEmpty()){
			return;
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(name);
		out.writeUTF(json);
		byte[] data = out.toByteArray();
		for(ProxiedPlayer sender : senders){
			sender.getServer().sendData("BungeeCord", data);
		}
	}
	
}
