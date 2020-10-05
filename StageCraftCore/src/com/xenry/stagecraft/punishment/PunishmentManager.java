package com.xenry.stagecraft.punishment;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.PrivateMessageEvent;
import com.xenry.stagecraft.chat.PublicChatEvent;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.punishment.commands.*;
import com.xenry.stagecraft.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PunishmentManager extends Manager<Core> {
	
	private final DBCollection collection;
	private final List<Punishment> punishments;
	
	public PunishmentManager(Core plugin){
		super("Punishment", plugin);
		collection = plugin.getMongoManager().getCoreCollection("punishments");
		collection.setObjectClass(Punishment.class);
		punishments = new ArrayList<>();
	}
	
	@Override
	protected void onEnable() {
		downloadPunishments();
		
		registerCommand(new PunishmentCommand(this));
		registerCommand(new DisconnectCommand(this));
		registerCommand(new KickCommand(this));
		registerCommand(new MuteCommand(this));
		registerCommand(new BanCommand(this));
	}
	
	public void addPunishment(Punishment punishment){
		punishments.add(punishment);
		save(punishment);
	}
	
	public void downloadPunishments(){
		punishments.clear();
		DBCursor c = collection.find();
		while(c.hasNext()){
			punishments.add((Punishment)c.next());
		}
	}
	
	public void save(Punishment punishment){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(punishment));
	}
	
	public void saveSync(Punishment punishment){
		collection.save(punishment);
	}
	
	public void delete(Punishment punishment){
		punishments.remove(punishment);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.remove(new BasicDBObject("_id", punishment.get("_id"))));
	}
	
	public List<Punishment> getPunishments(Profile profile){
		return getPunishments(profile, null);
	}
	
	public List<Punishment> getPunishments(Profile profile, Punishment.Type type){
		List<Punishment> list = new ArrayList<>();
		for(Punishment punishment : punishments){
			if(punishment.getPlayerUUID().equals(profile.getUUID()) && (type == null || type == punishment.getType())){
				list.add(punishment);
			}
		}
		return list;
	}
	
	public List<Punishment> getActivePunishments(Profile profile){
		return getActivePunishments(profile, null);
	}
	
	public List<Punishment> getActivePunishments(Profile profile, Punishment.Type type){
		List<Punishment> list = new ArrayList<>();
		for(Punishment punishment : punishments){
			if(punishment.isActive() && punishment.getPlayerUUID().equals(profile.getUUID()) && (type == null || type == punishment.getType())){
				list.add(punishment);
			}
		}
		return list;
	}
	
	public Punishment getOutstandingPunishment(Profile profile, Punishment.Type type){
		for(Punishment punishment : punishments){
			if(punishment.isActive() && punishment.getPlayerUUID().equals(profile.getUUID()) && type == punishment.getType()){
				return punishment;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onChat(PublicChatEvent event){
		Profile profile = event.getProfile();
		Punishment mute = getOutstandingPunishment(profile, Punishment.Type.MUTE);
		if(mute != null){
			event.setCancelled(true);
			profile.sendMessage(mute.getMessage());
			Log.info("Muted player " + profile.getLatestUsername() + " tried to chat: " + event.getMessage());
		}
	}
	
	@EventHandler
	public void onChat(PrivateMessageEvent event){
		Profile profile = event.getFrom();
		if(profile == null){
			return;
		}
		Punishment mute = getOutstandingPunishment(profile, Punishment.Type.MUTE);
		if(mute != null){
			event.setCancelled(true);
			profile.sendMessage(mute.getMessage());
			Log.info("Muted player " + profile.getLatestUsername() + " tried to send a private message: " + event.getMessage());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		Profile profile = plugin.getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		Punishment punishment = getOutstandingPunishment(profile, Punishment.Type.MUTE);
		if(punishment != null){
			player.sendMessage(punishment.getMessage());
		}
	}
	
}
