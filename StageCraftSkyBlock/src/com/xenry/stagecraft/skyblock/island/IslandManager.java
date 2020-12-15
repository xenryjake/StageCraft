package com.xenry.stagecraft.skyblock.island;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.commands.IslandCommand;
import com.xenry.stagecraft.skyblock.island.commands.admin.IslandAdminCommand;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandManager extends Manager<SkyBlock> {
	
	private final DBCollection collection;
	private final List<Island> islands;
	
	public IslandManager(SkyBlock skyBlock){
		super("Islands", skyBlock);
		collection = getCore().getMongoManager().getCoreCollection("skyblockIslands");
		collection.setObjectClass(Island.class);
		islands = new ArrayList<>();
	}
	
	@Override
	protected void onEnable() {
		registerCommand(new IslandCommand(this));
		registerCommand(new IslandAdminCommand(this));
		
		download();
	}
	
	@Override
	protected void onDisable() {
		saveAllSync();
	}
	
	@Nullable
	public Island getIsland(String id){
		for(Island island : islands){
			if(id.equalsIgnoreCase(island.getID())){
				return island;
			}
		}
		return null;
	}
	
	@Nullable
	public Island getIsland(int x, int z){
		for(Island island : islands){
			if(island.getX() == x && island.getZ() == z){
				return island;
			}
		}
		return null;
	}
	
	private void download(){
		islands.clear();
		DBCursor c = collection.find(new BasicDBObject("serverName", getCore().getServerName()));
		while(c.hasNext()) {
			islands.add((Island)c.next());
		}
	}
	
	public void saveAll(){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for(Island island : islands){
				collection.save(island);
			}
		});
	}
	
	public void save(final Island island){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(island));
	}
	
	private void saveAllSync(){
		for(Island island : islands){
			saveSync(island);
		}
	}
	
	private void saveSync(Island island){
		collection.save(island);
	}
	
	public void delete(final Island island){
		islands.remove(island);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.remove(new BasicDBObject("_id", island.get("_id"))));
	}
	
}
