package com.xenry.stagecraft.creative.gameplay.heads;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.util.Log;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HeadHandler extends Handler<Creative,GameplayManager> {
	
	private final List<Head> heads;
	
	public HeadHandler(GameplayManager manager) {
		super(manager);
		heads = new ArrayList<>();
	}
	
	public void setupHeads(){
		heads.clear();
		heads.addAll(VanillaHead.values());
		for(MHFHead head : MHFHead.values()){
			heads.add(head.getHead());
		}
		
		for(Head.Category category : Head.Category.values()){
			if(!category.download){
				continue;
			}
			String json = "";
			try{
				URL url = new URL("https://minecraft-heads.com/scripts/api.php?cat=" + category.id + "&tags=true");
				URLConnection con = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				json = in.readLine();
				in.close();
			}catch(Exception ex){
				Log.warn("Failed to download heads for category: " + category.id);
			}
			try{
				List<GsonHead> gsonHeads = new Gson().fromJson(json, new TypeToken<List<GsonHead>>(){}.getType());
				for(GsonHead gsonHead : gsonHeads){
					try{
						heads.add(gsonHead.toCustomHead(category));
					}catch(Exception ignored){
						Log.debug("Invalid head from api: " + gsonHead.uuid);
					}
				}
			}catch(Exception exception){
				Log.warn("Failed to parse json data for category: " + category.id);
			}
		}
	}
	
	private static class GsonHead {
		
		@Nullable
		public String uuid, name, value, tags;
		
		public CustomHead toCustomHead(Head.Category category){
			if(uuid == null || name == null || value == null){
				throw new IllegalArgumentException("uuid, name, or value == null");
			}
			return new CustomHead(uuid, name, value, category, tags == null ? new String[0] : tags.split(","));
		}
		
	}
	
	public List<Head> getAllHeads() {
		return heads;
	}
	
	@Nullable
	public Head getHead(String id){
		for(Head head : heads){
			if(head.getID().equals(id)){
				return head;
			}
		}
		return null;
	}
	
	public List<Head> getHeadsByCategory(Head.Category category){
		List<Head> heads = new ArrayList<>();
		for(Head head : this.heads){
			if(head.getCategory() == category){
				heads.add(head);
			}
		}
		return heads;
	}
	
	public List<Head> getHeadsByTag(String tag){
		List<Head> heads = new ArrayList<>();
		for(Head head : this.heads){
			if(head.getTags().contains(tag)){
				heads.add(head);
			}
		}
		return heads;
	}
	
}
