package com.xenry.stagecraft.bungee.player;
import com.xenry.stagecraft.bungee.Bungee;
import com.xenry.stagecraft.bungee.Manager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerManager extends Manager {
	
	private final HashMap<String,Player> players;
	
	public PlayerManager(Bungee plugin){
		super("Player", plugin);
		players = new HashMap<>();
	}
	
	@Nullable
	public Player getPlayer(String uuid){
		return players.getOrDefault(uuid, null);
	}
	
	@Nullable
	public Player getPlayer(@NotNull ProxiedPlayer player){
		return players.getOrDefault(player.getUniqueId().toString(), null);
	}
	
	@EventHandler
	public void on(PostLoginEvent event){
		players.put(event.getPlayer().getUniqueId().toString(), new Player(event.getPlayer()));
	}
	
	@EventHandler
	public void on(PlayerDisconnectEvent event){
		players.remove(event.getPlayer().getUniqueId().toString());
	}
	
}
