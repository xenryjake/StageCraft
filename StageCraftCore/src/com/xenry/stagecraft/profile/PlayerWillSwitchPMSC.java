package com.xenry.stagecraft.profile;
import com.google.common.io.ByteArrayDataInput;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerWillSwitchPMSC extends PluginMessageSubChannel<Core,ProfileManager> {
	
	public PlayerWillSwitchPMSC(ProfileManager manager){
		super("PlayerWillSwitch", manager);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		Player target = receiver;
		String uuid = in.readUTF();
		if(!uuid.equals(receiver.getUniqueId().toString())){
			target = Bukkit.getPlayer(UUID.fromString(uuid));
			if(target == null){
				Log.warn("Recieved PlayerWillSwitchPMSC for offline player.");
				return;
			}
		}
		Profile profile = manager.getProfile(target);
		if(profile != null){
			manager.save(profile);
		}
	}
	
}
