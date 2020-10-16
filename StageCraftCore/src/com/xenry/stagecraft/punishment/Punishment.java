package com.xenry.stagecraft.punishment;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import net.md_5.bungee.api.ChatColor;


/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Punishment extends BasicDBObject {
	
	public static final Access IMMUNITY = Rank.ADMIN;
	
	// All times for punishments are stored in seconds
	
	@Deprecated
	public Punishment(){
		//required for Mongo instantiation
	}
	
	public Punishment(Type type, String playerUUID, String punishedByUUID, String reason, long expiresAt, long duration){
		put("type", type.toString());
		put("player", playerUUID);
		put("punishedBy", punishedByUUID);
		put("timestamp", TimeUtil.toSecond(TimeUtil.now()));
		put("reason", reason);
		put("expiresAt", expiresAt);
		put("duration", duration);
		put("removed", false);
	}
	
	public Punishment(Type type, String playerUUID, String punishedByUUID, String reason){
		this(type, playerUUID, punishedByUUID, reason, type.defaultExpiry, type.defaultExpiry);
	}
	
	/*public Punishment(Jail jail, String playerUUID, String punishedByUUID, String reason, long expiresAt, long duration){
		this(Type.JAIL, playerUUID, punishedByUUID, reason, expiresAt, duration);
		put("jail", jail.getName());
	}*/
	
	/*public Punishment(Type type, Profile player, Profile punishedBy, String reason, long expiresAt, long duration){
		this(type, player.getUUID(), punishedBy.getUUID(), reason, expiresAt, duration);
	}
	
	public Punishment(Type type, Profile player, Profile punishedBy, String reason){
		this(type, player.getUUID(), punishedBy.getUUID(), reason);
	}*/
	
	public Type getType(){
		try{
			return Type.valueOf((String) get("type"));
		}catch(Exception ex){
			return null;
		}
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
	
	/*public String getJailName(){
		Object obj = get("jail");
		if(obj instanceof String){
			return (String)obj;
		}else{
			return null;
		}
	}*/
	
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
		long remaining = getExpiresAt() - TimeUtil.nowSeconds();
		if(remaining <= 0){
			setRemoved(true);
			return 0;
		}
		return remaining;
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
		sb.append(M.msg).append("You have been ").append(M.elm).append(getType().verb).append(M.msg).append("!").append("\n");
		if(getType() != Type.KICK){
			sb.append(M.msg).append("Duration: ").append(M.WHITE).append(TimeUtil.simplerString(getTimeRemaining())).append("\n");
		}
		if(!getReason().isEmpty()){
			sb.append(M.msg).append("Reason: ").append(M.WHITE).append(getReason());
		}
		return sb.toString().trim();
	}
	
	public enum Type {
		
		BAN("Ban", "banned", -1),
		MUTE("Mute", "muted", -1),
		KICK("Kick", "kicked", 0);
		
		public final String name, verb;
		public final int defaultExpiry;
		
		Type(String name, String verb, int defaultExpiry){
			this.name = name;
			this.verb = verb;
			this.defaultExpiry = defaultExpiry;
		}
		
	}
	
	public static Integer parseTime(String string) {
		string = string.toLowerCase();
		if(!string.matches("^[0-9]*[a-z]*$")) {
			return null;
		}
		int value;
		try {
			value = Integer.parseInt(string.replaceAll("[^0-9]", ""));
		} catch(Exception ex) {
			return null;
		}
		String unit = string.replaceAll("[^a-z]", "");
		if(unit.startsWith("mo")) {
			return null;
		}
		
		switch (unit) {
			case "seconds":
			case "second":
			case "secs":
			case "sec":
			case "s":
				return value;
			case "minutes":
			case "minute":
			case "mins":
			case "min":
			case "m":
				return value * 60;
			case "hours":
			case "hour":
			case "hrs":
			case "hr":
			case "h":
				return value * 3600;
			case "days":
			case "day":
			case "d":
				return value * 86400;
			default:
				return null;
		}
		
	}

}
