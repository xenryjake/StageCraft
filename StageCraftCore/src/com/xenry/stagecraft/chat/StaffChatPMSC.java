package com.xenry.stagecraft.chat;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.commands.StaffChatCommand;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public class StaffChatPMSC extends PluginMessageSubChannel<Core,ChatManager> {
	
	public StaffChatPMSC(ChatManager manager) {
		super("StaffChat", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String name = in.readUTF();
		String json = in.readUTF();
		BaseComponent[] text;
		try {
			text = ComponentSerializer.parse(json);
		}catch(Exception ex){
			Log.warn("Received malformed json in StaffChatPMSC");
			return;
		}
		BaseComponent[] message = new ComponentBuilder("[SC] " + name).color(ChatColor.DARK_PURPLE)
				.append(": ").color(ChatColor.DARK_GRAY)
				.append(text).color(ChatColor.GRAY).create();
		for(Profile profile : manager.plugin.getProfileManager().getOnlineProfiles()){
			if(StaffChatCommand.ACCESS.has(profile)){
				profile.sendMessage(message);
			}
		}
		Log.toCS(message);
	}
	
	public void send(Player sender, String senderName, BaseComponent[] message){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(senderName);
		out.writeUTF(ComponentSerializer.toString(message));
		send(out.toByteArray(), sender);
	}
	
}
