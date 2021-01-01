package com.xenry.stagecraft.skyblock.island;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.commands.IslandCommand;
import com.xenry.stagecraft.skyblock.island.commands.admin.IslandAdminCommand;
import com.xenry.stagecraft.skyblock.island.commands.admin.IslandTestCommand;
import com.xenry.stagecraft.skyblock.island.playerstate.PlayerStateHandler;
import com.xenry.stagecraft.skyblock.island.playerstate.PlayerStateTestCommand;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
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
	private final List<Invite> invites;
	
	private World world;
	private final SchematicHandler schematicHandler;
	private final ProtectionHandler protectionHandler;
	private final PlayerStateHandler playerStateHandler;
	
	public IslandManager(SkyBlock skyBlock){
		super("Islands", skyBlock);
		collection = getCore().getMongoManager().getCoreCollection("skyblockIslands");
		collection.setObjectClass(Island.class);
		islands = new ArrayList<>();
		invites = new ArrayList<>();
		
		if(getCore().getIntegrationManager().isWorldEdit()){
			schematicHandler = new SchematicHandler(this);
		}else{
			schematicHandler = null;
			Log.severe("WorldEdit not found! Cannot setup the schematic handler!");
		}
		protectionHandler = new ProtectionHandler(this);
		playerStateHandler = new PlayerStateHandler(this);
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
		registerCommand(new IslandTestCommand(this));
		registerCommand(new PlayerStateTestCommand(this));
		
		schematicHandler.loadMainIslandSchematic();
		
		registerListener(protectionHandler);
		
		download();
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
	
	public ProtectionHandler getProtectionHandler() {
		return protectionHandler;
	}
	
	public PlayerStateHandler getPlayerStateHandler() {
		return playerStateHandler;
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
		if(!world.equals(location.getWorld())){
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
	
	public List<String> getIslandIDs(){
		List<String> ids = new ArrayList<>();
		for(Island island : islands){
			ids.add(island.getID());
		}
		return ids;
	}
	
	public List<String> getIslandIDs(String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> ids = new ArrayList<>();
		for(Island island : islands){
			if(island.getID().toLowerCase().startsWith(startsWith)){
				ids.add(island.getID());
			}
		}
		return ids;
	}
	
	public List<String> getIslandIDs(SkyBlockProfile profile){
		List<String> ids = new ArrayList<>();
		for(Island island : islands){
			if(island.isMember(profile)){
				ids.add(island.getID());
			}
		}
		return ids;
	}
	
	public List<String> getIslandIDs(GenericProfile profile, String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> ids = new ArrayList<>();
		for(Island island : islands){
			if(island.isMember(profile) && island.getID().toLowerCase().startsWith(startsWith)){
				ids.add(island.getID());
			}
		}
		return ids;
	}
	
	public void sendMessageToIsland(Island island, String message){
		for(String uuid : island.getMemberUUIDs()){
			Profile profile = getCore().getProfileManager().getOnlineProfileByUUID(uuid);
			if(profile != null && profile.isOnline()){
				profile.sendMessage(message);
			}
		}
	}
	
	// INVITES
	
	public void addInvite(Invite invite){
		invites.add(invite);
	}
	
	public void removeInvite(Invite invite){
		invites.remove(invite);
	}
	
	@Nullable
	public Invite getInvite(String invitedUUID, String islandID){
		for(Invite invite : invites){
			if(invite.invitedUUID.equals(invitedUUID) && invite.islandID.equals(islandID)){
				return invite;
			}
		}
		return null;
	}
	
	// ISLAND DATABASE MANAGEMENT
	
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
