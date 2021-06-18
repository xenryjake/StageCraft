package com.xenry.stagecraft.punishment;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/31/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class PunishmentRemovePMSC extends PluginMessageSubChannel<Core,PunishmentManager> {
	
	public PunishmentRemovePMSC(PunishmentManager manager) {
		super("PunishmentRemove", manager);
	}
	
	public void send(Player sender, String modName, String targetName, Punishment.Type type){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(manager.plugin.getServerName());
		out.writeUTF(modName);
		out.writeUTF(targetName);
		out.writeUTF(type.name());
		send(out.toByteArray(), sender);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String originServerName = in.readUTF();
		String modName = in.readUTF();
		String targetName = in.readUTF();
		String typeName = in.readUTF();
		
		if(originServerName.equals(manager.plugin.getServerName())){
			return;
		}
		
		Punishment.Type type;
		try{
			type = Punishment.Type.valueOf(typeName);
		}catch(Exception ex){
			Log.warn("Invalid punishment type received in PMSC.");
			return;
		}
		
		String message = M.elm + modName + M.msg + " has removed " + M.elm + targetName + M.msg + "'s " + type.name + "s.";
		for(Player player : Bukkit.getOnlinePlayers()){
			Profile profile = manager.plugin.getProfileManager().getProfile(player);
			if(profile != null && PunishmentExecution.VIEW_ALERTS.has(profile) && !player.getName().equals(modName)){
				player.sendMessage(message);
			}
		}
		Log.toCS(message);
		
		Player target = Bukkit.getPlayerExact(targetName);
		if(target != null){
			List<Punishment> list = manager.getActivePunishments(manager.plugin.getProfileManager().getProfile(target), type);
			for(Punishment pun : list){
				pun.setRemoved(true);
			}
		}
	}
	
}
