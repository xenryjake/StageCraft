package com.xenry.stagecraft.punishment;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class LocalPunishmentExecution extends PunishmentExecution {
	
	public final CommandSender punishedBy;
	private final Player pmscSender;
	
	public LocalPunishmentExecution(PunishmentManager manager, @NotNull Punishment punishment, @NotNull CommandSender punishedBy, @Nullable Player pmscSender){
		super(manager, punishment, punishedBy instanceof Player ? punishedBy.getName() : M.CONSOLE_NAME, manager.getCore().getServerName());
		this.punishedBy = punishedBy;
		this.pmscSender = pmscSender;
	}
	
	@Override
	public void apply(){
		Punishment.Type type = punishment.getType();
		manager.addPunishment(punishment);
		Player punishedPlayer = punished.getPlayer();
		if(punishedPlayer != null){
			if(type.kickPlayer){
				punishedPlayer.kickPlayer(punishment.getMessage());
				punishedBy.sendMessage(M.elm + punishedPlayer.getName() + M.msg + " was disconnected from "
						+ manager.getCore().getServerName());
			}else{
				punishedPlayer.sendMessage(punishment.getMessage());
			}
		}
		sendMessageToPunisher();
		if(pmscSender != null){
			manager.getPunishmentPMSC().send(pmscSender, punishment);
		}
		broadcastMessage();
	}
	
	private void sendMessageToPunisher(){
		Punishment.Type type = punishment.getType();
		punishedBy.sendMessage(M.msg + "You have " + M.elm + type.verb + M.msg + " player " + M.elm + punished.getLatestUsername() + M.msg + ":");
		if(type.isTimed()){
			punishedBy.sendMessage(M.arrow("Duration: " + M.WHITE + TimeUtil.simplerString(punishment.getDurationSeconds())));
		}
		if(!punishment.getReason().trim().isEmpty()) {
			punishedBy.sendMessage(M.arrow("Reason: " + M.WHITE + punishment.getReason()));
		}
	}
	
}
