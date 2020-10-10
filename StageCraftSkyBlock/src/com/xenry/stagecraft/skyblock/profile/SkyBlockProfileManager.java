package com.xenry.stagecraft.skyblock.profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.skyblock.SkyBlock;
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
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SkyBlockProfileManager extends Manager<SkyBlock> {
	
	private final DBCollection collection;
	private final HashMap<String,SkyBlockProfile> profiles;
	
	public SkyBlockProfileManager(SkyBlock skyBlock){
		super("SkyBlock Profiles", skyBlock);
		collection = getCore().getMongoManager().getCoreCollection("skyBlockProfiles");
		collection.setObjectClass(SkyBlockProfile.class);
		profiles = new HashMap<>();
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
			SkyBlockProfile profile = (SkyBlockProfile)c.next();
			profiles.put(profile.getUUID(), profile);
		}
	}
	
	public void save(SkyBlockProfile profile){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(profile));
	}
	
	public void saveAll(){
		for(SkyBlockProfile profile : profiles.values()){
			save(profile);
		}
	}
	
	public void saveSync(SkyBlockProfile profile){
		collection.save(profile);
	}
	
	public void saveAllSync(){
		for(SkyBlockProfile profile : profiles.values()){
			saveSync(profile);
		}
	}
	
	@Nullable
	public SkyBlockProfile getProfile(@Nullable Player p){
		if(p == null){
			return null;
		}
		return profiles.getOrDefault(p.getUniqueId().toString(), null);
	}
	
	public List<SkyBlockProfile> getOnlineProfiles(){
		List<SkyBlockProfile> online = new ArrayList<>();
		for(SkyBlockProfile prof : profiles.values()){
			if(prof.isOnline()){
				online.add(prof);
			}
		}
		return online;
	}
	
	public List<SkyBlockProfile> getAllProfiles(){
		return new ArrayList<>(profiles.values());
	}
	
	@Nullable
	public SkyBlockProfile getProfileByLatestUsername(String username){
		Profile coreProfile = getCore().getProfileManager().getProfileByLatestUsername(username);
		if(coreProfile == null){
			return null;
		}
		return getProfileByUUID(coreProfile.getUUID());
	}
	
	@Nullable
	public SkyBlockProfile getProfileByUUID(String uuid){
		if(profiles.containsKey(uuid)) {
			return profiles.get(uuid);
		}
		return (SkyBlockProfile) collection.findOne(new BasicDBObject("uuid", uuid));
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
		SkyBlockProfile profile = getProfile(player);
		if(profile == null){
			profile = (SkyBlockProfile) collection.findOne(new BasicDBObject("uuid", player.getUniqueId().toString()));
		}
		if(profile == null){
			profile = new SkyBlockProfile(player);
			profiles.put(profile.getUUID(), profile);
		}
		save(profile);
		//});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setGameMode(GameMode.SURVIVAL);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		SkyBlockProfile profile = getProfile(player);
		if(profile == null) {
			return;
		}
		Location loc = player.getLocation();
		World world = loc.getWorld();
		if(world != null) {
			profile.setLastLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			profile.setLastLocationWorldName(world.getName());
		}
		save(profile);
		profiles.remove(profile.getUUID());
	}
	
}
