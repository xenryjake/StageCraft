package com.xenry.stagecraft.skyblock.island;
import com.mongodb.BasicDBObject;
import org.bukkit.entity.Player;

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
public class Island extends BasicDBObject {
	
	public static final int WIDTH = 256;
	
	// ISLAND COORDS to ACTUAL COORDS
	// [x,z]: (16x,16z) -> (16x+size-1,16z+size-1)
	
	@Deprecated
	public Island(){
		//required for Mongo instantiation
	}
	
	public Island(String serverName, Player owner, int x, int z){
		put("serverName", serverName);
		put("ownerUUID", owner.getUniqueId().toString());
		put("x", x);
		put("z", z);
		
		put("members", new ArrayList<String>());
	}
	
	public String getServerName(){
		return getString("serverName");
	}
	
	public String getID(){
		return getX() + "," + getZ();
	}
	
	public int getX(){
		return getInt("x");
	}
	
	public int getZ(){
		return getInt("z");
	}
	
	public int getActualX1(){
		return 16*getX();
	}
	
	public int getActualZ1(){
		return 16*getZ();
	}
	
	public int getActualX2(){
		return 16*getX() + WIDTH - 1;
	}
	
	public int getActualZ2(){
		return 16*getZ() + WIDTH - 1;
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
	
}
