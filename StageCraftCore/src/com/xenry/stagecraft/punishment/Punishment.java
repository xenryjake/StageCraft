package com.xenry.stagecraft.punishment;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.command.Access;
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
	
	public static final Access IMMUNITY = Rank.MOD;
	public static final Access CAN_PUNISH_WITHOUT_REASON = Rank.ADMIN;
	
	// All times for punishments are stored in seconds
	
	@Deprecated
	public Punishment(){
		//required for Mongo instantiation
	}
	
	public Punishment(Type type, String playerUUID, String punishedByUUID, String reason, long timestamp,
					  long expiresAt, long duration){
		put("type", type.toString());
		put("player", playerUUID);
		put("punishedBy", punishedByUUID);
		put("reason", reason);
		put("timestamp", timestamp);
		put("expiresAt", expiresAt);
		put("duration", duration);
		put("removed", false);
	}
	
	public Punishment(Type type, String playerUUID, String punishedByUUID, String reason, long expiresAt,
					  long duration) {
		this(type, playerUUID, punishedByUUID, reason, TimeUtil.nowSeconds(), expiresAt, duration);
	}
	
	public Punishment(Type type, String playerUUID, String punishedByUUID, String reason){
		this(type, playerUUID, punishedByUUID, reason, type.defaultExpiry, type.defaultExpiry);
	}
	
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
		//String reason = Emote.replaceAllEmotes((String) get("reason"), ChatColor.WHITE);
		String reason = getString("reason");
		return ChatColor.translateAlternateColorCodes('&', reason);
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
		long remaining = getExpiresAt() - TimeUtil.nowSeconds();
		if(remaining <= 0){
			setRemoved(true);
			return 0;
		}
		return remaining;
	}
	
	public boolean isActive(){
		return getType().isTimed() && !isRemoved() && (getExpiresAt() == -1 || getTimeRemaining() > 0);
	}
	
	public boolean isRemoved(){
		return (boolean) get("removed");
	}
	
	public void setRemoved(boolean removed){
		put("removed", removed);
	}
	
	public String getMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append(M.msg).append("You have been ").append(M.elm).append(getType().verb).append(M.msg).append("!");
		if(!getReason().isEmpty()){
			sb.append("\n").append(M.msg).append("Reason: ").append(M.WHITE).append(getReason());
		}
		if(getType().isTimed()){
			sb.append("\n").append(M.msg).append("Duration: ").append(M.WHITE)
					.append(TimeUtil.simplerString(getTimeRemaining()));
			sb.append("\n").append(M.msg).append("You can submit an appeal on our discord: ").append(M.elm)
					.append("https://mine-together.net/discord");
		}
		return sb.toString().trim();
	}
	
	public enum Type {
		
		BAN("Ban", "banned", -1, true),
		MUTE("Mute", "muted", -1, false),
		KICK("Kick", "kicked", 0, true),
		WARNING("Warning", "warned", 0, false);
		
		public final String name, verb;
		public final int defaultExpiry;
		public final boolean kickPlayer;
		
		Type(String name, String verb, int defaultExpiry, boolean kickPlayer){
			this.name = name;
			this.verb = verb;
			this.defaultExpiry = defaultExpiry;
			this.kickPlayer = kickPlayer;
		}
		
		public boolean isTimed(){
			return defaultExpiry != 0;
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
