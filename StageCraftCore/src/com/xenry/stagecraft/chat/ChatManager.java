package com.xenry.stagecraft.chat;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.commands.FakeMessageCommand;
import com.xenry.stagecraft.chat.commands.SayCommand;
import com.xenry.stagecraft.chat.commands.privatemessage.SocialSpyCommand;
import com.xenry.stagecraft.chat.commands.StaffChatCommand;
import com.xenry.stagecraft.chat.commands.chat.ChatCommand;
import com.xenry.stagecraft.chat.commands.privatemessage.MessageCommand;
import com.xenry.stagecraft.chat.commands.privatemessage.ReplyCommand;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.chat.emotes.EmotesCommand;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.Setting;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ChatManager extends Manager<Core> {
	
	public static final Access COLOR_ACCESS = Rank.ADMIN;
	
	private final HashMap<String,ConversationEntry> conversations;
	private Rank chatRank = Rank.MEMBER;
	
	public ChatManager(Core plugin){
		super("Chat", plugin);
		conversations = new HashMap<>();
	}
	
	@Override
	protected void onEnable(){
		registerCommand(new ChatCommand(this));
		registerCommand(new FakeMessageCommand(this));
		registerCommand(new StaffChatCommand(this));
		registerCommand(new MessageCommand(this));
		registerCommand(new ReplyCommand(this));
		registerCommand(new SocialSpyCommand(this));
		registerCommand(new SayCommand(this));
		
		registerCommand(new EmotesCommand(this));
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
		if(!profile.check(chatRank)){
			player.sendMessage(M.error("The chat is currently silenced to rank "
					+ chatRank.getColor() + chatRank.getName() + M.err + "."));
			Log.info("[Chat Attempt] " + event.getPlayer().getName() + ": " + event.getMessage());
			event.setCancelled(true);
			return;
		}
		
		PublicChatEvent chatEvent = new PublicChatEvent(event.isAsynchronous(), profile, "§7",
				profile.getDisplayName(), event.getMessage());
		plugin.getServer().getPluginManager().callEvent(chatEvent);
		if(chatEvent.isCancelled()){
			event.setCancelled(true);
			return;
		}
		if(COLOR_ACCESS.has(profile)){
			chatEvent.setMessage(ChatColor.translateAlternateColorCodes('&', chatEvent.getMessage()));
		}
		if(Emote.EMOTE_ACCESS.has(profile)){
			chatEvent.setMessage(Emote.replaceEmotes(chatEvent.getMessage(), ChatColor.WHITE));
		}
		
		HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new Text(TextComponent.fromLegacyText("§rMessage " + chatEvent.getPrefix()
						+ (chatEvent.prefixContainsText() ? " " : "") + chatEvent.getDisplayName())));
		ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName() + " ");
		BaseComponent[] message = new ComponentBuilder()
				.append(TextComponent.fromLegacyText("§r" + chatEvent.getPrefix()
						+ (chatEvent.prefixContainsText() ? " " : "")))
				.append(TextComponent.fromLegacyText(chatEvent.getDisplayName())).event(he).event(ce)
				.append(TextComponent.fromLegacyText("§8:§r ")).event((ClickEvent)null).event((HoverEvent)null)
				.append(TextComponent.fromLegacyText(chatEvent.getMessage()))
				.create();
		for(Player p : Bukkit.getOnlinePlayers()){
			p.spigot().sendMessage(message);
		}
		Log.toCS(message);
		event.setCancelled(true);
		//event.setFormat("§r" + chatEvent.getPrefix() + (chatEvent.prefixContainsText() ? " " : "")
		// + chatEvent.getDisplayName() + "§8:§r %2$s");
	}
	
	/**
	 * Get the rank required to use chat
	 * @return minimum rank
	 */
	public Rank getChatRank() {
		return chatRank;
	}
	
	/**
	 * Set the rank required to use chat
	 * @param chatRank minimum rank
	 */
	public void setChatRank(Rank chatRank) {
		this.chatRank = chatRank;
	}
	
	/**
	 * Send a private message
	 * @param to sender of message (null if console)
	 * @param from recipient of message (null if console)
	 * @param message message to be sent
	 */
	public void sendPrivateMessage(@Nullable Profile to, @Nullable Profile from, String message) {
		if(to == from) {
			if(from == null) {
				Log.toCS(M.error("You can't message yourself."));
			} else {
				from.sendMessage(M.error("You can't message yourself."));
			}
			return;
		}
		PrivateMessageEvent event = new PrivateMessageEvent(from, to, message);
		plugin.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			return;
		}
		message = event.getMessage();
		
		if(from == null || COLOR_ACCESS.has(from)) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(from == null || Emote.EMOTE_ACCESS.has(from)) {
			message = Emote.replaceEmotes(message, ChatColor.WHITE);
		}
		
		CommandSender toCS = to == null ? plugin.getServer().getConsoleSender() : to.getPlayer();
		CommandSender fromCS = from == null ? plugin.getServer().getConsoleSender() : from.getPlayer();
		boolean useDisplayName = toCS instanceof Player && fromCS instanceof Player;
		
		String toName = to == null ? M.CONSOLE_NAME : to.getLatestUsername();
		String fromName = from == null ? M.CONSOLE_NAME : from.getLatestUsername();
		String toDisplayName = to == null ? M.CONSOLE_NAME : to.getColorlessDisplayName();
		String fromDisplayName = from == null ? M.CONSOLE_NAME : from.getColorlessDisplayName();
		
		Log.info("[PM] " + fromName + " to " + toName + ": " + message);
		toCS.sendMessage("§b" + (useDisplayName ? fromDisplayName : fromName) + "§b » You: §f" + message);
		fromCS.sendMessage("§b" + "You » " + (useDisplayName ? toDisplayName : toName) + "§b: §f" + message);

		conversations.put(toName, new ConversationEntry(fromName, System.currentTimeMillis() + 3600000));
		conversations.put(fromName, new ConversationEntry(toName, System.currentTimeMillis() + 3600000));
		for(Player player : Bukkit.getOnlinePlayers()){
			Profile socialSpy = plugin.getProfileManager().getProfile(player);
			if(socialSpy != null && socialSpy != to && socialSpy != from && socialSpy.getSetting(Setting.SOCIAL_SPY)
					&& SocialSpyCommand.ACCESS.has(socialSpy)){
				socialSpy.sendMessage(M.DGRAY + "[PM] " + fromName + " to " + toName + ": " + message);
			}
		}
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
