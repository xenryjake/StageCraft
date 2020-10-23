package com.xenry.stagecraft.creative.teleportation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WarpHandler extends Handler<Creative,TeleportationManager> {
	
	private final DBCollection collection;
	private final List<Warp> warps;
	
	public WarpHandler(TeleportationManager manager){
		super(manager);
		collection = manager.plugin.getCore().getMongoManager().getCoreCollection("creativeWarps");
		collection.setObjectClass(Warp.class);
		warps = new ArrayList<>();
	}
	
	public Warp addWarp(Warp warp){
		warps.add(warp);
		saveWarp(warp);
		return warp;
	}
	
	public void downloadWarps(){
		warps.clear();
		DBCursor c = collection.find();
		while(c.hasNext()) {
			warps.add((Warp)c.next());
		}
	}
	
	public void saveAllWarps(){
		for(Warp warp : warps){
			saveWarp(warp);
		}
	}
	
	public void saveWarp(Warp warp){
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> collection.save(warp));
	}
	
	public void saveAllWarpsSync(){
		for(Warp warp : warps){
			saveWarpSync(warp);
		}
	}
	
	public void saveWarpSync(Warp warp){
		collection.save(warp);
	}
	
	public void deleteWarp(final Warp warp){
		warps.remove(warp);
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> collection.remove(new BasicDBObject("_id", warp.get("_id"))));
	}
	
	public List<Warp> getWarps(){
		return warps;
	}
	
	public List<String> getWarpNameList(){
		List<String> names = new ArrayList<>();
		for(Warp warp : warps){
			names.add(warp.getName());
		}
		Collections.sort(names);
		return names;
	}
	
	public Warp getWarp(String name){
		name = name.toLowerCase();
		for(Warp warp : warps){
			if(warp.getName().equalsIgnoreCase(name) || warp.getAliases().contains(name)){
				return warp;
			}
		}
		return null;
	}
	
	public Warp getSpawn(){
		Warp spawn = getWarp("spawn");
		if(spawn == null){
			World world = Bukkit.getWorld(WorldUtil.getDefaultLevelName());
			if(world == null){
				world = Bukkit.getWorld("world");
				if(world == null){
					Log.warn("Unable to get default world.");
					return null;
				}
			}
			spawn = addWarp(new Warp("spawn", world.getSpawnLocation()));
		}
		return spawn;
	}
	
	public void setSpawn(Location location){
		World world = location.getWorld();
		if(world == null){
			throw new IllegalArgumentException("Location#getWorld cannot be null");
		}
		world.setSpawnLocation(location);
		Warp spawn = getWarp("spawn");
		if(spawn != null){
			deleteWarp(spawn);
		}
		addWarp(new Warp("spawn", location));
	}
	
}
