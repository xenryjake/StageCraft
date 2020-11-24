package com.xenry.stagecraft.creative.profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.permissions.PermissionSet;
import com.xenry.stagecraft.profile.permissions.RankPermissionSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
		setupPermissions();
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
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for(CreativeProfile profile : profiles.values()){
				collection.save(profile);
			}
		});
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
	
	private void setupPermissions(){
		{
			PermissionSet perms = new RankPermissionSet("creative:MEMBER", Rank.MEMBER);
			perms.set("essentials.hat.prevent-type.*", false);
			perms.set("essentials.hat", true);
			perms.set("essentials.condense", true);
			perms.set("griefprevention.buysellclaimblocks", false);
			perms.set("griefprevention.ignore", false);
			perms.set("griefprevention.siege", false);
			perms.set("griefprevention.claimbook", true);
			perms.set("griefprevention.claims", true);
			perms.set("griefprevention.createclaims", true);
			perms.set("griefprevention.givepet", true);
			perms.set("griefprevention.lava", true);
			perms.set("griefprevention.trapped", true);
			perms.set("griefprevention.visualizenearbyclaims", true);
			perms.set("griefprevention.seeclaimsize", true);
			perms.set("griefprevention.seeinactivity", true);
			plugin.getCore().getProfileManager().getPermissionsHandler().registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("creative:PREMIUM", Rank.PREMIUM);
			perms.set("worldedit.navigation.ascend", true);
			perms.set("worldedit.navigation.descend", true);
			perms.set("worldedit.navigation.thru.command", true);
			perms.set("worldedit.navigation.jumpto.command", true);
			plugin.getCore().getProfileManager().getPermissionsHandler().registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("creative:MOD", Rank.MOD);
			perms.set("griefprevention.claimslistother", true);
			perms.set("griefprevention.deleteclaims", true);
			perms.set("griefprevention.ignoreclaims", true);
			perms.set("essentials.invsee", true);
			perms.set("essentials.invsee.equip", true);
			plugin.getCore().getProfileManager().getPermissionsHandler().registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("creative:HEAD_MOD", Rank.HEAD_MOD);
			perms.set("essentials.invsee.modify", true);
			plugin.getCore().getProfileManager().getPermissionsHandler().registerPermissionSet(perms);
		}
	}
	
}
