package com.xenry.stagecraft.chat;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.commands.BroadcastCommand;
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
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
	
	private String globalChatPrefix;
	
	private PlayerChatPMSC playerChatPMSC;
	private PrivateMessagePMSC privateMessagePMSC;
	private StaffChatPMSC staffChatPMSC;
	private BroadcastPMSC broadcastPMSC;
	
	public ChatManager(Core plugin){
		super("Chat", plugin);
	}
	
	@Override
	protected void onEnable(){
		playerChatPMSC = new PlayerChatPMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(playerChatPMSC);
		
		privateMessagePMSC = new PrivateMessagePMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(privateMessagePMSC);
		
		staffChatPMSC = new StaffChatPMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(staffChatPMSC);
		
		broadcastPMSC = new BroadcastPMSC(this);
		plugin.getPluginMessageManager().registerSubChannel(broadcastPMSC);
		
		registerCommand(new ChatCommand(this));
		registerCommand(new BroadcastCommand(this));
		registerCommand(new FakeMessageCommand(this));
		registerCommand(new StaffChatCommand(this));
		registerCommand(new MessageCommand(this));
		registerCommand(new ReplyCommand(this));
		registerCommand(new SocialSpyCommand(this));
		registerCommand(new SayCommand(this));
		
		registerCommand(new EmotesCommand(this));
		
		setGlobalChatPrefix(plugin.getConfig().getString("global-chat-prefix", ""));
	}
	
	public PlayerChatPMSC getPlayerChatPMSC() {
		return playerChatPMSC;
	}
	
	public PrivateMessagePMSC getPrivateMessagePMSC() {
		return privateMessagePMSC;
	}
	
	public StaffChatPMSC getStaffChatPMSC() {
		return staffChatPMSC;
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
		if(!profile.check(chatRank)){
			player.sendMessage(M.error("The chat is currently silenced to rank " + chatRank.getColoredName() + M.err
					+ "."));
			Log.info("[Chat Attempt] " + event.getPlayer().getName() + ": " + event.getMessage());
			event.setCancelled(true);
			return;
		}
		
		PublicChatEvent chatEvent = new PublicChatEvent(event.isAsynchronous(), profile, globalChatPrefix,
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
		playerChatPMSC.send(player, message);
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
	 * Get the global chat prefix
	 * @return the prefix
	 */
	public String getGlobalChatPrefix() {
		return globalChatPrefix;
	}
	
	/**
	 * Set the global chat prefix
	 * @param globalChatPrefix the prefix
	 */
	public void setGlobalChatPrefix(String globalChatPrefix) {
		this.globalChatPrefix = globalChatPrefix == null ? "" :
				ChatColor.translateAlternateColorCodes('&', globalChatPrefix);
	}
	
	/* *
	 * Send a private message
	 * @param to sender of message (null if console)
	 * @param from recipient of message (null if console)
	 * @param message message to be sent
	 * /
	@Deprecated
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
		/*for(Player player : Bukkit.getOnlinePlayers()){
			Profile socialSpy = plugin.getProfileManager().getProfile(player);
			if(socialSpy != null && socialSpy != to && socialSpy != from && socialSpy.getSetting(Setting.SOCIAL_SPY)
					&& SocialSpyCommand.ACCESS.has(socialSpy)){
				socialSpy.sendMessage(M.DGRAY + "[PM] " + fromName + " to " + toName + ": " + message);
			}
		}
	}*/
	
	public void handlePrivateMessage(Profile from, String targetName, String message){
		if(from.getLatestUsername().equals(targetName)){
			from.sendMessage(M.error("You can't message yourself."));
			return;
		}
		
		PrivateMessageEvent event = new PrivateMessageEvent(from, targetName, message);
		plugin.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			return;
		}
		message = event.getMessage();
		
		if(COLOR_ACCESS.has(from)) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(Emote.EMOTE_ACCESS.has(from)) {
			message = Emote.replaceEmotes(message, ChatColor.WHITE);
		}
		
		privateMessagePMSC.send(from.getPlayer(), targetName, message);
	}
	
}
