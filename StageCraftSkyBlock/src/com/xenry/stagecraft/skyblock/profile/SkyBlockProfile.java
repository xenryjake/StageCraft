package com.xenry.stagecraft.skyblock.profile;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.util.LocationVector;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SkyBlockProfile extends GenericProfile {
	
	@Deprecated
	public SkyBlockProfile(){
		//required for Mongo instantiation
	}
	
	public SkyBlockProfile(Player player){
		super(player);
		
		put("activeIslandID", "none");
		
		put("lastLocationX", 0.0);
		put("lastLocationY", 0.0);
		put("lastLocationZ", 0.0);
		put("lastLocationYaw", 0.0);
		put("lastLocationPitch", 0.0);
		put("lastLocationWorldName", "world");
		
		put("hasAcceptedRules", false);
	}
	
	public String getActiveIslandID(){
		return getString("activeIslandID");
	}
	
	@Nullable
	public Island getActiveIsland(@NotNull IslandManager islandManager){
		Island island = islandManager.getIsland(getActiveIslandID());
		if(island == null || !island.isMember(getUUID())){
			return null;
		}
		return island;
	}
	
	public void setActiveIsland(@NotNull Island island){
		put("activeIslandID", island.getID());
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
		double yaw;
		if(objYaw instanceof Double){
			yaw = (Double)objYaw;
		}else{
			put("lastLocationYaw", 0);
			yaw = 0;
		}
		double pitch;
		if(objPitch instanceof Double){
			pitch = (Double)objPitch;
		}else{
			put("lastLocationPitch", 0);
			pitch = 0;
		}
		return new LocationVector(x, y, z, (float)yaw, (float)pitch);
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
	
	public void setLastLocation(double x, double y, double z, double yaw, double pitch){
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
	
}
