package com.xenry.stagecraft.survival.jail;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.survival.jail.commands.*;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.profile.SurvivalProfile;
import com.xenry.stagecraft.survival.teleportation.PreTeleportationEvent;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.survival.teleportation.TeleportationEvent;
import com.xenry.stagecraft.survival.teleportation.Warp;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class JailManager extends Manager<Survival> {
	
	private final DBCollection collection;
	private final List<Sentence> sentences;
	
	private JailHandler jailHandler;
	private int jailTickTaskID;
	
	public JailManager(Survival plugin) {
		super("Jail", plugin);
		collection = getCore().getMongoManager().getCoreCollection("survivalJailSentences");
		collection.setObjectClass(Sentence.class);
		sentences = new ArrayList<>();
	}
	
	@Override
	protected void onEnable() {
		jailHandler = new JailHandler(this);
		jailHandler.downloadJails();
		downloadSentences();
		
		registerCommand(new JailCommand(this));
		registerCommand(new SetJailCommand(this));
		registerCommand(new DeleteJailCommand(this));
		registerCommand(new JailListCommand(this));
		registerCommand(new SentenceCommand(this));
		
		jailTickTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::handleJailTick, 10L, 10L);
	}
	
	@Override
	protected void onDisable() {
		jailHandler.saveAllJailsSync();
		plugin.getServer().getScheduler().cancelTask(jailTickTaskID);
	}
	
	public JailHandler getJailHandler() {
		return jailHandler;
	}
	
	public void addSentence(Sentence sentence){
		sentences.add(sentence);
		save(sentence);
	}
	
	public void downloadSentences(){
		sentences.clear();
		DBCursor c = collection.find();
		while(c.hasNext()){
			sentences.add((Sentence)c.next());
		}
	}
	
	public void save(Sentence sentence){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> collection.save(sentence));
	}
	
	public void saveSync(Sentence sentence){
		collection.save(sentence);
	}
	
	public void delete(Sentence sentence){
		sentences.remove(sentence);
		Bukkit.getScheduler().runTaskAsynchronously(plugin,
				() -> collection.remove(new BasicDBObject("_id", sentence.get("_id"))));
	}
	
	public List<Sentence> getSentences(GenericProfile profile){
		List<Sentence> list = new ArrayList<>();
		for(Sentence sentence : sentences){
			if(sentence.getPlayerUUID().equals(profile.getUUID())){
				list.add(sentence);
			}
		}
		return list;
	}
	
	public List<Sentence> getActiveSentences(GenericProfile profile){
		List<Sentence> list = new ArrayList<>();
		for(Sentence sentence : sentences){
			if(sentence.isActive() && sentence.getPlayerUUID().equals(profile.getUUID())){
				list.add(sentence);
			}
		}
		return list;
	}
	
	public Sentence getOutstandingSentence(GenericProfile profile){
		for(Sentence sentence : sentences){
			if(sentence.isActive() && sentence.getPlayerUUID().equals(profile.getUUID())){
				return sentence;
			}
		}
		return null;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onTeleport(PreTeleportationEvent event){
		if(event.getTeleportation().type == Teleportation.Type.ADMIN){
			return;
		}
		GenericProfile profile = getCore().getProfileManager().getProfile(event.getTeleportation().target);
		if(profile == null){
			return;
		}
		if(getOutstandingSentence(profile) != null){
			event.setCancelled(true);
			event.setCancellationMessage("You can't teleport while in jail!");
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onTeleport(TeleportationEvent event){
		if(event.getTeleportation().type == Teleportation.Type.ADMIN){
			return;
		}
		GenericProfile profile = getCore().getProfileManager().getProfile(event.getTeleportation().target);
		if(profile == null){
			return;
		}
		if(getOutstandingSentence(profile) != null){
			event.setCancelled(true);
			event.setCancellationMessage("You can't teleport while in jail!");
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		GenericProfile profile = getCore().getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		Sentence sentence = getOutstandingSentence(profile);
		if(sentence != null){
			Jail jail = jailHandler.getJail(sentence.getJailName());
			if(jail != null){
				player.teleport(jail.getLocation());
				player.setGameMode(GameMode.ADVENTURE);
				player.sendMessage(sentence.getMessage());
			}
		}
	}
	
	//teleport player to spawn when they quit if they are in jail so they are at spawn when they re-join if not jailed
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		GenericProfile profile = plugin.getSurvivalProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		Sentence sentence = getOutstandingSentence(profile);
		if(sentence != null){
			Warp spawn = plugin.getTeleportationManager().getWarpHandler().getSpawn();
			if(spawn != null){
				player.teleport(spawn.getLocation());
			}
		}
	}
	
	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent event){
		if(event.getNewGameMode() == GameMode.ADVENTURE){
			return;
		}
		Player player = event.getPlayer();
		GenericProfile profile = getCore().getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		Sentence sentence = getOutstandingSentence(profile);
		if(sentence != null){
			event.setCancelled(true);
		}
	}
	
	public void handleJailTick(){
		for(Player player : Bukkit.getOnlinePlayers()){
			SurvivalProfile profile = plugin.getSurvivalProfileManager().getProfile(player);
			if(profile == null){
				continue;
			}
			Sentence sentence = getOutstandingSentence(profile);
			if(sentence == null){
				if(profile.sessionInJail){
					profile.sessionInJail = false;
					player.setGameMode(GameMode.SURVIVAL);
					Warp spawn = plugin.getTeleportationManager().getWarpHandler().getSpawn();
					if(spawn != null){
						plugin.getTeleportationManager().createAndExecuteTeleportation(player, null,
								player.getLocation(), spawn.getLocation(), Teleportation.Type.ADMIN, false);
					}
				}
				continue;
			}
			profile.sessionInJail = true;
			sendJailActionbar(player, sentence);
		}
	}
	
	private void sendJailActionbar(Player player, Sentence sentence){
		TitleManagerAPI api = getCore().getIntegrationManager().getTitleManager();
		if(api == null){
			return;
		}
		String actionBar = "You are in jail!";
		if(sentence.getExpiresAt() > 0){
			actionBar += " Expires in " + M.elm + TimeUtil.simplerString(sentence.getTimeRemaining());
		}
		api.sendActionbar(player, actionBar);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event){
		GenericProfile profile = getCore().getProfileManager().getProfile(event.getPlayer());
		if(profile == null){
			return;
		}
		if(getOutstandingSentence(profile) != null){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent event){
		GenericProfile profile = getCore().getProfileManager().getProfile(event.getPlayer());
		if(profile == null){
			return;
		}
		if(getOutstandingSentence(profile) != null){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent event){
		GenericProfile profile = getCore().getProfileManager().getProfile(event.getPlayer());
		if(profile == null){
			return;
		}
		if(getOutstandingSentence(profile) != null){
			event.setCancelled(true);
		}
	}
	
}
