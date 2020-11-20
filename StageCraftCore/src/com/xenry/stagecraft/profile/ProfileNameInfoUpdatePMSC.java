package com.xenry.stagecraft.profile;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.util.Log;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class ProfileNameInfoUpdatePMSC extends PluginMessageSubChannel<Core,ProfileManager> {
	
	public ProfileNameInfoUpdatePMSC(ProfileManager manager) {
		super("ProfileNameInfoUpdate", manager);
	}
	
	public void send(Player sender, Profile profile){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(manager.plugin.getServerName());
		out.writeUTF(profile.getUUID());
		out.writeUTF(profile.getNick());
		out.writeUTF(profile.getNameColor().getName());
		send(out.toByteArray(), sender);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String serverOriginName = in.readUTF();
		if(serverOriginName.equals(manager.plugin.getServerName())){
			return;
		}
		String uuid = in.readUTF();
		String nick = in.readUTF();
		String nameColor = in.readUTF();
		
		Profile profile = manager.getProfileByUUID(uuid);
		if(profile == null){
			return;
		}
		ChatColor color;
		try{
			color = ChatColor.of(nameColor);
		}catch(Exception ex){
			Log.warn("Received ProfileNameInfoUpdatePMSC with invalid nameColor.");
			return;
		}
		if(profile.isOnline()){
			profile.setNick(nick);
			profile.setNameColor(color);
			profile.updateDisplayName();
			manager.save(profile);
		}
	}
	
}
