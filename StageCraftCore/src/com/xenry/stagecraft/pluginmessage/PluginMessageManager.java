package com.xenry.stagecraft.pluginmessage;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
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
public final class PluginMessageManager extends Manager<Core> implements PluginMessageListener {
	
	private final List<PluginMessageSubChannel<?,?>> subChannels;
	
	public PluginMessageManager(Core plugin){
		super("PluginMessage", plugin);
		subChannels = new ArrayList<>();
	}
	
	@Override
	protected void onEnable() {
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
	}
	
	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player receiver, @NotNull byte[] data) {
		if(!channel.equalsIgnoreCase("BungeeCord")){
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(data);
		if(!in.readUTF().equalsIgnoreCase(PluginMessageSubChannel.CHANNEL_NAME)){
			return;
		}
		
		String subChannelName = in.readUTF();
		PluginMessageSubChannel<?,?> subChannel = getSubChannel(subChannelName);
		if(subChannel == null){
			Log.warn("Received unknown PluginMessage sub-channel: " + subChannelName);
			return;
		}
		subChannel.receive(in, receiver);
	}
	
	@Nullable
	public PluginMessageSubChannel<?,?> getSubChannel(String name){
		for(PluginMessageSubChannel<?,?> subChannel : subChannels){
			if(subChannel.subChannelName.equalsIgnoreCase(name)){
				return subChannel;
			}
		}
		return null;
	}
	
	public void registerSubChannel(PluginMessageSubChannel<?,?> subChannel){
		if(getSubChannel(subChannel.subChannelName) != null){
			throw new IllegalArgumentException("SubChannel with name " + subChannel.subChannelName + " already exists.");
		}
		subChannels.add(subChannel);
	}
	
}
