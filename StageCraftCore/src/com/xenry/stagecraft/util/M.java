package com.xenry.stagecraft.util;
import net.md_5.bungee.api.ChatColor;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class M {
	
	private M(){}
	
	public static final ChatColor msg = ChatColor.DARK_AQUA;
	public static final ChatColor err = ChatColor.RED;
	public static final ChatColor elm = ChatColor.YELLOW;
	public static final ChatColor gry = ChatColor.GRAY;
	
	public static final ChatColor WHITE = ChatColor.WHITE;
	public static final ChatColor DGRAY = ChatColor.DARK_GRAY;
	public static final ChatColor DRED  = ChatColor.DARK_RED;
	public static final ChatColor ORANG = ChatColor.of("#ff9900");
	
	public static final String BOLD = ChatColor.BOLD.toString();
	public static final String ITALIC = ChatColor.ITALIC.toString();
	
	public static final String CONSOLE_NAME = "Server";
	
	public static String error(String message){
		return ChatColor.DARK_RED + "Error: " + err + message;
	}
	
	public static String usage(String usage){
		return err + "Usage: " + ChatColor.WHITE + usage;
	}
	
	public static String arrow(String message) {
		return " §8§l»§r " + msg + message;
	}
	
	public static String help(String command, String description){
		return elm + "  /" + command + gry + " - " + description;
	}
	
	public static String playerNotFound(String playerName){
		return error("Player not found: " + playerName);
	}
	
	public static String trueFalse(boolean value, String trueWord, String falseWord){
		return value ? "§a" + trueWord : "§c" + falseWord;
	}
	
	public static String yesNo(boolean value){
		return trueFalse(value, "yes", "no");
	}
	
	public static String onOff(boolean value){
		return trueFalse(value, "on", "Off");
	}
	
	public static String enabledDisabled(boolean value){
		return trueFalse(value, "enabled", "disabled");
	}
	
}
