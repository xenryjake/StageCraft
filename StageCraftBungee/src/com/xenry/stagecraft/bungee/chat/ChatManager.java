package com.xenry.stagecraft.bungee.chat;
import com.xenry.stagecraft.bungee.Bungee;
import com.xenry.stagecraft.bungee.Manager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ChatManager extends Manager {
	
	public ChatManager(Bungee plugin){
		super("Chat", plugin);
	}
	
	@Override
	protected void onEnable() {
		plugin.getPluginMessageManager().registerSubChannel(new PlayerChatPMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new StaffChatPMSC(this));
		
		registerCommand(new GlobalFakeMessageCommand(this));
	}
	
	@EventHandler
	public void onLogin(PostLoginEvent event){
		plugin.getProxy().broadcast(TextComponent.fromLegacyText("§a +§7 " + event.getPlayer().getName()));
	}
	
	@EventHandler
	public void onLeave(PlayerDisconnectEvent event){
		plugin.getProxy().broadcast(TextComponent.fromLegacyText("§c -§7 " + event.getPlayer().getName()));
	}
	
	@EventHandler
	public void onSwitch(ServerSwitchEvent event){
		ServerInfo from = event.getFrom();
		if(from == null){
			return;
		}
		final ProxiedPlayer player = event.getPlayer();
		plugin.getProxy().getScheduler().schedule(plugin, () -> plugin.getProxy().broadcast(
				TextComponent.fromLegacyText("§9 »§7 " + event.getPlayer().getName() + " §9→ "
						+ player.getServer().getInfo().getName())), 50, TimeUnit.MILLISECONDS);
	}
	
}
