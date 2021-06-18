package com.xenry.stagecraft.chat;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.jetbrains.annotations.Nullable;

import static com.xenry.stagecraft.chat.ChatManager.COLOR_ACCESS;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 3/11/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum Channel {
	
	GLOBAL(Access.TRUE, true, (manager, profile, message) -> {
		if(profile == null || COLOR_ACCESS.has(profile)){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(profile == null){
			message = Emote.replaceAllEmotes(message, ChatColor.WHITE);
		}else{
			message = Emote.replaceEmotes(message, ChatColor.WHITE, profile);
		}
		ComponentBuilder cb = new ComponentBuilder();
		if(profile != null){
			String prefix = manager.getGlobalChatPrefix() + profile.getMainRank().getColoredPrefix();
			if(!ChatColor.stripColor(prefix).isEmpty() && !prefix.endsWith(" ")){
				prefix += " ";
			}
			cb.append(TextComponent.fromLegacyText("§r" + prefix));
			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new Text(TextComponent.fromLegacyText("§rMessage " + profile.getDisplayName())));
			ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + profile.getLatestUsername() + " ");
			cb.append(TextComponent.fromLegacyText(profile.getDisplayName())).event(he).event(ce);
		}else{
			cb.append(M.CONSOLE_NAME).color(ChatColor.YELLOW);
		}
		cb.append(TextComponent.fromLegacyText(M.DGRAY + ":§r ")).event((ClickEvent)null).event((HoverEvent)null)
				.append(TextComponent.fromLegacyText(message, ChatColor.WHITE));
		return cb.create();
	}),
	
	STAFF(Rank.MOD, false, (manager, profile, message) -> {
		if(profile == null || COLOR_ACCESS.has(profile)){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(profile == null){
			message = Emote.replaceAllEmotes(message, ChatColor.GRAY);
		}else{
			message = Emote.replaceEmotes(message, ChatColor.GRAY, profile);
		}
		String name = profile == null ? M.CONSOLE_NAME : profile.getColorlessDisplayName();
		
		return new ComponentBuilder("[STAFF CHAT] " + name).color(ChatColor.YELLOW)
				.append(": ").color(ChatColor.DARK_GRAY)
				.append(message).color(ChatColor.GRAY).create();
	}),
	
	ADMIN(Rank.ADMIN, false, (manager, profile, message) -> {
		if(profile == null || COLOR_ACCESS.has(profile)){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(profile == null){
			message = Emote.replaceAllEmotes(message, ChatColor.GRAY);
		}else{
			message = Emote.replaceEmotes(message, ChatColor.GRAY, profile);
		}
		String name = profile == null ? M.CONSOLE_NAME : profile.getColorlessDisplayName();
		
		return new ComponentBuilder("[ADMIN CHAT] " + name).color(ChatColor.DARK_RED)
				.append(": ").color(ChatColor.DARK_GRAY)
				.append(message).color(ChatColor.GRAY).create();
	});
	
	public final Access access;
	public final boolean isPublic;
	public final Formatter formatter;
	
	Channel(Access access, boolean isPublic, Formatter formatter) {
		this.access = access;
		this.isPublic = isPublic;
		this.formatter = formatter;
	}
	
	public BaseComponent[] format(ChatManager manager, @Nullable Profile profile, String message){
		return formatter.format(manager, profile, message);
	}
	
	interface Formatter {
		
		BaseComponent[] format(ChatManager manager, @Nullable Profile profile, String message);
		
	}
	
}
