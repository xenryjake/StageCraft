package com.xenry.stagecraft.survival.builder;
import com.mongodb.BasicDBObject;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class BuildArea extends BasicDBObject {
	
	@Deprecated
	public BuildArea(){
		//required for Mongo instantiation
	}
	
	public BuildArea(String name, String world, int ax, int az, int bx, int bz){
		put("name", name);
		put("world", world);
		put("ax", ax);
		put("az", az);
		put("bx", bx);
		put("bz", bz);
		
		put("nbi", false);
	}
	
	public String getName() {
		return getString("name");
	}
	
	public String getWorld() {
		return getString("world");
	}
	
	public int getAX() {
		return getInt("ax");
	}
	
	public int getAZ() {
		return getInt("az");
	}
	
	public int getBX() {
		return getInt("bx");
	}
	
	public int getBZ() {
		return getInt("bz");
	}
	
	public boolean allowsNonBuilderInteraction(){
		Object obj = get("nbi");
		if(obj instanceof Boolean){
			return (Boolean)obj;
		}else{
			put("nbi", false);
			return allowsNonBuilderInteraction();
		}
	}
	
	public void setName(String name) {
		put("name", name);
	}
	
	public void setWorld(String world) {
		put("world", world);
	}
	
	public void setAX(int ax) {
		put("ax", ax);
	}
	
	public void setAZ(int az) {
		put("az", az);
	}
	
	public void setBX(int bx) {
		put("bx", bx);
	}
	
	public void setBZ(int bz) {
		put("bz", bz);
	}
	
	public void setAllowNonBuilderInteraction(boolean allow){
		put("nbi", allow);
	}
	
}
