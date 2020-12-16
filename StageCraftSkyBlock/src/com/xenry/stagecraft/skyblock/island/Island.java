package com.xenry.stagecraft.skyblock.island;
import com.mongodb.BasicDBObject;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Island extends BasicDBObject {
	
	public static final int WIDTH_IN_CHUNKS = 16;
	public static final int WIDTH = 16*WIDTH_IN_CHUNKS;
	
	// ISLAND COORDS to CHUNK COORDS (chunkWidth = 16)
	// [x,z]: (16x,16z) -> (16x+chunkWidth-1,16z+chunkWidth-1)
	// [0,0]: (0,0) -> (15,15)
	
	// ISLAND COORDS to ACTUAL COORDS
	// [x,z]: (width*x,width*z) -> (width*(x+1)-1,width*(z+1)-1)
	// [0,0]: (0,0) -> (255,255)
	
	@Deprecated
	public Island(){
		//required for Mongo instantiation
	}
	
	public Island(String serverName, String id, String name, Player owner, int x, int z){
		put("serverName", serverName);
		put("id", id);
		put("name", name);
		put("x", x);
		put("z", z);
		
		put("ownerUUID", owner.getUniqueId().toString());
		put("members", new ArrayList<>(Collections.singletonList(owner.getUniqueId().toString())));
		
		put("homeX", 0.0);
		put("homeY", 0.0);
		put("homeZ", 0.0);
		put("homeYaw", 0.0);
		put("homePitch", 0.0);
	}
	
	public String getServerName(){
		return getString("serverName");
	}
	
	public String getID(){
		return getString("id");
	}
	
	public String getName(){
		return getString("name");
	}
	
	public String getOwnerUUID(){
		return getString("ownerUUID");
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getMemberUUIDs(){
		Object obj = get("members");
		if(obj instanceof List){
			return (List<String>)obj;
		}else{
			put("members", new ArrayList<>());
			return getMemberUUIDs();
		}
	}
	
	public void addMember(String uuid){
		List<String> members = getMemberUUIDs();
		members.add(uuid);
		put("members", members);
	}
	
	public void removeMember(String uuid){
		List<String> members = getMemberUUIDs();
		members.remove(uuid);
		put("members", members);
	}
	
	public int getX(){
		return getInt("x");
	}
	
	public int getZ(){
		return getInt("z");
	}
	
	public int getChunkX1(){
		return 16*getX();
	}
	
	public int getChunkZ1(){
		return 16*getZ();
	}
	
	public int getChunkX2(){
		return 16*getX() + WIDTH_IN_CHUNKS - 1;
	}
	
	public int getChunkZ2(){
		return 16*getZ() + WIDTH_IN_CHUNKS - 1;
	}
	
	public int getActualX1(){
		return WIDTH*getX();
	}
	
	public int getActualZ1(){
		return WIDTH*getZ();
	}
	
	public int getActualX2(){
		return WIDTH*(getX()+1) - 1;
	}
	
	public int getActualZ2(){
		return WIDTH*(getZ()+1) - 1;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getMembers(){
		Object obj = get("members");
		if(obj instanceof HashMap){
			return (List<String>)obj;
		}else{
			put("members", new ArrayList<>());
			return getMembers();
		}
	}
	
	public boolean isMember(String uuid){
		return getMembers().contains(uuid);
	}
	
	public static int islandToChunk(int island){
		return (16*island)+WIDTH_IN_CHUNKS-1;
	}
	
	public static int chunkToIsland(int chunk){
		return (int)Math.ceil((double)(chunk-WIDTH_IN_CHUNKS+1)/16);
	}
	
	public static int islandToActual(int island){
		return WIDTH*(island+1)-1;
	}
	
	public static int actualToIsland(int actual){
		return (int)Math.ceil((double)(actual+1)/WIDTH-1);
	}
	
}
