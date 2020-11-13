package com.xenry.stagecraft.profile.permissions;
import com.xenry.stagecraft.profile.Profile;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerPermissionSet extends PermissionSet {
	
	public final String uuid;
	public final String playerName;
	
	public PlayerPermissionSet(String name, String uuid, String playerName) {
		super(name);
		this.uuid = uuid;
		this.playerName = playerName;
	}
	
	public PlayerPermissionSet(String name, Profile profile){
		this(name, profile.getUUID(), profile.getLatestUsername());
	}
	
	public PlayerPermissionSet(String name, Player player){
		this(name, player.getUniqueId().toString(), player.getName());
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public boolean appliesTo(String uuid){
		return uuid.equals(this.uuid);
	}
	
	public boolean appliesTo(Player player){
		return player.getUniqueId().toString().equals(uuid);
	}
	
	@Override
	public boolean appliesTo(Profile profile) {
		return profile.getUUID().equals(uuid);
	}
	
	@Override
	public String getAccessName() {
		return playerName;
	}
	
}
