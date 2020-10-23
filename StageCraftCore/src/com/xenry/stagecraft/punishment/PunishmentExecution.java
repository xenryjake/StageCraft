package com.xenry.stagecraft.punishment;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
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
	
	public PunishmentExecution(PunishmentManager manager, Punishment punishment, Profile punished, String punishedByName){
		this.manager = manager;
		this.punishment = punishment;
		this.punished = punished;
		this.punishedByName = punishedByName;
	}
	
	public PunishmentExecution(PunishmentManager manager, Punishment punishment, String punishedByName){
		this(manager, punishment, manager.plugin.getProfileManager().getProfileByUUID(punishment.getPlayerUUID()), punishedByName);
	}
	
	public void apply(){
		Punishment.Type type = punishment.getType();
		manager.addPunishment(punishment);
		Player punishedPlayer = punished.getPlayer();
		if(punishedPlayer != null){
			if(type == Punishment.Type.MUTE){
				punishedPlayer.sendMessage(punishment.getMessage());
			}else{
				punishedPlayer.kickPlayer(punishment.getMessage());
			}
		}
		handleApplication();
		broadcastMessage();
	}
	
	protected abstract void broadcastMessage();
	
	protected void handleApplication(){}
	
}
