package com.xenry.stagecraft.punishment;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class LocalPunishmentExecution extends PunishmentExecution {
	
	public final CommandSender punishedBy;
	
	public LocalPunishmentExecution(PunishmentManager manager, Punishment punishment, CommandSender punishedBy){
		super(manager, punishment, punishedBy instanceof Player ? punishedBy.getName() : M.CONSOLE_NAME);
		this.punishedBy = punishedBy;
	}
	
	@Override
	protected void handleApplication() {
		sendMessageToPunisher();
	}
	
	private void sendMessageToPunisher(){
		Punishment.Type type = punishment.getType();
		punishedBy.sendMessage(M.msg + "You have " + M.elm + type.verb + M.msg + " player " + M.elm + punished.getLatestUsername() + M.msg + ":");
		if(type != Punishment.Type.KICK){
			punishedBy.sendMessage(M.arrow("Duration: " + M.WHITE + TimeUtil.simplerString(punishment.getDurationSeconds())));
		}
		if(!punishment.getReason().isEmpty()) {
			punishedBy.sendMessage(M.arrow("Reason: " + M.WHITE + punishment.getReason()));
		}
	}
	
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
			if(profile == null || !VIEW_ALERTS.has(profile) || player == punishedBy || player.getUniqueId().toString().equals(punished.getUUID())){
				continue;
			}
			player.sendMessage(message);
		}
		if(!(punishedBy instanceof Player)){
			Log.toCS(message);
		}
	}
	
}
