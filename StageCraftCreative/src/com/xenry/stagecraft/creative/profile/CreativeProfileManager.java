package com.xenry.stagecraft.creative.profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class CreativeProfileManager extends Manager<Creative> {
	
	private final DBCollection collection;
	private final HashMap<String,CreativeProfile> profiles;
	
	public CreativeProfileManager(Creative survival){
		super("Creative Profiles", survival);
		profiles = new HashMap<>();
		collection = plugin.getCore().getMongoManager().getCoreCollection("creativeProfiles");
		collection.setObjectClass(CreativeProfile.class);
	}
	
	@Override
	protected void onEnable() {
		downloadProfiles();
	}
	
	@Override
	protected void onDisable() {
		saveAllSync();
	}
	
	public void downloadProfiles(){
		profiles.clear();
		DBCursor c = collection.find();
		while(c.hasNext()){
			CreativeProfile profile = (CreativeProfile)c.next();
			profiles.put(profile.getUUID(), profile);
		}
	}
	
	public void save(CreativeProfile profile){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(profile));
	}
	
	public void saveAll(){
		for(CreativeProfile profile : profiles.values()){
			save(profile);
		}
	}
	
	public void saveSync(CreativeProfile profile){
		collection.save(profile);
	}
	
	public void saveAllSync(){
		for(CreativeProfile profile : profiles.values()){
			saveSync(profile);
		}
	}
	
	@Nullable
	public CreativeProfile getProfile(@Nullable Player p){
		if(p == null){
			return null;
		}
		return profiles.getOrDefault(p.getUniqueId().toString(), null);
	}
	
	public List<CreativeProfile> getOnlineProfiles(){
		List<CreativeProfile> online = new ArrayList<>();
		for(CreativeProfile prof : profiles.values()){
			if(prof.isOnline()){
				online.add(prof);
			}
		}
		return online;
	}
	
	public List<CreativeProfile> getAllProfiles(){
		return new ArrayList<>(profiles.values());
	}
	
	@Nullable
	public CreativeProfile getProfileByLatestUsername(String username){
		Profile coreProfile = getCore().getProfileManager().getProfileByLatestUsername(username);
		if(coreProfile == null){
			return null;
		}
		return getProfileByUUID(coreProfile.getUUID());
	}
	
	@Nullable
	public CreativeProfile getProfileByUUID(String uuid){
		if(profiles.containsKey(uuid)) {
			return profiles.get(uuid);
		}
		return (CreativeProfile) collection.findOne(new BasicDBObject("uuid", uuid));
	}
	
	public boolean hasOfflineProfile(String uuid){
		return getProfileByUUID(uuid) != null;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(final PlayerLoginEvent event){
		if(event.getResult() != PlayerLoginEvent.Result.ALLOWED){
			return;
		}
		final Player player = event.getPlayer();
		//Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
		CreativeProfile profile = (CreativeProfile) collection.findOne(new BasicDBObject("uuid", player.getUniqueId().toString()));
		if(profile == null){
			profile = new CreativeProfile(player);
			profiles.put(profile.getUUID(), profile);
			save(profile);
		}
		//});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		CreativeProfile profile = getProfile(player);
		if(profile == null) {
			return;
		}
		Location loc = player.getLocation();
		World world = loc.getWorld();
		if(world != null) {
			profile.setLastLocation(loc);
		}
		save(profile);
		profiles.remove(profile.getUUID());
	}
	
}
