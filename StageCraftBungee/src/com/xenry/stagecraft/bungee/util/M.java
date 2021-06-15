package com.xenry.stagecraft.bungee.util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import static net.md_5.bungee.api.ChatColor.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/9/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class M {
	
	private M(){}
	
	public static final ChatColor msg = DARK_AQUA;
	public static final ChatColor err = RED;
	public static final ChatColor elm = YELLOW;
	public static final ChatColor gry = GRAY;
	
	public static final ChatColor WHITE = ChatColor.WHITE;
	
	public static final String SERVER_NAME_FORMATTED = "§a§lStage§9§lCraft";
	public static final String SERVER_NAME_RAW = "StageCraft";
	public static final String CONSOLE_NAME = "Server";
	
	public static BaseComponent[] msg(String message){
		return new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText(message))).color(msg).create();
	}
	
	public static BaseComponent[] error(String message){
		return new ComponentBuilder("Error: ").color(DARK_RED)
				.append(new TextComponent(TextComponent.fromLegacyText(message))).color(RED).create();
	}
	
	public static BaseComponent[] usage(String usage){
		return new ComponentBuilder("Usage: ").color(err)
				.append(new TextComponent(TextComponent.fromLegacyText(usage))).color(WHITE).create();
	}
	
	public static BaseComponent[] arrow(String message) {
		return new ComponentBuilder(" » ").color(DARK_GRAY).bold(true)
				.append(new TextComponent(TextComponent.fromLegacyText(message))).color(msg).bold(false).create();
	}
	
	public static BaseComponent[] help(String command, String description){
		return new ComponentBuilder(" /" + command).color(elm)
				.append(" - " + new TextComponent(TextComponent.fromLegacyText(description))).color(gry).create();
	}
	
	public static BaseComponent[] playerNotFound(String playerName){
		return error("Player not found: " + playerName);
	}
	
	public static String bool(boolean value, String trueWord, String falseWord){
		return value ? "§a" + trueWord : "§c" + falseWord;
	}
	
	public static String trueFalse(boolean value){
		return bool(value, "true", "false");
	}
	
	public static String yesNo(boolean value){
		return bool(value, "yes", "no");
	}
	
	public static String onOff(boolean value){
		return bool(value, "on", "off");
	}
	
	public static String enabledDisabled(boolean value){
		return bool(value, "enabled", "disabled");
	}
	
}
