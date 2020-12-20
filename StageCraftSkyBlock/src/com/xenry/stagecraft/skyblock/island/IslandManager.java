package com.xenry.stagecraft.skyblock.island;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.commands.IslandCommand;
import com.xenry.stagecraft.skyblock.island.commands.admin.IslandAdminCommand;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.Vector2DInt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandManager extends Manager<SkyBlock> {
	
	public final static int MAX_OWNED_ISLANDS_PER_PLAYER = 3;
	public final static int MAX_JOINED_ISLANDS_PER_PLAYER = 5;
	
	private final DBCollection collection;
	private final List<Island> islands;
	
	private World world;
	private SchematicHandler schematicHandler;
	
	public IslandManager(SkyBlock skyBlock){
		super("Islands", skyBlock);
		collection = getCore().getMongoManager().getCoreCollection("skyblockIslands");
		collection.setObjectClass(Island.class);
		islands = new ArrayList<>();
		
		if(getCore().getIntegrationManager().isWorldEdit()){
			Log.severe("WorldEdit not found! Cannot setup the schematic handler!");
		}else{
			schematicHandler = new SchematicHandler(this);
		}
	}
	
	@Override
	protected void onEnable() {
		String worldName = plugin.getConfig().getString("island-world");
		if(worldName == null){
			Log.warn("No island-world specified in config!");
		}else{
			world = Bukkit.getWorld(worldName);
			if(world == null){
				Log.warn("Island-world from config does not exist! (" + worldName + ")");
			}
		}
		
		registerCommand(new IslandCommand(this));
		registerCommand(new IslandAdminCommand(this));
		
		download();
		schematicHandler.loadMainIslandSchematic();
	}
	
	@Override
	protected void onDisable() {
		saveAllSync();
	}
	
	public World getWorld() {
		return world;
	}
	
	public SchematicHandler getSchematicHandler() {
		return schematicHandler;
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
	public Island getIsland(int islandX, int islandZ){
		for(Island island : islands){
			if(island.getX() == islandX && island.getZ() == islandZ){
				return island;
			}
		}
		return null;
	}
	
	@Nullable
	public Island getIsland(Location location){
		if(!location.getWorld().equals(world)){
			return null;
		}
		return getIsland(Island.actualToIsland(location.getBlockX()), Island.actualToIsland(location.getBlockZ()));
	}
	
	public List<Island> getOwnedIslands(GenericProfile profile){
		List<Island> islands = new ArrayList<>();
		for(Island island : this.islands){
			if(profile.getUUID().equals(island.getOwnerUUID())){
				islands.add(island);
			}
		}
		return islands;
	}
	
	public List<Island> getJoinedIslands(GenericProfile profile){
		List<Island> islands = new ArrayList<>();
		for(Island island : this.islands){
			if(island.isMember(profile.getUUID())){
				islands.add(island);
			}
		}
		return islands;
	}
	
	public Vector2DInt getAvailableIslandPosition(){
		int r = 0;
		int x = 0;
		int z = 0;
		while(true){
			Island island = getIsland(x, z);
			if(island == null){
				return new Vector2DInt(x, z);
			}
			if(z < r){
				z++;
				continue;
			}
			if(x > 0){
				x--;
				continue;
			}
			r++;
			x = r;
			z = 0;
		}
	}
	
	public boolean createIsland(Island island){
		if(getIsland(island.getID()) != null || getIsland(island.getX(), island.getZ()) != null){
			return false;
		}
		Log.info("Creating new island " + island.getID() + " at (" + island.getX() + "," + island.getZ() + ")");
		boolean success = schematicHandler.pasteMainIsland(world, island);
		addIsland(island);
		return success;
	}
	
	private void addIsland(Island island){
		islands.add(island);
		save(island);
	}
	
	private void download(){
		islands.clear();
		DBCursor c = collection.find(new BasicDBObject("serverName", getCore().getServerName()));
		while(c.hasNext()) {
			islands.add((Island)c.next());
		}
	}
	
	private void saveAll(){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for(Island island : islands){
				collection.save(island);
			}
		});
	}
	
	private void save(Island island){
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
	
	private void deleteFromDatabase(Island island){
		islands.remove(island);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.remove(new BasicDBObject("_id", island.get("_id"))));
	}
	
}
