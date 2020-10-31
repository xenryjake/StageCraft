package com.xenry.stagecraft.bungee.chat;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.bungee.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.bungee.util.Log;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PrivateMessagePMSC extends PluginMessageSubChannel<ChatManager> {
	
	public PrivateMessagePMSC(ChatManager manager){
		super("PrivateMessage", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, ProxiedPlayer receiver) {
		String targetName = in.readUTF();
		String json = in.readUTF();
		BaseComponent[] message;
		try{
			message = ComponentSerializer.parse(json);
		}catch(Exception ex){
			Log.warn("Received malformed json in PrivateMessagePMSC");
			return;
		}
		manager.handlePrivateMessage(receiver, targetName, message);
	}
	
}
