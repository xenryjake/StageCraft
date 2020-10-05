package com.xenry.stagecraft.profile;
import com.mongodb.BasicDBObject;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class GenericProfile extends BasicDBObject {
	
	protected static ProfileManager coreProfileManager;
	
	@Deprecated
	public GenericProfile(){
		//required for Mongo instantiation
	}
	
	public GenericProfile(Player player){
		put("uuid", player.getUniqueId().toString());
	}
	
	public String getUUID(){
		Object obj = get("uuid");
		if(obj instanceof String){
			return (String)obj;
		}else{
			return "";
		}
	}
	
	public boolean isOnline(){
		return getPlayer() != null;
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(UUID.fromString(getUUID()));
	}
	
	@Nullable
	public String getOnlinePlayerName(){
		Player player = getPlayer();
		if(player == null){
			return null;
		}
		return player.getName();
	}
	
	public void sendMessage(String message){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.sendMessage(message);
	}
	
	public void sendMessages(String...messages){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.sendMessage(messages);
	}
	
	public void sendMessage(BaseComponent component){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.spigot().sendMessage(component);
	}
	
	public void sendMessage(BaseComponent...components){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.spigot().sendMessage(components);
	}
	
	@Nullable
	public Profile getCoreProfile(){
		return coreProfileManager.getProfileByUUID(getUUID());
	}
	
}
