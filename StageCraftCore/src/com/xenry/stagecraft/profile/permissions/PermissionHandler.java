package com.xenry.stagecraft.profile.permissions;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.ProfileRankChangeEvent;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PermissionHandler extends Handler<Core,ProfileManager> {
	
	private final HashMap<String,PermissionAttachment> attachments;
	private final HashMap<String,PermissionSet> permissionSets;
	
	public PermissionHandler(ProfileManager manager) {
		super(manager);
		attachments = new HashMap<>();
		permissionSets = new HashMap<>();
		{
			PermissionSet perms = new RankPermissionSet("global:MEMBER", Rank.MEMBER);
			perms.set("stagecraft.rank.member", true);
			perms.set("essentials.afk", true);
			perms.set("essentials.afk.auto", true);
			perms.set("essentials.afk.message", true);
			registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("global:PREMIUM", Rank.PREMIUM);
			perms.set("stagecraft.rank.premium", true);
			registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("global:MOD", Rank.MOD);
			perms.set("stagecraft.rank.mod", true);
			perms.set("minecraft.commands.whitelist", true);
			registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("global:PREMIUM_MOD", Rank.PREMIUM_MOD);
			perms.set("stagecraft.rank.premium_mod", true);
			registerPermissionSet(perms);
		}{
			PermissionSet perms = new RankPermissionSet("global:ADMIN", Rank.ADMIN);
			perms.set("stagecraft.rank.admin", true);
			registerPermissionSet(perms);
		}{
			PermissionSet perms = new PlayerPermissionSet("global:@xenryjake", "fef89265-10d1-41d0-88f7-1ddf2f9e30ec", "xenryjake");
			perms.set("bukkit.command.op", true);
			perms.set("minecraft.command.deop", true);
			perms.set("minecraft.command.op", true);
			registerPermissionSet(perms);
		}
	}
	
	public void registerPermissionSet(PermissionSet set){
		if(getPermissionSet(set.name) != null){
			throw new IllegalArgumentException("A PermissionSet with this name already exists.");
		}
		permissionSets.put(set.name, set);
	}
	
	@Nullable
	public PermissionSet getPermissionSet(String name){
		PermissionSet permissionSet = permissionSets.getOrDefault(name, null);
		if(permissionSet != null){
			return permissionSet;
		}
		for(PermissionSet set : permissionSets.values()){
			if(set.name.equalsIgnoreCase(name)){
				return set;
			}
		}
		return null;
	}
	
	public List<String> getPermissionSetNames(){
		return new ArrayList<>(permissionSets.keySet());
	}
	
	public void assignPermissions(@NotNull Player player){
		Profile profile = manager.getProfile(player);
		if(profile == null){
			return;
		}
		clearPermissions(player);
		PermissionAttachment attachment = player.addAttachment(manager.plugin);
		for(PermissionSet set : permissionSets.values()){
			if(set.appliesTo(profile)){
				for(Map.Entry<String,Boolean> perm : set.getPermissions().entrySet()){
					attachment.setPermission(perm.getKey(), perm.getValue());
				}
			}
		}
		attachments.put(player.getUniqueId().toString(), attachment);
	}
	
	public void clearPermissions(@NotNull Player player){
		PermissionAttachment attachment = attachments.get(player.getUniqueId().toString());
		if(attachment != null){
			player.removeAttachment(attachment);
		}
		attachments.remove(player.getUniqueId().toString());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		assignPermissions(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		clearPermissions(event.getPlayer());
	}
	
	@EventHandler
	public void onRankChange(ProfileRankChangeEvent event){
		Player player = event.getProfile().getPlayer();
		if(player != null){
			assignPermissions(player);
		}
	}
	
}
