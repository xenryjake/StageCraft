package com.xenry.stagecraft.punishment;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.BungeeUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class RemotePunishmentExecution extends PunishmentExecution {
	
	public RemotePunishmentExecution(PunishmentManager manager, Punishment punishment, Profile punished,
									 String punishedByName, String originServerName) {
		super(manager, punishment, punished, punishedByName, originServerName);
	}
	
	@Override
	public void apply(){
		Punishment.Type type = punishment.getType();
		Player punishedPlayer = punished.getPlayer();
		if(punishedPlayer != null){
			if(type.kickPlayer){
				if(punishedByName != null){
					BungeeUtil.message(punishedPlayer, punishedByName, M.elm + punishedPlayer.getName() + M.msg
							+ " was disconnected from " + manager.getCore().getServerName());
				}
				punishedPlayer.kickPlayer(punishment.getMessage());
			}else{
				punishedPlayer.sendMessage(punishment.getMessage());
			}
		}
		broadcastMessage();
	}
	
}
