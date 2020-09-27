package com.xenry.stagecraft.profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.profile.commands.*;
import com.xenry.stagecraft.profile.commands.rank.RankCommand;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.util.time.TimeUtil;
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
 * StageCraft created by Henry Blasingame (Xenry) on 5/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProfileManager extends Manager<Core> {
	
	private final DBCollection profileCollection;
	private final HashMap<String,Profile> profiles;
	
	public ProfileManager(Core plugin){
		super("Profile", plugin);
		profiles = new HashMap<>();
		profileCollection = plugin.getMongoManager().getCoreCollection("profiles");
		profileCollection.setObjectClass(Profile.class);
	}
	
	@Override
	protected void onEnable() {
		registerCommand(new RankCommand(this));
		registerCommand(new LookupCommand(this));
		registerCommand(new SeenCommand(this));
		registerCommand(new NickCommand(this));
		registerCommand(new DownloadProfilesCommand(this));
		registerCommand(new PlaytimeCommand(this));
		registerCommand(new MeCommand(this));
		registerCommand(new ProfileListCommand(this));
		
		downloadProfiles();
	}
	
	@Override
	protected void onDisable() {
		saveAllSync();
	}
	
	public void downloadProfiles(){
		profiles.clear();
		DBCursor c = profileCollection.find();
		while(c.hasNext()){
			Profile profile = (Profile)c.next();
			profiles.put(profile.getUUID(), profile);
		}
	}
	
	public void save(Profile profile){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> profileCollection.save(profile));
	}
	
	public void saveAll(){
		for(Profile profile : profiles.values()){
			save(profile);
		}
	}
	
	public void saveSync(Profile profile){
		profileCollection.save(profile);
	}
	
	public void saveAllSync(){
		for(Profile profile : profiles.values()){
			saveSync(profile);
		}
	}
	
	@Nullable
	public Profile getProfile(@Nullable Player p){
		if(p == null){
			return null;
		}
		return profiles.getOrDefault(p.getUniqueId().toString(), null);
	}
	
	public List<Profile> getOnlineProfiles(){
		List<Profile> online = new ArrayList<>();
		for(Profile prof : profiles.values()){
			if(prof.isOnline()){
				online.add(prof);
			}
		}
		return online;
	}
	
	public List<Profile> getAllProfiles(){
		return new ArrayList<>(profiles.values());
	}
	
	public List<String> getAllProfileLatestUsernames(){
		List<String> names = new ArrayList<>();
		for(Profile prof : profiles.values()){
			names.add(prof.getLatestUsername());
		}
		return names;
	}
	
	@Nullable
	public Profile getProfileByLatestUsername(String username){
		for(Profile profile : profiles.values()){
			if(profile.getLatestUsername().equalsIgnoreCase(username)){
				return profile;
			}
		}
		return (Profile) profileCollection.findOne(new BasicDBObject("latestUsername", username));
	}
	
	@Nullable
	public Profile getProfileByUUID(String uuid){
		if(profiles.containsKey(uuid)) {
			return profiles.get(uuid);
		}
		return (Profile) profileCollection.findOne(new BasicDBObject("uuid", uuid));
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
		//Profile profile = (Profile) profileCollection.findOne(new BasicDBObject("uuid", player.getUniqueId().toString()));
		Profile profile = getProfile(player);
		if(profile == null){
			profile = new Profile(player);
			profiles.put(profile.getUUID(), profile);
		}
		profile.updateUsernames(player.getName());
		profile.updateAddresses(player.getAddress());
		save(profile);
		
		Punishment ban = getCore().getPunishmentManager().getOutstandingPunishment(profile, Punishment.Type.BAN);
		if(ban != null){
			event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
			event.setKickMessage(ban.getMessage());
		}
		
		//ProfileLoadEvent profileLoadEvent = new ProfileLoadEvent(profile);
		//plugin.getServer().getPluginManager().callEvent(profileLoadEvent);
		
		//profile.updateDisplayName(player);
		//});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Profile profile = getProfile(player);
		if(profile != null){
			profile.updateDisplayName();
			profile.setLastLogin(TimeUtil.nowSeconds());
		}
		
		player.setGameMode(GameMode.SURVIVAL);
		player.setPlayerListHeader("§9§lStage§a§lCraft");
		event.setJoinMessage(" §a+§7 " + player.getDisplayName());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Profile profile = getProfile(player);
		if(profile == null) {
			return;
		}
		profile.setLastLogout(TimeUtil.nowSeconds());
		Location loc = player.getLocation();
		World world = loc.getWorld();
		if(world != null) {
			profile.setLastLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			profile.setLastLocationWorldName(world.getName());
		}
		profile.updateTotalPlaytime();
		save(profile);
		//profiles.remove(profile.getUUID());
		event.setQuitMessage(" §c-§7 " + player.getDisplayName());
	}
	
}
