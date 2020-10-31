package com.xenry.stagecraft.chat;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import net.md_5.bungee.api.chat.BaseComponent;
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
	
	public void send(Player sender, BaseComponent[] message){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(ComponentSerializer.toString(message));
		send(out.toByteArray(), sender);
	}
	
}
