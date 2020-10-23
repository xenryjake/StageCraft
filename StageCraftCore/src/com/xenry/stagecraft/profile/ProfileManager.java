package com.xenry.stagecraft.profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.profile.commands.*;
import com.xenry.stagecraft.profile.commands.rank.RankCommand;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProfileManager extends Manager<Core> {
	
	private final DBCollection collection;
	private final HashMap<String,Profile> profiles;
	
	public ProfileManager(Core plugin){
		super("Profiles", plugin);
		profiles = new HashMap<>();
		collection = plugin.getMongoManager().getCoreCollection("profiles");
		collection.setObjectClass(Profile.class);
		
		GenericProfile.coreProfileManager = this;
	}
	
	@Override
	protected void onEnable() {
		registerCommand(new RankCommand(this));
		registerCommand(new LookupCommand(this));
		registerCommand(new SeenCommand(this));
		registerCommand(new NickCommand(this));
		registerCommand(new PlaytimeCommand(this));
		registerCommand(new MeCommand(this));
		registerCommand(new ProfileListCommand(this));
		
		//downloadProfiles();
	}
	
	@Override
	protected void onDisable() {
		saveAllSync();
	}
	
	/*public void downloadProfiles(){
		profiles.clear();
		DBCursor c = collection.find();
		while(c.hasNext()){
			Profile profile = (Profile)c.next();
			profiles.put(profile.getUUID(), profile);
		}
	}*/
	
	public void save(Profile profile){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(profile));
	}
	
	public void saveAll(){
		for(Profile profile : profiles.values()){
			save(profile);
		}
	}
	
	public void saveSync(Profile profile){
		collection.save(profile);
	}
	
	public void saveAllSync(){
		for(Profile profile : profiles.values()){
			saveSync(profile);
		}
	}
	
	@Nullable
	public Profile getProfile(@Nullable Player player){
		if(player == null){
			return null;
		}
		Profile profile = profiles.getOrDefault(player.getUniqueId().toString(), null);
		if(player.isOnline() && profile == null){
			Log.warn("Online player has no profile!");
		}
		return profile;
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
		return (Profile) collection.findOne(new BasicDBObject("latestUsername",
				Pattern.compile(Pattern.quote(username), Pattern.CASE_INSENSITIVE)));
	}
	
	@Nullable
	public Profile getProfileByUUID(String uuid){
		if(profiles.containsKey(uuid)) {
			return profiles.get(uuid);
		}
		return (Profile) collection.findOne(new BasicDBObject("uuid", uuid));
	}
	
	public boolean hasProfile(String uuid){
		return getProfileByUUID(uuid) != null;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(final AsyncPlayerPreLoginEvent event){
		if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED){
			return;
		}
		Profile profile = (Profile) collection.findOne(new BasicDBObject("uuid", event.getUniqueId().toString()));
		if(profile == null){
			profile = new Profile(event.getUniqueId(), event.getName(), event.getAddress());
		}
		profiles.put(profile.getUUID(), profile);
		Log.debug("Loaded profile for " + event.getName());
		
		Punishment ban = getCore().getPunishmentManager().getOutstandingPunishment(profile, Punishment.Type.BAN);
		if(ban != null){
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
			event.setKickMessage(ban.getMessage());
		}
	}
	
	/*@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(final PlayerLoginEvent event){
		if(event.getResult() != PlayerLoginEvent.Result.ALLOWED){
			return;
		}
		final Player player = event.getPlayer();
		//Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
		Profile profile = (Profile) collection.findOne(new BasicDBObject("uuid", player.getUniqueId().toString()));
		if(profile == null){
			profile = new Profile(player);
		}
		profiles.put(profile.getUUID(), profile);
		
		Punishment ban = getCore().getPunishmentManager().getOutstandingPunishment(profile, Punishment.Type.BAN);
		if(ban != null){
			event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
			event.setKickMessage(ban.getMessage());
		}
		//});
	}*/
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Profile profile = getProfile(player);
		//player.setPlayerListHeader("§9§lStage§a§lCraft");
		//player.setPlayerListFooter("§8§o" + plugin.getServerName());
		event.setJoinMessage(null);
		if(profile == null){
			player.kickPlayer(M.error("Your profile failed to load. Please contact an admin."));
			Log.warn("There is no profile for " + player.getName() + "!");
			return;
		}
		profile.updateUsernames(player.getName());
		profile.updateAddresses(player.getAddress());
		profile.updateDisplayName();
		profile.setLastLogin(plugin.getServerName(), TimeUtil.nowSeconds());
		//Bukkit.broadcastMessage(" §a+§7 " + profile.getDisplayName());
		Log.toCS("§a[JOIN] §7" + player.getName());
		save(profile);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Profile profile = getProfile(player);
		if(profile == null) {
			return;
		}
		profile.setLastLogout(plugin.getServerName(), TimeUtil.nowSeconds());
		profile.updatePlaytime(plugin.getServerName());
		save(profile);
		profiles.remove(profile.getUUID());
		event.setQuitMessage(null);
		//event.setQuitMessage(" §c-§7 " + player.getDisplayName());
		Log.toCS("§c[QUIT] §7" + player.getName());
	}
	
}
