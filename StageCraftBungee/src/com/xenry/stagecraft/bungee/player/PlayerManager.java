package com.xenry.stagecraft.bungee.player;
import com.xenry.stagecraft.bungee.Bungee;
import com.xenry.stagecraft.bungee.Manager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerManager extends Manager {
	
	private final HashMap<String,PlayerState> playerStates;
	private PlayerStateUpdatePMSC playerStateUpdatePMSC;
	
	public PlayerManager(Bungee plugin){
		super("Player", plugin);
		playerStates = new HashMap<>();
	}
	
	@Override
	protected void onEnable() {
		playerStateUpdatePMSC = new PlayerStateUpdatePMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(playerStateUpdatePMSC);
	}
	
	public PlayerStateUpdatePMSC getPlayerStateUpdatePMSC() {
		return playerStateUpdatePMSC;
	}
	
	public HashMap<String,PlayerState> getPlayerStates() {
		return playerStates;
	}
	
	@Nullable
	public PlayerState getPlayerState(String uuid){
		return playerStates.getOrDefault(uuid, null);
	}
	
	@Nullable
	public PlayerState getPlayerState(@NotNull ProxiedPlayer player){
		return playerStates.getOrDefault(player.getUniqueId().toString(), null);
	}
	
	public void setPlayerState(@NotNull PlayerState state){
		playerStates.put(state.getUUID(), state);
	}
	
	public void clearPlayerState(String uuid){
		playerStates.remove(uuid);
	}
	
	@EventHandler
	public void on(PostLoginEvent event){
		playerStates.put(event.getPlayer().getUniqueId().toString(), new PlayerState(event.getPlayer()));
		plugin.getProxy().getScheduler().schedule(plugin, this::update, 50L, TimeUnit.MILLISECONDS);
	}
	
	@EventHandler
	public void on(PlayerDisconnectEvent event){
		playerStates.remove(event.getPlayer().getUniqueId().toString());
		plugin.getProxy().getScheduler().schedule(plugin, this::update, 50L, TimeUnit.MILLISECONDS);
	}
	
	@EventHandler
	public void on(ServerSwitchEvent event){
		plugin.getProxy().getScheduler().schedule(plugin, this::update, 50L, TimeUnit.MILLISECONDS);
	}
	
	public void update(){
		for(PlayerState state : new HashMap<>(playerStates).values()){
			Server server = state.getProxiedPlayer().getServer();
			state.setServerName(server == null ? "nowhere" : server.getInfo().getName());
		}
		playerStateUpdatePMSC.send();
	}
	
}
