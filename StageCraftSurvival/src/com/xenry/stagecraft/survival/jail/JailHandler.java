package com.xenry.stagecraft.survival.jail;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class JailHandler extends Handler<Survival,JailManager> {
	
	private final DBCollection collection;
	private final List<Jail> jails;
	
	public JailHandler(JailManager manager){
		super(manager);
		collection = getCore().getMongoManager().getCoreCollection("survivalJails");
		collection.setObjectClass(Jail.class);
		jails = new ArrayList<>();
	}
	
	public void addJail(Jail jail){
		jails.add(jail);
		saveJail(jail);
	}
	
	public void downloadJails(){
		jails.clear();
		DBCursor c = collection.find();
		while(c.hasNext()) {
			jails.add((Jail)c.next());
		}
	}
	
	public void saveAllJails(){
		for(Jail jail : jails){
			saveJail(jail);
		}
	}
	
	public void saveJail(Jail jail){
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> collection.save(jail));
	}
	
	public void saveAllJailsSync(){
		for(Jail jail : jails){
			saveJailSync(jail);
		}
	}
	
	public void saveJailSync(Jail jail){
		collection.save(jail);
	}
	
	public void deleteJail(final Jail jail){
		jails.remove(jail);
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> collection.remove(new BasicDBObject("_id", jail.get("_id"))));
	}
	
	public List<Jail> getJails(){
		return jails;
	}
	
	public List<String> getJailNameList(){
		List<String> names = new ArrayList<>();
		for(Jail jail : jails){
			names.add(jail.getName());
		}
		Collections.sort(names);
		return names;
	}
	
	public Jail getJail(String name){
		if(name == null){
			return null;
		}
		for(Jail jail : jails){
			if(jail.getName().equalsIgnoreCase(name)){
				return jail;
			}
		}
		return null;
	}
	
}
