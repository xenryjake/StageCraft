package com.xenry.stagecraft.survival.profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.permissions.PermissionSet;
import com.xenry.stagecraft.profile.permissions.RankPermissionSet;
import com.xenry.stagecraft.survival.Survival;
import org.bukkit.Bukkit;
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
			SurvivalProfile profile = (SurvivalProfile)c.next();
			profiles.put(profile.getUUID(), profile);
		}
	}
	
	public void save(SurvivalProfile profile){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(profile));
	}
	
	public void saveAll(){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for(SurvivalProfile profile : profiles.values()){
				collection.save(profile);
			}
		});
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event){
		SurvivalProfile cp = getProfile(event.getPlayer());
		if(cp != null && !cp.hasAcceptedRules()){
			event.getPlayer().chat("/info");
		}
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
	
	private void setupPermissions(){
		{
			PermissionSet perms = new RankPermissionSet("survival:MEMBER", Rank.MEMBER);
			perms.set("com.cyber.mobheads.behead.fish", true);
			perms.set("com.cyber.mobheads.behead.mobs", true);
			perms.set("com.cyber.mobheads.behead.players", true);
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
			PermissionSet perms = new RankPermissionSet("survival:MOD", Rank.MOD);
			perms.set("dynmap.marker.add", true);
			perms.set("dynmap.marker.delete", true);
			perms.set("dynmap.marker.list", true);
			perms.set("griefprevention.claimslistother", true);
			plugin.getCore().getProfileManager().getPermissionsHandler().registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("survival:HEAD_MOD", Rank.HEAD_MOD);
			perms.set("griefprevention.ignoreclaims", true);
			perms.set("griefprevention.deleteclaims", true);
			perms.set("essentials.invsee", true);
			perms.set("essentials.invsee.equip", true);
			plugin.getCore().getProfileManager().getPermissionsHandler().registerPermissionSet(perms);
		}
	}
	
}
