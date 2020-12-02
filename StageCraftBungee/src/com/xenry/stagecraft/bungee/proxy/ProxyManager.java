package com.xenry.stagecraft.bungee.proxy;
import com.xenry.stagecraft.bungee.Bungee;
import com.xenry.stagecraft.bungee.Manager;
import com.xenry.stagecraft.bungee.proxy.commands.*;
import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProxyManager extends Manager {
	
	private boolean willShutDown = false;
	private String shutdownReason = null;
	private ScheduledTask shutdownTask = null;
	
	private NetworkPlayersUpdatePMSC networkPlayersUpdatePMSC;
	private PlayerWillSwitchPMSC playerWillSwitchPMSC;
	
	public ProxyManager(Bungee plugin){
		super("Proxy", plugin);
	}
	
	@Override
	protected void onEnable() {
		networkPlayersUpdatePMSC = new NetworkPlayersUpdatePMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(networkPlayersUpdatePMSC);
		
		playerWillSwitchPMSC = new PlayerWillSwitchPMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(playerWillSwitchPMSC);
		
		plugin.getPluginMessageManager().registerSubChannel(new SendPlayersPMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new PunishmentPMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new PunishmentRemovePMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new ProfileRankUpdatePMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new ProfileNameInfoUpdatePMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new EvacuatePlayerPMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new SudoPMSC(this));
		
		registerCommand(new EndCommand(this));
		registerCommand(new DotSendCommand(this));
		registerCommand(new DotFindCommand(this));
		registerCommand(new DotServerCommand(this));
		registerCommand(new DotListCommand(this));
		registerCommand(new ProxyDebugModeCommand(this));
		registerCommand(new ProxyBetaFeaturesCommand(this));
		registerCommand(new ProxyConfigReloadCommand(this));
	}
	
	public NetworkPlayersUpdatePMSC getNetworkPlayersUpdatePMSC() {
		return networkPlayersUpdatePMSC;
	}
	
	public PlayerWillSwitchPMSC getPlayerWillSwitchPMSC() {
		return playerWillSwitchPMSC;
	}
	
	@EventHandler
	public void onPing(ProxyPingEvent event){
		ServerPing response = event.getResponse();
		response.getPlayers().setSample(getSample(9));
		response.setDescriptionComponent(plugin.getConfiguration().getMOTDComponent());
		response.setVersion(new ServerPing.Protocol("Join with 1.16.4", response.getVersion().getProtocol()));
	}
	
	@SuppressWarnings("SameParameterValue")
	private ServerPing.PlayerInfo[] getSample(int max){
		ArrayList<ServerPing.PlayerInfo> players = new ArrayList<>();
		for(ProxiedPlayer player : plugin.getProxy().getPlayers()){
			if(max <= 0){
				break;
			}
			players.add(new ServerPing.PlayerInfo(player.getName(), player.getUniqueId().toString()));
			max--;
		}
		return players.toArray(new ServerPing.PlayerInfo[0]);
	}
	
	@EventHandler
	public void onJoin(PreLoginEvent event){
		if(willShutDown){
			event.setCancelled(true);
			event.setCancelReason(new TextComponent("The server is about to shut down. Try again later."));
		}
	}
	
	public void cancelShutdown(){
		willShutDown = false;
		if(shutdownTask != null){
			shutdownTask.cancel();
			plugin.getProxy().broadcast(new ComponentBuilder("Network shutdown cancelled.")
					.color(ChatColor.RED).create());
			shutdownTask = null;
		}
	}
	
	public void doShutdown(int seconds, String reason){
		if(seconds < 0){
			throw new IllegalArgumentException("seconds cannot be negative");
		}
		willShutDown = true;
		shutdownReason = reason;
		if(seconds == 0){
			shutdownNow();
			return;
		}
		BaseComponent[] line1 = new ComponentBuilder(" ⚠ ").color(ChatColor.RED).bold(false)
				.append(" Attention ").color(ChatColor.RED).bold(true)
				.append(" ⚠ ").color(ChatColor.RED).bold(false).create();
		BaseComponent[] line2 = new ComponentBuilder("The network will be shutting down in " + seconds
				+ " seconds.").color(ChatColor.YELLOW).create();
		plugin.getProxy().broadcast(new TextComponent(""));
		plugin.getProxy().broadcast(new TextComponent(""));
		plugin.getProxy().broadcast(line1);
		plugin.getProxy().broadcast(line2);
		if(reason != null && !reason.isEmpty()){
			plugin.getProxy().broadcast(new ComponentBuilder(reason).color(ChatColor.AQUA).italic(true).create());
		}
		plugin.getProxy().broadcast(new TextComponent(""));
		plugin.getProxy().broadcast(new TextComponent(""));
		
		BungeeTitle bt = new BungeeTitle();
		bt.title(line1);
		bt.subTitle(line2);
		bt.fadeIn(5);
		bt.stay(80);
		bt.fadeOut(20);
		for(ProxiedPlayer player : plugin.getProxy().getPlayers()){
			bt.send(player);
		}
		shutdownTask = plugin.getProxy().getScheduler().schedule(plugin, this::shutdownNow, seconds, TimeUnit.SECONDS);
	}
	
	private void shutdownNow(){
		ComponentBuilder cb = new ComponentBuilder("StageCraft is shutting down.").color(ChatColor.RED);
		if(shutdownReason != null && !shutdownReason.isEmpty()){
			cb.append("\n" + shutdownReason).color(ChatColor.WHITE);
		}
		BaseComponent[] message = cb.create();
		for(ProxiedPlayer player : plugin.getProxy().getPlayers()){
			player.disconnect(message);
		}
		plugin.getProxy().stop();
	}
	
	public ScheduledTask getShutdownTask() {
		return shutdownTask;
	}
	
	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent event){
		plugin.getProxy().getScheduler().schedule(plugin, this::sendNetworkPlayersUpdate, 50L, TimeUnit.MILLISECONDS);
	}
	
	private static final TextComponent TAB_LIST_HEADER = new TextComponent(new ComponentBuilder()
			.append("Stage").color(ChatColor.BLUE).bold(true)
			.append("Craft").color(ChatColor.GREEN).bold(true).create());
	
	@EventHandler
	public void onSwitch(ServerSwitchEvent event){
		event.getPlayer().setTabHeader(TAB_LIST_HEADER, new TextComponent(new ComponentBuilder(
				event.getPlayer().getServer().getInfo().getName()).color(ChatColor.DARK_GRAY).italic(true).create()));
		plugin.getProxy().getScheduler().schedule(plugin, this::sendNetworkPlayersUpdate, 50L, TimeUnit.MILLISECONDS);
	}
	
	private void sendNetworkPlayersUpdate(){
		networkPlayersUpdatePMSC.send();
	}
	
}
