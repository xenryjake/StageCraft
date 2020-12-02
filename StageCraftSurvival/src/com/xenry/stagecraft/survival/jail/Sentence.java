package com.xenry.stagecraft.survival.jail;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import net.md_5.bungee.api.ChatColor;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Sentence extends BasicDBObject {
	
	public static final Access IMMUNITY = Punishment.IMMUNITY;
	
	// All times for punishments are stored in seconds
	
	@Deprecated
	public Sentence(){
		//required for Mongo instantiation
	}
	
	private Sentence(String playerUUID, String punishedByUUID, String reason, long expiresAt, long duration){
		put("player", playerUUID);
		put("punishedBy", punishedByUUID);
		put("timestamp", TimeUtil.toSecond(TimeUtil.now()));
		put("reason", reason);
		put("expiresAt", expiresAt);
		put("duration", duration);
		put("removed", false);
	}
	
	public Sentence(Jail jail, String playerUUID, String punishedByUUID, String reason, long expiresAt, long duration){
		this(playerUUID, punishedByUUID, reason, expiresAt, duration);
		put("jail", jail.getName());
	}
	
	public String getPlayerUUID(){
		return (String) get("player");
	}
	
	public String getPunishedByUUID(){
		return (String) get("punishedBy");
	}
	
	public long getTimestamp(){
		Object obj = get("timestamp");
		if(obj instanceof Long){
			return (Long)obj;
		}else if(obj instanceof Integer){
			return ((Integer)obj).longValue();
		}
		return 0;
	}
	
	public String getReason(){
		String reason = Emote.replaceEmotes((String) get("reason"), ChatColor.WHITE);
		return ChatColor.translateAlternateColorCodes('&', reason);
	}
	
	public String getJailName(){
		Object obj = get("jail");
		if(obj instanceof String){
			return (String)obj;
		}else{
			return null;
		}
	}
	
	public long getExpiresAt(){
		Object obj = get("expiresAt");
		if(obj instanceof Long){
			return (Long)obj;
		}else if(obj instanceof Integer){
			return ((Integer)obj).longValue();
		}
		return 0;
	}
	
	public long getDurationSeconds(){
		Object obj = get("duration");
		if(obj instanceof Long){
			return (Long)obj;
		}else if(obj instanceof Integer){
			return ((Integer)obj).longValue();
		}
		return 0;
	}
	
	public long getTimeRemaining(){
		if(isRemoved()){
			return 0;
		}
		if(getExpiresAt() == -1){
			return -1;
		}
		return Math.max(getExpiresAt() - TimeUtil.nowSeconds(), 0);
	}
	
	public boolean isActive(){
		return !isRemoved() && (getExpiresAt() == -1 || getTimeRemaining() > 0);
	}
	
	public boolean isRemoved(){
		return (boolean) get("removed");
	}
	
	public void setRemoved(boolean removed){
		put("removed", removed);
	}
	
	public String getMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append(M.msg).append("You have been sentenced to jail!\n");
		sb.append(M.msg).append("Duration: ").append(M.WHITE).append(TimeUtil.simplerString(getTimeRemaining())).append("\n");
		if(!getReason().isEmpty()){
			sb.append(M.msg).append("Reason: ").append(M.WHITE).append(getReason());
		}
		return sb.toString().trim();
	}
	
}
