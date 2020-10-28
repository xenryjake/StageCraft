package com.xenry.stagecraft.server;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.server.commands.*;
import com.xenry.stagecraft.util.M;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ServerManager extends Manager<Core> {
	
	private boolean willShutDown = false;
	private BukkitTask shutdownTask;
	
	private HashMap<String,List<String>> networkPlayers;
	private List<String> allNetworkPlayers;
	
	public ServerManager(Core plugin){
		super("Server", plugin);
	}
	
	@Override
	protected void onEnable() {
		plugin.getPluginMessageManager().registerSubChannel(new NetworkPlayersUpdatePMSC(this));
		
		registerCommand(new DebugModeCommand(this));
		registerCommand(new BetaFeaturesCommand(this));
		registerCommand(new StopCommand(this));
		registerCommand(new ServerConfigReloadCommand(this));
		registerCommand(new ListCommand(this));
		registerCommand(new ServerCommand(this));
	}
	
	@EventHandler
	public void onListPing(ServerListPingEvent event){
		event.setMotd("StageCraft internal server. Connect to proxy.");
	}
	
	@EventHandler
	public void on(AsyncPlayerPreLoginEvent event){
		if(willShutDown){
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(M.err + "The server is about to shut down. Try again later.");
		}
	}
	
	public void cancelShutdown(){
		willShutDown = false;
		if(shutdownTask != null){
			shutdownTask.cancel();
			Bukkit.broadcastMessage("§cServer shutdown cancelled.");
			shutdownTask = null;
		}
	}
	
	public void doShutdown(int seconds, String reason){
		if(seconds < 0){
			throw new IllegalArgumentException("seconds cannot be negative");
		}
		willShutDown = true;
		if(seconds == 0){
			shutdownNow();
			return;
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§c ⚠ §r §c§lAttention§r §c ⚠");
		Bukkit.broadcastMessage("§eThe server will be shutting down in " + seconds + " seconds.");
		if(reason != null && !reason.isEmpty()){
			Bukkit.broadcastMessage("§b§o" + reason);
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		TitleManagerAPI tm = plugin.getIntegrationManager().getTitleManager();
		if(tm != null){
			for(Player player : Bukkit.getOnlinePlayers()){
				tm.sendTitles(player, "§c⚠§r §c§lAttention§r §c⚠", "§eThe server will be shutting down in "
						+ seconds + " seconds.", 5, 80, 20);
			}
		}
		shutdownTask = plugin.getServer().getScheduler().runTaskLater(plugin, this::shutdownNow, seconds*20L);
	}
	
	private void shutdownNow(){
		for(Player player : Bukkit.getOnlinePlayers()){
			player.kickPlayer(Bukkit.getShutdownMessage());
		}
		Bukkit.shutdown();
	}
	
	public BukkitTask getShutdownTask() {
		return shutdownTask;
	}
	
	public void setNetworkPlayers(HashMap<String,List<String>> map){
		networkPlayers = map;
		List<String> all = new ArrayList<>();
		for(List<String> list : map.values()){
			all.addAll(list);
		}
		allNetworkPlayers = all;
	}
	
	public HashMap<String,List<String>> getNetworkPlayers() {
		return networkPlayers;
	}
	
	public List<String> getAllNetworkPlayers() {
		return allNetworkPlayers;
	}
	
	public Set<String> getServerNames(){
		return networkPlayers.keySet();
	}
	
}
