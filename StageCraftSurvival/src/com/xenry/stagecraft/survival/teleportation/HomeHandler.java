package com.xenry.stagecraft.survival.teleportation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.survival.Survival;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
public final class HomeHandler extends Handler<Survival,TeleportationManager> {
	
	private final DBCollection collection;
	private final List<Home> homes;
	
	public HomeHandler(TeleportationManager manager){
		super(manager);
		collection = getCore().getMongoManager().getCoreCollection("survivalHomes");
		collection.setObjectClass(Home.class);
		homes = new ArrayList<>();
	}
	
	public void addHome(Home home){
		homes.add(home);
		saveHome(home);
	}
	
	public void downloadHomes(){
		homes.clear();
		DBCursor c = collection.find();
		while(c.hasNext()) {
			homes.add((Home)c.next());
		}
	}
	
	public void saveAllHomes(){
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> {
			for(Home home : homes){
				collection.save(home);
			}
		});
	}
	
	public void saveHome(Home home){
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> collection.save(home));
	}
	
	public void saveAllHomesSync(){
		for(Home home : homes){
			saveHomeSync(home);
		}
	}
	
	public void saveHomeSync(Home home){
		collection.save(home);
	}
	
	public void deleteHome(final Home home){
		homes.remove(home);
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> collection.remove(new BasicDBObject("_id", home.get("_id"))));
	}
	
	public List<Home> getAllHomes(){
		return homes;
	}
	
	public List<Home> getHomes(Profile profile){
		return getHomes(profile.getUUID());
	}
	
	public List<Home> getHomes(Player player){
		return getHomes(player.getUniqueId().toString());
	}
	
	public List<Home> getHomes(String ownerUUID){
		List<Home> list = new ArrayList<>();
		for(Home home : homes){
			if(home.getOwnerUUID().equals(ownerUUID)){
				list.add(home);
			}
		}
		return list;
	}
	
	public List<String> getHomeNameList(Profile profile){
		List<String> names = new ArrayList<>();
		for(Home home : homes){
			if(home.getOwnerUUID().equals(profile.getUUID())){
				names.add(home.getName());
			}
		}
		Collections.sort(names);
		return names;
	}
	
	public List<String> getHomeNameList(Profile profile, String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> names = new ArrayList<>();
		for(Home home : homes){
			if(home.getOwnerUUID().equals(profile.getUUID()) && home.getName().toLowerCase().startsWith(startsWith)){
				names.add(home.getName());
			}
		}
		Collections.sort(names);
		return names;
	}
	
	public Home getHome(Profile profile, String name){
		return getHome(profile.getUUID(), name);
	}
	
	public Home getHome(Player player, String name){
		return getHome(player.getUniqueId().toString(), name);
	}
	
	public Home getHome(String ownerUUID, String name){
		for(Home home : getHomes(ownerUUID)){
			if(home.getName().equalsIgnoreCase(name)){
				return home;
			}
		}
		return null;
	}
	
}
