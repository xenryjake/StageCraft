package com.xenry.stagecraft.skyblock.island;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.util.LocationVector;
import com.xenry.stagecraft.util.NumberUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
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
	public static final int BUILD_BUFFER = 16;
	
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
	
	public Island(IslandManager manager, String id, Player owner, int x, int z){
		put("serverName", manager.getCore().getServerName());
		put("id", id);
		put("name", owner.getName() + "'s Island");
		put("x", x);
		put("z", z);
		
		put("ownerUUID", owner.getUniqueId().toString());
		put("members", new ArrayList<>(Collections.singletonList(owner.getUniqueId().toString())));
		
		put("homeX", getActualX1() + manager.getSchematicHandler().getMainIslandSpawnX());
		put("homeY", manager.getSchematicHandler().getMainIslandSpawnY());
		put("homeZ", getActualZ1() + manager.getSchematicHandler().getMainIslandSpawnZ());
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
	
	public void setName(String name){
		put("name", name);
	}
	
	public String getOwnerUUID(){
		return getString("ownerUUID");
	}
	
	public void setOwnerUUID(String ownerUUID){
		put("ownerUUID", ownerUUID);
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
	
	public boolean isMember(String uuid){
		return getMemberUUIDs().contains(uuid);
	}
	
	public boolean isMember(Player player){
		return isMember(player.getUniqueId().toString());
	}
	
	public boolean isMember(GenericProfile profile){
		return isMember(profile.getUUID());
	}
	
	public LocationVector getHomeVector(){
		Object objX = get("homeX");
		Object objY = get("homeY");
		Object objZ = get("homeZ");
		Object objYaw = get("homeYaw");
		Object objPitch = get("homePitch");
		double x;
		if(objX instanceof Double){
			x = (Double)objX;
		}else{
			put("homeX", 0);
			x = 0;
		}
		double y;
		if(objY instanceof Double){
			y = (Double)objY;
		}else{
			put("homeY", 0);
			y = 0;
		}
		double z;
		if(objZ instanceof Double){
			z = (Double)objZ;
		}else{
			put("homeZ", 0);
			z = 0;
		}
		double yaw;
		if(objYaw instanceof Double){
			yaw = (Double)objYaw;
		}else{
			put("homeYaw", 0);
			yaw = 0;
		}
		double pitch;
		if(objPitch instanceof Double){
			pitch = (Double)objPitch;
		}else{
			put("homePitch", 0);
			pitch = 0;
		}
		return new LocationVector(x, y, z, (float)yaw, (float)pitch);
	}
	
	public void setHome(LocationVector vector){
		put("homeX", vector.x);
		put("homeY", vector.y);
		put("homeZ", vector.z);
		put("homeYaw", vector.yaw);
		put("homePitch", vector.pitch);
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
	
	public boolean isInIsland(int x, int z){
		return NumberUtil.isIntWithin(x, getActualX1(), getActualX2()) && NumberUtil.isIntWithin(z, getActualZ1(), getActualZ2());
	}
	
	public boolean isInIsland(Location location){
		return isInIsland(location.getBlockX(), location.getBlockZ());
	}
	
	public boolean isInBuildArea(int x, int z){
		return NumberUtil.isIntWithin(x, getActualX1() + BUILD_BUFFER, getActualX2() - BUILD_BUFFER) && NumberUtil.isIntWithin(z, getActualZ1() + BUILD_BUFFER, getActualZ2() - BUILD_BUFFER);
	}
	
	public boolean isInBuildArea(Location location){
		return isInBuildArea(location.getBlockX(), location.getBlockZ());
	}
	
	public boolean isInBufferZone(int x, int z){
		return isInIsland(x, z) && !isInBuildArea(x, z);
	}
	
	public boolean isInBufferZone(Location location){
		return isInBufferZone(location.getBlockX(), location.getBlockZ());
	}
	
	public static int islandToChunk1(int island){
		return 16*island;
	}
	
	public static int islandToChunk2(int island){
		return (16*island)+WIDTH_IN_CHUNKS-1;
	}
	
	public static int chunkToIsland(int chunk){
		return (int)Math.ceil((double)(chunk-WIDTH_IN_CHUNKS+1)/16);
	}
	
	public static int islandToActual1(int island){
		return WIDTH*island;
	}
	
	public static int islandToActual2(int island){
		return WIDTH*(island+1)-1;
	}
	
	public static int actualToIsland(int actual){
		return (int)Math.ceil((double)(actual+1)/WIDTH-1);
	}
	
}
