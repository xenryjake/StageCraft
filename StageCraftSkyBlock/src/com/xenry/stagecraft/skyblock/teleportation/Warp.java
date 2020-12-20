package com.xenry.stagecraft.skyblock.teleportation;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Warp extends BasicDBObject {
	
	@Deprecated
	public Warp(){
		//required for Mongo instantiation
	}
	
	public Warp(String name, String world, double x, double y, double z, float yaw, float pitch){
		put("name", name);
		put("aliases", new ArrayList<>());
		
		put("world", world);
		put("x", x);
		put("y", y);
		put("z", z);
		put("yaw", yaw);
		put("pitch", pitch);
	}
	
	public Warp(String name, Location location){
		this(name, location.getWorld() == null ? null : location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
	public String getName(){
		return (String)get("name");
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAliases(){
		Object obj = get("aliases");
		if(obj instanceof List){
			return (List<String>)obj;
		}else{
			put("aliases", new ArrayList<>());
			return getAliases();
		}
	}
	
	public void addAlias(String alias){
		List<String> aliases = getAliases();
		aliases.add(alias.toLowerCase());
		put("aliases", aliases);
	}
	
	public void removeAlias(String alias){
		List<String> aliases = getAliases();
		aliases.remove(alias.toLowerCase());
		put("aliases", aliases);
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
	
	public boolean setLocation(Location location){
		World world = location.getWorld();
		if(world == null){
			return false;
		}
		put("world", world.getName());
		put("x", location.getX());
		put("y", location.getY());
		put("z", location.getZ());
		put("yaw", location.getYaw());
		put("pitch", location.getPitch());
		return true;
	}
	
}
