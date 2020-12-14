package com.xenry.stagecraft.creative.heads;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.commands.HeadCommand;
import com.xenry.stagecraft.creative.heads.commands.PlayerHeadCommand;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HeadsManager extends Manager<Creative> {
	
	private final DBCollection favoritesCollection;
	private final HashMap<String,PlayerFavorites> playerFavorites;
	
	private final List<Head> heads;
	private final List<String> allTags;
	private final List<String> favoriteUpdates;
	
	public HeadsManager(Creative plugin) {
		super("Heads", plugin);
		favoritesCollection = plugin.getCore().getMongoManager().getCoreCollection("playerFavoriteHeads");
		favoritesCollection.setObjectClass(PlayerFavorites.class);
		playerFavorites = new HashMap<>();
		heads = new ArrayList<>();
		allTags = new ArrayList<>();
		favoriteUpdates = new ArrayList<>();
	}
	
	@Override
	protected void onEnable() {
		setupHeads();
		downloadFavorites();
		
		registerCommand(new PlayerHeadCommand(this));
		registerCommand(new HeadCommand(this));
	}
	
	public void downloadFavorites(){
		Log.debug("download favorites");
		playerFavorites.clear();
		DBCursor c = favoritesCollection.find();
		while(c.hasNext()){
			PlayerFavorites favorites = (PlayerFavorites)c.next();
			Log.debug("found favorite for uuid: " + favorites.getUUID());
			playerFavorites.put(favorites.getUUID(), favorites);
		}
	}
	
	public void saveFavorites(PlayerFavorites favorites){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> favoritesCollection.save(favorites));
	}
	
	public void saveAllFavorites(){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for(PlayerFavorites favorites : playerFavorites.values()){
				favoritesCollection.save(favorites);
			}
		});
	}
	
	public void saveFavoritesSync(PlayerFavorites favorites){
		favoritesCollection.save(favorites);
	}
	
	public void saveAllFavoritesSync(){
		for(PlayerFavorites favorites : playerFavorites.values()){
			favoritesCollection.save(favorites);
		}
	}
	
	public boolean hasFavoriteUpdate(String uuid){
		return favoriteUpdates.contains(uuid);
	}
	
	public boolean hasFavoriteUpdate(HumanEntity player){
		return hasFavoriteUpdate(player.getUniqueId().toString());
	}
	
	public void addFavoriteUpdate(String uuid){
		favoriteUpdates.add(uuid);
	}
	
	public void addFavoriteUpdate(HumanEntity player){
		addFavoriteUpdate(player.getUniqueId().toString());
	}
	
	public void removeFavoriteUpdate(String uuid){
		favoriteUpdates.remove(uuid);
	}
	
	public void removeFavoriteUpdate(HumanEntity player){
		removeFavoriteUpdate(player.getUniqueId().toString());
	}
	
	public PlayerFavorites getPlayerFavorites(String uuid){
		PlayerFavorites favorites = playerFavorites.getOrDefault(uuid, null);
		if(favorites == null){
			playerFavorites.put(uuid, new PlayerFavorites(uuid));
			return getPlayerFavorites(uuid);
		}
		return favorites;
	}
	
	public PlayerFavorites getPlayerFavorites(HumanEntity player){
		return getPlayerFavorites(player.getUniqueId().toString());
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
		
		for(Head head : heads){
			for(String tag : head.getTags()){
				if(!allTags.contains(tag)){
					allTags.add(tag);
				}
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
	
	public List<String> getAllTags() {
		return allTags;
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
	
	public int countHeads(){
		return heads.size();
	}
	
	public int countHeads(Head.Category category){
		int i = 0;
		for(Head head : this.heads){
			if(head.getCategory() == category){
				i++;
			}
		}
		return i;
	}
	
	public int countHeads(String tag){
		int i = 0;
		for(Head head : this.heads){
			if(head.getTags().contains(tag)){
				i++;
			}
		}
		return i;
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
	
	@Nullable
	public String getTag(String tag){
		for(String string : allTags){
			if(tag.equalsIgnoreCase(string)){
				return string;
			}
		}
		return null;
	}
	
	public Button getSpawnHeadButton(Head head, boolean favorite){
		ItemStack is = head.getItem();
		ItemMeta im = is.getItemMeta();
		if(favorite){
			im.setDisplayName("§c⭐§r " + im.getDisplayName());
		}
		im.setLore(Arrays.asList("§fTags: " + Joiner.on(", ").join(head.getTags()),
				"§7Left-click to spawn this head",
				"§7Right-click to " + (favorite ? "remove from" : "add to") +  " your favorites"));
		is.setItemMeta(im);
		return new Button(is, (event) -> {
			HumanEntity player = event.getWhoClicked();
			if(event.isLeftClick()){
				ItemStack item = head.getItem();
				if(event.isShiftClick()){
					item.setAmount(item.getMaxStackSize());
				}
				player.getInventory().addItem(item);
			}else if(event.isRightClick()){
				PlayerFavorites favorites = getPlayerFavorites(player);
				if(favorite){
					favorites.removeFavorite(head.id);
				}else{
					favorites.addFavorite(head.id);
				}
				saveFavorites(favorites);
				addFavoriteUpdate(player);
				player.sendMessage(M.msg + (favorite ? "Removed " : "Added ") + M.elm + head.name + M.msg + " as a favorite.");
			}
		});
	}
	
}
