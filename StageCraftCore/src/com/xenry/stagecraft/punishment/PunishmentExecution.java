package com.xenry.stagecraft.punishment;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
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
public abstract class PunishmentExecution {
	
	public static final Access VIEW_ALERTS = Rank.MOD;
	
	public final PunishmentManager manager;
	public final Punishment punishment;
	public final Profile punished;
	public final String punishedByName;
	public final String originServerName;
	
	public PunishmentExecution(PunishmentManager manager, Punishment punishment, Profile punished, String punishedByName, String originServerName){
		this.manager = manager;
		this.punishment = punishment;
		this.punished = punished;
		this.punishedByName = punishedByName;
		this.originServerName = originServerName;
	}
	
	public PunishmentExecution(PunishmentManager manager, Punishment punishment, String punishedByName, String originServerName){
		this(manager, punishment, manager.plugin.getProfileManager().getProfileByUUID(punishment.getPlayerUUID()), punishedByName, originServerName);
	}
	
	public abstract void apply();
	
	protected void broadcastMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append(M.elm).append(punished.getLatestUsername()).append(M.msg).append(" has been ").append(M.elm).append(punishment.getType().verb).append(M.msg).append(" by ").append(M.elm).append(punishedByName).append(M.msg).append(":").append("\n");
		if(punishment.getType().isTimed()){
			sb.append(M.arrow("Duration: ")).append(M.WHITE).append(TimeUtil.simplerString(punishment.getTimeRemaining())).append("\n");
		}
		if(!punishment.getReason().trim().isEmpty()){
			sb.append(M.arrow("Reason: ")).append(M.WHITE).append(punishment.getReason()).append("\n");
		}
		String message = sb.toString().trim();
		for(Player player : Bukkit.getOnlinePlayers()){
			Profile profile = manager.plugin.getProfileManager().getProfile(player);
			if(profile != null && VIEW_ALERTS.has(profile) && !player.getName().equals(punishedByName) && !player.getUniqueId().toString().equals(punished.getUUID())){
				player.sendMessage(message);
			}
		}
		if(!M.CONSOLE_NAME.equals(punishedByName) || !originServerName.equals(manager.plugin.getServerName())){
			Log.toCS(message);
		}
	}
	
}
