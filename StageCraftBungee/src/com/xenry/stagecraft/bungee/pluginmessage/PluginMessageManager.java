package com.xenry.stagecraft.bungee.pluginmessage;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.bungee.Bungee;
import com.xenry.stagecraft.bungee.Manager;
import com.xenry.stagecraft.bungee.util.Log;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PluginMessageManager extends Manager {
	
	private final List<PluginMessageSubChannel<?>> subChannels;
	
	public PluginMessageManager(Bungee plugin){
		super("PluginMessage", plugin);
		subChannels = new ArrayList<>();
	}
	
	@EventHandler
	public void on(PluginMessageEvent event){
		Connection receiver = event.getReceiver();
		if(!(receiver instanceof ProxiedPlayer)){
			// the message is outgoing
			return;
		}
		if(!event.getTag().equalsIgnoreCase("BungeeCord")){
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		if(!in.readUTF().equalsIgnoreCase("StageCraft")){
			return;
		}
		
		String subChannelName = in.readUTF();
		PluginMessageSubChannel<?> subChannel = getSubChannel(subChannelName);
		if(subChannel == null){
			Log.warn("Received unknown PluginMessage sub-channel: " + subChannelName);
			return;
		}
		subChannel.receive(in, (ProxiedPlayer)event.getReceiver());
	}
	
	@Nullable
	public PluginMessageSubChannel<?> getSubChannel(String name){
		for(PluginMessageSubChannel<?> subChannel : subChannels){
			if(subChannel.subChannelName.equalsIgnoreCase(name)){
				return subChannel;
			}
		}
		return null;
	}
	
	public void registerSubChannel(PluginMessageSubChannel<?> subChannel){
		if(getSubChannel(subChannel.subChannelName) != null){
			throw new IllegalArgumentException("SubChannel with name " + subChannel.subChannelName + " already exists.");
		}
		subChannels.add(subChannel);
	}
	
}
