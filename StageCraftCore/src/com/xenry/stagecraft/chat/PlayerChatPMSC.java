package com.xenry.stagecraft.chat;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PlayerChatPMSC extends PluginMessageSubChannel<Core,ChatManager> {
	
	public PlayerChatPMSC(ChatManager manager){
		super("PlayerChat", manager);
	}
	
	public void send(Player pluginMessageSender, Channel channel, BaseComponent[] message){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(channel.name());
		out.writeUTF(ComponentSerializer.toString(message));
		send(out.toByteArray(), pluginMessageSender);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String channelName = in.readUTF();
		String json = in.readUTF();
		Channel channel;
		try{
			channel = Channel.valueOf(channelName);
		}catch(Exception ex){
			Log.warn("Invalid Channel name in PlayerChatPMSC");
			return;
		}
		if(channel.isPublic && channel != manager.getPublicChannel()){
			return;
		}
		BaseComponent[] message;
		try {
			message = ComponentSerializer.parse(json);
		}catch(Exception ex){
			Log.warn("Received malformed json in PlayerChatPMSC");
			return;
		}
		for(Profile profile : manager.plugin.getProfileManager().getOnlineProfiles()){
			if(channel.access.has(profile)){
				profile.sendMessage(message);
			}
		}
		Log.toCS(new ComponentBuilder("[#" + channel + "] ").append(message).create());
	}
	
}
