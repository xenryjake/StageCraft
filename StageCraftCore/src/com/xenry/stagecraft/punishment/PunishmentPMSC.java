package com.xenry.stagecraft.punishment;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public class PunishmentPMSC extends PluginMessageSubChannel<Core,PunishmentManager> {
	
	public PunishmentPMSC(PunishmentManager manager){
		super("Punishment", manager);
	}
	
	public void send(Player sender, Punishment punishment){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward");
		out.writeUTF("ALL");
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(manager.plugin.getServerName());
		out.writeUTF(punishment.getType().toString());
		out.writeUTF(punishment.getPlayerUUID());
		out.writeUTF(punishment.getPunishedByUUID());
		out.writeUTF(punishment.getReason());
		out.writeLong(punishment.getTimestamp());
		out.writeLong(punishment.getExpiresAt());
		out.writeLong(punishment.getDurationSeconds());
		send(out.toByteArray(), sender);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String originServerName = in.readUTF();
		String typeName = in.readUTF();
		String uuid = in.readUTF();
		String punishedByUUID = in.readUTF();
		String reason = in.readUTF();
		long timestamp = in.readLong();
		long expiresAt = in.readLong();
		long duration = in.readLong();
		
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
		
		Profile profile = manager.plugin.getProfileManager().getProfileByUUID(uuid);
		if(profile == null){
			Log.warn("PunishmentPMSC had player uuid with no profile");
			return;
		}
		Punishment punishment = new Punishment(type, uuid, punishedByUUID, reason, timestamp, expiresAt, duration);
		manager.punishments.add(punishment);
		PunishmentExecution execution = new RemotePunishmentExecution(manager, punishment, profile, null, originServerName);
		execution.apply();
	}
	
}
