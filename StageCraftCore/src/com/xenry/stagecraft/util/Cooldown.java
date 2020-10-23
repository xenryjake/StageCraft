package com.xenry.stagecraft.util;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Cooldown {
	
	private final long rechargeTime;
	private final String message;
	private final HashMap<String,Long> recharges;
	
	public Cooldown(long rechargeTime, @Nullable String message) {
		this.rechargeTime = rechargeTime;
		this.message = message;
		recharges = new HashMap<>();
	}
	
	public long getRechargeTime() {
		return rechargeTime;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean use(Profile profile){
		return use(profile.getPlayer());
	}
	
	public boolean use(Player player){
		return use(player, true);
	}
	
	public boolean use(Player player, boolean notify){
		if(!canUse(player, notify)){
			return false;
		}
		recharges.put(player.getUniqueId().toString(), System.currentTimeMillis() + rechargeTime);
		return true;
	}
	
	public boolean canUse(Profile profile){
		return canUse(profile.getPlayer());
	}
	
	public boolean canUse(Player player){
		return canUse(player, true);
	}
	
	public boolean canUse(Player player, boolean notify){
		long timeRemaining = recharges.getOrDefault(player.getUniqueId().toString(), 0L) - System.currentTimeMillis();
		if(timeRemaining > 0){
			if(message != null && notify){
				player.sendMessage(message.replace("%t%", TimeUtil.simplerString(timeRemaining / 1000L, false)));
			}
			return false;
		}
		return true;
	}
	
	public void clearRecharges(){
		recharges.clear();
	}
	
	public void removeRecharge(String uuid){
		recharges.remove(uuid);
	}
	
	public void removeRecharge(Player player){
		removeRecharge(player.getUniqueId().toString());
	}
	
}
