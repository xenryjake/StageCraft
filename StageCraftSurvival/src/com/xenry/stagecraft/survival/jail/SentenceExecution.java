package com.xenry.stagecraft.survival.jail;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.xenry.stagecraft.punishment.PunishmentExecution.VIEW_ALERTS;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SentenceExecution {
	
	public final JailManager manager;
	public final Sentence sentence;
	public final Profile sentenced;
	public final CommandSender sentencedBy;
	
	public SentenceExecution(JailManager manager, Sentence sentence, CommandSender sentencedBy){
		this.manager = manager;
		this.sentence = sentence;
		sentenced = manager.getCore().getProfileManager().getProfileByUUID(sentence.getPlayerUUID());
		this.sentencedBy = sentencedBy;
	}
	
	public void apply(){
		manager.addSentence(sentence);
		Player sentencedPlayer = sentenced.getPlayer();
		if(sentencedPlayer != null){
			sentencedPlayer.sendMessage(sentence.getMessage());
			Jail jail = manager.getJailHandler().getJail(sentence.getJailName());
			if(jail != null){
				manager.plugin.getTeleportationManager().createAndExecuteTeleportation(sentencedPlayer, sentencedBy, sentencedPlayer.getLocation(), jail.getLocation(), Teleportation.Type.ADMIN, false);
				sentencedPlayer.setGameMode(GameMode.ADVENTURE);
			}
		}
		sendMessageToSentencer();
		broadcastMessage();
	}
	
	private void sendMessageToSentencer(){
		sentencedBy.sendMessage(M.msg + "You have sentenced player " + M.elm + sentenced.getLatestUsername() + M.msg + " to jail:");
		sentencedBy.sendMessage(M.arrow(M.WHITE + "Duration: " + M.elm + TimeUtil.simplerString(sentence.getDurationSeconds())));
		if(!sentence.getReason().isEmpty()) {
			sentencedBy.sendMessage(M.arrow(M.WHITE + "Reason: " + M.gry + sentence.getReason()));
		}
	}
	
	private void broadcastMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append(M.elm).append(sentenced.getLatestUsername()).append(M.msg).append(" has been sentenced to jail by ").append(M.elm).append(sentencedBy.getName()).append(M.msg).append(":").append("\n");
		sb.append(M.arrow("Duration: ")).append(M.WHITE).append(TimeUtil.simplerString(sentence.getTimeRemaining())).append("\n");
		if(!sentence.getReason().isEmpty()){
			sb.append(M.arrow("Reason: ")).append(M.WHITE).append(sentence.getReason()).append("\n");
		}
		sb.append(M.arrow("Jail: ")).append(M.WHITE).append(sentence.getJailName()).append("\n");
		String message = sb.toString().trim();
		for(Player player : Bukkit.getOnlinePlayers()){
			Profile profile = manager.getCore().getProfileManager().getProfile(player);
			if(profile == null || !VIEW_ALERTS.has(profile) || player == sentencedBy || player.getUniqueId().toString().equals(sentenced.getUUID())){
				continue;
			}
			player.sendMessage(message);
		}
		if(!(sentencedBy instanceof Player)){
			Log.toCS(message);
		}
	}
	
}
