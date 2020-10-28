package com.xenry.stagecraft.survival.profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.survival.Survival;
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
public final class SurvivalProfileManager extends Manager<Survival> {
	
	private final DBCollection collection;
	private final HashMap<String,SurvivalProfile> profiles;
	
	public SurvivalProfileManager(Survival survival){
		super("Survival Profiles", survival);
		profiles = new HashMap<>();
		collection = plugin.getCore().getMongoManager().getCoreCollection("survivalProfiles");
		collection.setObjectClass(SurvivalProfile.class);
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
			SurvivalProfile profile = (SurvivalProfile)c.next();
			profiles.put(profile.getUUID(), profile);
		}
	}
	
	public void save(SurvivalProfile profile){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(profile));
	}
	
	public void saveAll(){
		for(SurvivalProfile profile : profiles.values()){
			save(profile);
		}
	}
	
	public void saveSync(SurvivalProfile profile){
		collection.save(profile);
	}
	
	public void saveAllSync(){
		for(SurvivalProfile profile : profiles.values()){
			saveSync(profile);
		}
	}
	
	@Nullable
	public SurvivalProfile getProfile(@Nullable Player p){
		if(p == null){
			return null;
		}
		return profiles.getOrDefault(p.getUniqueId().toString(), null);
	}
	
	public List<SurvivalProfile> getOnlineProfiles(){
		List<SurvivalProfile> online = new ArrayList<>();
		for(SurvivalProfile prof : profiles.values()){
			if(prof.isOnline()){
				online.add(prof);
			}
		}
		return online;
	}
	
	public List<SurvivalProfile> getAllProfiles(){
		return new ArrayList<>(profiles.values());
	}
	
	@Nullable
	public SurvivalProfile getProfileByLatestUsername(String username){
		Profile coreProfile = getCore().getProfileManager().getProfileByLatestUsername(username);
		if(coreProfile == null){
			return null;
		}
		return getProfileByUUID(coreProfile.getUUID());
	}
	
	@Nullable
	public SurvivalProfile getProfileByUUID(String uuid){
		if(profiles.containsKey(uuid)) {
			return profiles.get(uuid);
		}
		return (SurvivalProfile) collection.findOne(new BasicDBObject("uuid", uuid));
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
		SurvivalProfile profile = (SurvivalProfile) collection.findOne(new BasicDBObject("uuid", player.getUniqueId().toString()));
		if(profile == null){
			profile = new SurvivalProfile(player);
			profiles.put(profile.getUUID(), profile);
			save(profile);
		}
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
		SurvivalProfile profile = getProfile(player);
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