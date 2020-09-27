package com.xenry.stagecraft.survival.teleportation;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Home extends BasicDBObject {
	
	@Deprecated
	public Home(){
		//required for Mongo instantiation
	}
	
	public Home(String ownerUUID, String name, String world, double x, double y, double z, float yaw, float pitch){
		put("ownerUUID", ownerUUID);
		put("name", name);
		put("world", world);
		put("x", x);
		put("y", y);
		put("z", z);
		put("yaw", yaw);
		put("pitch", pitch);
	}
	
	public Home(String ownerUUID, String name, Location location){
		this(ownerUUID, name, location.getWorld() == null ? null : location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
	public Home(Profile owner, String name, Location location){
		this(owner.getUUID(), name, location);
	}
	
	public Home(Player owner, String name, Location location){
		this(owner.getUniqueId().toString(), name, location);
	}
	
	public String getOwnerUUID(){
		return (String)get("ownerUUID");
	}
	
	public String getName(){
		return (String)get("name");
	}
	
	public String getWorldName(){
		return (String)get("world");
	}
	
	public World getWorld(){
		return Bukkit.getWorld(getWorldName());
	}
	
	public double getX(){
		return (double) get("x");
	}
	
	public double getY(){
		return (double) get("y");
	}
	
	public double getZ(){
		return (double) get("z");
	}
	
	public float getYaw(){
		Object obj = get("yaw");
		if(obj instanceof Float){
			return (Float)obj;
		}else{
			return NumberUtil.doubleToFloat((Double)obj);
		}
	}
	
	public float getPitch(){
		Object obj = get("pitch");
		if(obj instanceof Float){
			return (Float)obj;
		}else{
			return NumberUtil.doubleToFloat((Double)obj);
		}
	}
	
	public Location getLocation(){
		return new Location(getWorld(), getX(), getY(), getZ(), getYaw(), getPitch());
	}
	
}
