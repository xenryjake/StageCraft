package com.xenry.stagecraft.creative.heads;
import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerFavorites extends BasicDBObject {
	
	@Deprecated
	public PlayerFavorites(){
		//required for Mongo instantiation
	}
	
	public PlayerFavorites(String uuid){
		put("uuid", uuid);
		put("favorites", new ArrayList<>());
	}
	
	public String getUUID(){
		return getString("uuid");
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getFavorites(){
		return (List<String>) get("favorites");
	}
	
	public boolean isFavorite(String id){
		return getFavorites().contains(id);
	}
	
	public void addFavorite(String id){
		List<String> favorites = getFavorites();
		favorites.add(id);
		put("favorites", favorites);
	}
	
	public void removeFavorite(String id){
		List<String> favorites = getFavorites();
		favorites.remove(id);
		put("favorites", favorites);
	}
	
}
