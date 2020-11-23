package com.xenry.stagecraft.creative.profile;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.util.LocationVector;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class CreativeProfile extends GenericProfile {
	
	@Deprecated
	public CreativeProfile(){
		//required for Mongo instantiation
	}
	
	public CreativeProfile(UUID uuid){
		super(uuid);
		
		put("lastLocationX", 0.0);
		put("lastLocationY", 0.0);
		put("lastLocationZ", 0.0);
		put("lastLocationYaw", 0.0F);
		put("lastLocationPitch", 0.0F);
		put("lastLocationWorldName", "world");
		
		put("hasAcceptedRules", false);
	}
	
	public CreativeProfile(Player player){
		this(player.getUniqueId());
	}
	
	public LocationVector getLastLocationVector(){
		Object objX = get("lastLocationX");
		Object objY = get("lastLocationY");
		Object objZ = get("lastLocationZ");
		Object objYaw = get("lastLocationYaw");
		Object objPitch = get("lastLocationPitch");
		double x;
		if(objX instanceof Double){
			x = (Double)objX;
		}else{
			put("lastLocationX", 0);
			x = 0;
		}
		double y;
		if(objY instanceof Double){
			y = (Double)objY;
		}else{
			put("lastLocationY", 0);
			y = 0;
		}
		double z;
		if(objZ instanceof Double){
			z = (Double)objZ;
		}else{
			put("lastLocationZ", 0);
			z = 0;
		}
		float yaw;
		if(objYaw instanceof Float){
			yaw = (Float)objYaw;
		}else{
			put("lastLocationYaw", 0);
			yaw = 0;
		}
		float pitch;
		if(objPitch instanceof Float){
			pitch = (Float)objPitch;
		}else{
			put("lastLocationPitch", 0);
			pitch = 0;
		}
		return new LocationVector(x, y, z, yaw, pitch);
	}
	
	public String getLastLocationWorldName(){
		Object obj = get("lastLocationWorldName");
		if(obj instanceof String){
			return (String)obj;
		}else{
			put("lastLocationWorldName", "world");
			return getLastLocationWorldName();
		}
	}
	
	private void setLastLocation(double x, double y, double z, float yaw, float pitch){
		put("lastLocationX", x);
		put("lastLocationY", y);
		put("lastLocationZ", z);
		put("lastLocationYaw", yaw);
		put("lastLocationPitch", pitch);
	}
	
	public void setLastLocation(Location loc){
		setLastLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		World world = loc.getWorld();
		if(world != null){
			setLastLocationWorldName(world.getName());
		}
	}
	
	public void setLastLocationWorldName(String name){
		put("lastLocationWorldName", name);
	}
	
	public boolean hasAcceptedRules(){
		Object obj = get("hasAcceptedRules");
		if(obj instanceof Boolean){
			return (Boolean)obj;
		}else{
			put("hasAcceptedRules", false);
			return hasAcceptedRules();
		}
	}
	
	public void setHasAcceptedRules(boolean hasAcceptedRules){
		put("hasAcceptedRules", hasAcceptedRules);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Integer> getCurrency(){
		Object obj = get("currency");
		if(obj instanceof HashMap){
			return (HashMap<String,Integer>)obj;
		}else{
			put("currency", new HashMap<String,Integer>());
			return getCurrency();
		}
	}
	
}
