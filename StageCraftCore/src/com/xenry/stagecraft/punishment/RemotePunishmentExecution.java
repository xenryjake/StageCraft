package com.xenry.stagecraft.punishment;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class RemotePunishmentExecution extends PunishmentExecution {
	
	private final String originServerName;
	
	public RemotePunishmentExecution(PunishmentManager manager, Punishment punishment, Profile punished,
									 String punishedByName, String originServerName) {
		super(manager, punishment, punished, punishedByName);
		this.originServerName = originServerName;
	}
	
	@Override
	protected void broadcastMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append(M.elm).append(punished.getLatestUsername()).append(M.msg).append(" has been ").append(M.elm).append(punishment.getType().verb).append(M.msg).append(" by ").append(M.elm).append(punishedByName).append(M.msg).append(":").append("\n");
		if(punishment.getType() != Punishment.Type.KICK){
			sb.append(M.arrow("Duration: ")).append(M.WHITE).append(TimeUtil.simplerString(punishment.getTimeRemaining())).append("\n");
		}
		if(!punishment.getReason().isEmpty()){
			sb.append(M.arrow("Reason: ")).append(M.WHITE).append(punishment.getReason()).append("\n");
		}
		String message = sb.toString().trim();
		for(Player player : Bukkit.getOnlinePlayers()){
			Profile profile = manager.plugin.getProfileManager().getProfile(player);
			if(profile == null || !VIEW_ALERTS.has(profile) || player.getName().equals(punishedByName) || player.getUniqueId().toString().equals(punished.getUUID())){
				continue;
			}
			player.sendMessage(message);
		}
		if(!punishedByName.equals(M.CONSOLE_NAME) || !originServerName.equals(manager.plugin.getServerName())){
			Log.toCS(message);
		}
	}
	
}
