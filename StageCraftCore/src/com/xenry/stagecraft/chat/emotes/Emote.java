package com.xenry.stagecraft.chat.emotes;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum Emote {
	
	//PUBLIC ACCESS
	SMILE("☺", ":)", "(:"),
	FROWN("☹", ":(", "):"),
	LEFT("←", ":left:"),
	UP("↑", ":up:"),
	RIGHT("→", ":right:"),
	DOWN("↓", ":down:"),
	POUND("£", ":pound:", ":gbp:"),
	YEN("¥", ":yen:", ":yuan:", ":jpy:", ":cny:"),
	CENT("¢", ":cent:"),
	EURO("€", ":euro:", ":eur:"),
	DOLLAR("$", ":dollar:", ":usd:", ":cad:", ":aud:", ":mxn:", ":peso:"),
	RUBLE("₽", ":ruble:", ":rub:"),
	INFINITY("∞", ":infinity:"),
	PATENT("℗", "(p)"),
	COPYRIGHT("©", "(c)"),
	REGISTERED_TRADEMARK(Rank.PREMIUM, "®", "(r)"),
	TRADEMARK("™", ":tm:", ":trademark:"),
	MULTIPLY("×", ":multiply:", ":times:"),
	DIVIDE("÷", ":divide:"),
	SQUARE_ROOT("√", ":sqrt:", ":squareroot:", ":square_root:"),
	APPROX("≈", ":approx:"),
	MINUS_PLUS("∓", ":-+:"),
	PLUS_MINUS("±", ":+-:"),
	INVERTED_QUESTION("¿", ":?:"),
	INVERTED_EXCLAMATION("¡", ":!:"),
	INTERROBANG("‽", ":interrobang:", ":?!:", ":!?:"),
	
	//PREMIUM ACCESS
	HEART(Rank.PREMIUM, "§c❤§r", "<3"),
	FIRE(Rank.PREMIUM, ChatColor.of("#ff9900") + "\uD83D\uDD25§r", ":fire:"),
	UMBRELLA(Rank.PREMIUM, ChatColor.of("#3498db") + "☔§r",":umbrella:"),
	X(Rank.PREMIUM, ChatColor.of("#ff0000") + "❌§r", ":x:"),
	CHECK(Rank.PREMIUM, ChatColor.of("#00ff00") + "✔§r", ":check:"),
	STAR(Rank.PREMIUM, "§e⭐§r", ":star:"),
	PICKAXE(Rank.PREMIUM, "⛏", ":pickaxe:", ":pick:"),
	ANCHOR(Rank.PREMIUM, "§7⚓§r", ":anchor:"),
	RAINCLOUD(Rank.PREMIUM, ChatColor.of("#8ff0ff") + "\uD83C\uDF27§r", ":raincloud:"),
	COMET(Rank.PREMIUM, "☄", ":comet:", ":meteor:", ":meteorite:"),
	CLOCK(Rank.PREMIUM, "⌚", ":clock:", ":time:"),
	FLAG(Rank.PREMIUM, "⚐", ":flag:"),
	PENCIL(Rank.PREMIUM, "✎", ":pencil:"),
	SPADE_SUIT(Rank.PREMIUM, "♠", ":spadesuit:", ":club_suit:"),
	CLUB_SUIT(Rank.PREMIUM, "♣", ":clubsuit:", ":club_suit:"),
	HEART_SUIT(Rank.PREMIUM, "♥", ":heartsuit:", ":heart_suit:"),
	DIAMOND_SUIT(Rank.PREMIUM, "♦", ":diamondsuit:", ":diamond_suit:"),
	SIGMA(Rank.PREMIUM, "∑", ":sigma:", ":sum:"),
	NOT_EQUAL(Rank.PREMIUM, "≠", ":notequal:", ":not_equal:"),
	SKULL(Rank.PREMIUM, "☠", ":skull:"),
	LEFT_RIGHT(Rank.PREMIUM, "↔", ":leftright:"),
	SWORDS(Rank.PREMIUM, "⚔", ":swords:"),
	EJECT(Rank.PREMIUM, "⏏", ":eject:"),
	MAIL(Rank.PREMIUM, "✉", ":mail:", ":email:"),
	MUSIC(Rank.PREMIUM, "♫", ":music:"),
	MALE(Rank.PREMIUM, "♀", ":male:"),
	FEMALE(Rank.PREMIUM, "♂", ":female:"),
	INTERSEX(Rank.PREMIUM, "⚥", ":intersex:"),
	SUN(Rank.PREMIUM, "§e☀§r", ":sun:"),
	MOON(Rank.PREMIUM, ChatColor.of("#7f8c8d") + "☽§r", ":moon:"),
	YIN_YANG(Rank.PREMIUM, "☯", ":yinyang:", ":yin_yang:"),
	BOX(Rank.PREMIUM, "☐", ":box:"),
	CHECK_BOX(Rank.PREMIUM, "☑", ":checkbox:", ":check_box:"),
	X_BOX(Rank.PREMIUM, "☒", ":xbox:", ":x_box:"),
	PEACE(Rank.PREMIUM, "☮", ":peace:"),
	POINT_LEFT(Rank.PREMIUM, "☜", ":pointleft:", ":point_left:"),
	POINT_RIGHT(Rank.PREMIUM, "☞", ":pointright:", ":point_right:"),
	BULLSEYE(Rank.PREMIUM, "⓪", ":bullseye:"),
	WARNING(Rank.PREMIUM, "§c⚠§r", ":warning:", ":warn:"),
	DIE_1(Rank.PREMIUM, "⚀", ":die1:", ":dice1:"),
	DIE_2(Rank.PREMIUM, "⚁", ":die2:", ":dice2:"),
	DIE_3(Rank.PREMIUM, "⚂", ":die3:", ":dice3:"),
	DIE_4(Rank.PREMIUM, "⚃", ":die4:", ":dice4:"),
	DIE_5(Rank.PREMIUM, "⚄", ":die5:", ":dice5:"),
	DIE_6(Rank.PREMIUM, "⚅", ":die6:", ":dice6:");
	
	public final Access access;
	public final String[] keywords;
	public final String replacement;
	
	Emote(String replacement, String...keywords){
		this(Access.TRUE, replacement, keywords);
	}
	
	Emote(Access access, String replacement, String...keywords) {
		this.access = access;
		this.keywords = keywords;
		this.replacement = replacement;
	}
	
	public static String replaceAllEmotes(String message){
		return replaceAllEmotes(message, ChatColor.RESET);
	}
	
	public static String replaceAllEmotes(String message, ChatColor defaultColor){
		for(Emote emote : values()){
			String replacement = emote.replacement;
			if(replacement.endsWith("§r")){
				replacement = replacement.replace("§r", defaultColor.toString());
			}
			for(String keyword : emote.keywords){
				message = message.replace(keyword, replacement);
			}
		}
		return defaultColor + message;
	}
	
	public static String replaceEmotes(String message, Profile profile){
		return replaceEmotes(message, ChatColor.RESET, profile);
	}
	
	public static String replaceEmotes(String message, ChatColor defaultColor, Profile profile){
		for(Emote emote : values()){
			if(!emote.access.has(profile)){
				continue;
			}
			String replacement = emote.replacement;
			if(replacement.contains("§r")){
				replacement = replacement.replace("§r", defaultColor.toString());
			}
			for(String keyword : emote.keywords){
				message = message.replace(keyword, replacement);
			}
		}
		return defaultColor + message;
	}
	
	public static List<Emote> getAvailableEmotes(Profile profile){
		List<Emote> emotes = new ArrayList<>();
		for(Emote emote : values()){
			if(emote.access.has(profile)){
				emotes.add(emote);
			}
		}
		return emotes;
	}
	
	@Override
	public String toString() {
		return replacement;
	}
	
}
