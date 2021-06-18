package com.xenry.stagecraft.chat;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.commands.*;
import com.xenry.stagecraft.chat.commands.privatemessage.MessageCommand;
import com.xenry.stagecraft.chat.commands.privatemessage.ReplyCommand;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.chat.emotes.EmotesCommand;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ChatManager extends Manager<Core> {
	
	public static final Access COLOR_ACCESS = Rank.ADMIN;
	public static final String PM_REPLY_KEY = "**REPLY**";
	
	private Rank chatRank = Rank.MEMBER;
	private Channel publicChannel;
	
	private String globalChatPrefix;
	
	private PlayerChatPMSC playerChatPMSC;
	private PrivateMessagePMSC privateMessagePMSC;
	private BroadcastPMSC broadcastPMSC;
	
	private final Cooldown defaultCooldown = new Cooldown(0, null);
	private final Cooldown premiumCooldown = new Cooldown(0, null);
	private final Access premiumCooldownAccess = new Access.Any(Rank.PREMIUM, Rank.MOD);
	private final Access noCooldownAccess = Rank.MOD;
	
	public ChatManager(Core plugin){
		super("Chat", plugin);
	}
	
	@Override
	protected void onEnable(){
		playerChatPMSC = new PlayerChatPMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(playerChatPMSC);
		
		privateMessagePMSC = new PrivateMessagePMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(privateMessagePMSC);
		
		broadcastPMSC = new BroadcastPMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(broadcastPMSC);
		
		registerCommand(new BroadcastCommand(this));
		registerCommand(new FakeMessageCommand(this));
		registerCommand(new StaffChatCommand(this));
		registerCommand(new AdminChatCommand(this));
		registerCommand(new MessageCommand(this));
		registerCommand(new ReplyCommand(this));
		registerCommand(new SayCommand(this));
		
		registerCommand(new EmotesCommand(this));
		
		setGlobalChatPrefix(plugin.getConfig().getString("global-chat-prefix", ""));
		try{
			setPublicChannel(Channel.valueOf(plugin.getConfig().getString("public-chat-channel", Channel.GLOBAL.name())));
		}catch(Exception exception){
			Log.warn("Invalid public-chat-channel. Setting to " + Channel.GLOBAL.name() + ".");
			setPublicChannel(Channel.GLOBAL);
		}
	}
	
	public PlayerChatPMSC getPlayerChatPMSC() {
		return playerChatPMSC;
	}
	
	public PrivateMessagePMSC getPrivateMessagePMSC() {
		return privateMessagePMSC;
	}
	
	public BroadcastPMSC getBroadcastPMSC() {
		return broadcastPMSC;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		Profile profile = plugin.getProfileManager().getProfile(player);
		if(profile == null){
			Log.warn("Player tried to chat, but profile was not found.");
			return;
		}
		event.setCancelled(true);
		Channel channel = publicChannel;
		if(!publicChannel.access.has(profile)){
			player.sendMessage(M.error("You don't have permission to speak in this channel."));
			return;
		}
		if(channel.isPublic && !chatRank.has(profile)){
			player.sendMessage(M.error("The chat is currently silenced to a rank above you."));
			Log.info("[Chat Attempt] " + event.getPlayer().getName() + ": " + event.getMessage());
			return;
		}
		if(!noCooldownAccess.has(profile)){
			Cooldown cooldown = premiumCooldownAccess.has(profile) ? premiumCooldown : defaultCooldown;
			if(!cooldown.use(profile)){
				player.sendMessage(M.err + "Please slow down.");
				return;
			}
		}
		
		ChatEvent chatEvent = new ChatEvent(event.isAsynchronous(), channel, profile, event.getMessage());
		plugin.getServer().getPluginManager().callEvent(chatEvent);
		if(chatEvent.isCancelled()){
			return;
		}
		sendChat(player, channel, channel.format(this, profile, chatEvent.getMessage()));
	}
	
	/**
	 * Send a message in the specified channel
	 * @param pluginMessageSender the player used to send the plugin message (any player)
	 * @param channel the channel for the message
	 * @param message the message to send (fully formatted)
	 */
	public void sendChat(Player pluginMessageSender, Channel channel, BaseComponent[] message){
		playerChatPMSC.send(pluginMessageSender, channel, message);
		//Log.toCS(new ComponentBuilder("[#" + channel.name() + "] ").append(message).create());
	}
	
	/**
	 * Get the rank required to use chat
	 * @return minimum rank
	 */
	@Deprecated
	public Rank getChatRank() {
		return chatRank;
	}
	
	/**
	 * Set the rank required to use chat
	 * @param chatRank minimum rank
	 */
	@Deprecated
	public void setChatRank(Rank chatRank) {
		this.chatRank = chatRank;
	}
	
	/**
	 * Get the global chat prefix
	 * @return the prefix
	 */
	public String getGlobalChatPrefix() {
		return globalChatPrefix;
	}
	
	/**
	 * Set the global chat prefix. This does NOT save to the config file.
	 * @param globalChatPrefix the prefix
	 */
	public void setGlobalChatPrefix(String globalChatPrefix) {
		this.globalChatPrefix = globalChatPrefix == null ? "" :
				ChatColor.translateAlternateColorCodes('&', globalChatPrefix);
	}
	
	/**
	 * Get the public chat channel
	 * @return the channel
	 */
	public Channel getPublicChannel() {
		return publicChannel;
	}
	
	/**
	 * Set the public chat channel. This does NOT save to the config file.
	 * @param publicChannel the channel
	 */
	public void setPublicChannel(Channel publicChannel){
		this.publicChannel = publicChannel;
	}
	
	/*public void handleServerPrivateMessage(@NotNull CommandSender from, String targetName, String message){
		Player pluginMessageSender = PlayerUtil.getAnyPlayer();
		if(pluginMessageSender == null){
			from.sendMessage(M.error("There are no players on this server. Please try again on another server or the proxy."));
			return;
		}
		PrivateMessageEvent event = new PrivateMessageEvent(null, targetName, message);
		plugin.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()){
			return;
		}
		message = ChatColor.translateAlternateColorCodes('&', event.getMessage());
		message = Emote.replaceAllEmotes(message);
		privateMessagePMSC.send(pluginMessageSender, M.CONSOLE_NAME, targetName, message);
	}*/
	
	public void handlePrivateMessage(@NotNull Profile from, String targetName, String message){
		PrivateMessageEvent event = new PrivateMessageEvent(from, targetName, message);
		plugin.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			return;
		}
		message = event.getMessage();
		
		if(COLOR_ACCESS.has(from)) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		message = Emote.replaceEmotes(message, ChatColor.WHITE, from);
		
		privateMessagePMSC.send(from.getPlayer(), from.getLatestUsername(), targetName, message);
	}
	
	
}
