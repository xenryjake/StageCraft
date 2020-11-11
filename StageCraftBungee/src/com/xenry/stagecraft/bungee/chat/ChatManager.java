package com.xenry.stagecraft.bungee.chat;
import com.xenry.stagecraft.bungee.Bungee;
import com.xenry.stagecraft.bungee.Manager;
import com.xenry.stagecraft.bungee.chat.commands.*;
import com.xenry.stagecraft.bungee.util.Log;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ChatManager extends Manager {
	
	public static final String PM_REPLY_KEY = "**REPLY**";
	
	private final HashMap<String,ConversationEntry> conversations;
	
	private StaffChatPMSC staffChatPMSC;
	
	public ChatManager(Bungee plugin){
		super("Chat", plugin);
		conversations = new HashMap<>();
	}
	
	@Override
	protected void onEnable() {
		plugin.getPluginMessageManager().registerSubChannel(new PlayerChatPMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new PrivateMessagePMSC(this));
		plugin.getPluginMessageManager().registerSubChannel(new BroadcastPMSC(this));
		staffChatPMSC = new StaffChatPMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(staffChatPMSC);
		
		registerCommand(new DotFakeMessageCommand(this));
		registerCommand(new DotStaffChatCommand(this));
		registerCommand(new DotSayCommand(this));
		registerCommand(new DotMessageCommand(this));
		registerCommand(new DotReplyCommand(this));
		registerCommand(new DotBroadcastCommand(this));
	}
	
	public StaffChatPMSC getStaffChatPMSC() {
		return staffChatPMSC;
	}
	
	/*@EventHandler
	public void onLogin(PostLoginEvent event){
		//plugin.getProxy().broadcast(TextComponent.fromLegacyText("§a +§7 " + event.getPlayer().getName());
	}*/
	
	@EventHandler
	public void onLeave(PlayerDisconnectEvent event){
		if(event.getPlayer().getServer() != null){
			plugin.getProxy().broadcast(TextComponent.fromLegacyText("§c -§7 " + event.getPlayer().getName()));
		}
	}
	
	@EventHandler
	public void onSwitch(ServerSwitchEvent event){
		ServerInfo from = event.getFrom();
		final ProxiedPlayer player = event.getPlayer();
		BaseComponent[] message;
		if(from == null){
			message = TextComponent.fromLegacyText("§a +§7 " + event.getPlayer().getName() + " §9("
					+ player.getServer().getInfo().getName() + ")");
		}else{
			message = TextComponent.fromLegacyText("§9 »§7 " + event.getPlayer().getName() + " §9→ "
					+ player.getServer().getInfo().getName());
		}
		plugin.getProxy().getScheduler().schedule(plugin, () -> plugin.getProxy().broadcast(message), 50,
				TimeUnit.MILLISECONDS);
	}
	
	public void handlePrivateMessage(ProxiedPlayer sender, String targetName, BaseComponent[] message){
		ProxiedPlayer target;
		if(targetName.equals(PM_REPLY_KEY)){
			targetName = getConversation(sender.getName());
			if(targetName == null){
				sender.sendMessage(M.error("You have not messaged anyone recently."));
				return;
			}
		}
		target = plugin.getProxy().getPlayer(targetName);
		if(target == null){
			sender.sendMessage(M.error(targetName + " is not online."));
			return;
		}
		handlePrivateMessage(sender, target, message);
	}
	
	public void handlePrivateMessage(ProxiedPlayer sender, ProxiedPlayer target, BaseComponent[] message){
		if(sender == target){
			sender.sendMessage(M.error("You can't message yourself."));
			return;
		}
		
		String senderName = sender.getName();
		String targetName = target.getName();
		Log.info("[PM] " + senderName + " to " + targetName + ": " + TextComponent.toPlainText(message));
		sender.sendMessage(new ComponentBuilder("You » " + targetName + ": ")
				.color(ChatColor.AQUA).append(message).create());
		target.sendMessage(new ComponentBuilder(senderName + " » You: ")
				.color(ChatColor.AQUA).append(message).create());
		
		long timeout = System.currentTimeMillis() + 3600000;
		conversations.put(senderName, new ConversationEntry(targetName, timeout));
		conversations.put(targetName, new ConversationEntry(senderName, timeout));
	}
	
	/**
	 * Get the name of a player's conversation
	 * @param fromName name of player to lookup
	 * @return name of player conversing with
	 */
	public String getConversation(String fromName){
		ConversationEntry entry = conversations.get(fromName);
		if(entry == null){
			return null;
		}
		if(entry.timeout <= System.currentTimeMillis()){
			conversations.remove(fromName);
			return null;
		}
		return entry.name;
	}
	
	private static class ConversationEntry {
		
		public final String name;
		public final Long timeout;
		
		public ConversationEntry(String name, Long timeout){
			this.name = name;
			this.timeout = timeout;
		}
		
	}
	
}
