package com.xenry.stagecraft.skyblock.profile;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.util.Vector3DInt;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SkyBlockProfile extends GenericProfile {
	
	@Deprecated
	public SkyBlockProfile(){
		//required for Mongo instantiation
	}
	
	public SkyBlockProfile(Player player){
		super(player);
		
		put("activeIslandID", "none");
		
		put("lastLocationX", 0);
		put("lastLocationY", 0);
		put("lastLocationZ", 0);
		put("lastLocationWorldName", "world");
	}
	
	public String getActiveIslandID(){
		return getString("activeIslandID");
	}
	
	public Vector3DInt getLastLocation(){
		Object objX = get("lastLocationX");
		Object objY = get("lastLocationY");
		Object objZ = get("lastLocationZ");
		int x;
		if(objX instanceof Integer){
			x = (Integer)objX;
		}else{
			put("lastLocationX", 0);
			x = 0;
		}
		int y;
		if(objY instanceof Integer){
			y = (Integer)objY;
		}else{
			put("lastLocationY", 0);
			y = 0;
		}
		int z;
		if(objZ instanceof Integer){
			z = (Integer)objZ;
		}else{
			put("lastLocationZ", 0);
			z = 0;
		}
		return new Vector3DInt(x, y, z);
	}
	
	public String getLastLocationWorldName(){
		Object obj = get("lastLocationWorldName");
		if(obj instanceof String){
			return (String)obj;
		}else{
			put("lastLocationWorldName", "world");
			return getLastLocationWorldName();
		}
	}
	
	public void setLastLocation(int x, int y, int z){
		put("lastLocationX", x);
		put("lastLocationY", y);
		put("lastLocationZ", z);
	}
	
	public void setLastLocationWorldName(String name){
		put("lastLocationWorldName", name);
	}
	
}
