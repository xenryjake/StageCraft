package com.xenry.stagecraft.chat.emotes;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.profile.Rank;
import net.md_5.bungee.api.ChatColor;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum Emote {
	
	HEART("§c❤§r", "<3"),
	FIRE(ChatColor.of("#ff9900") + "\uD83D\uDD25§r", ":fire:"),
	UMBRELLA(ChatColor.of("#3498db") + "☔§r",":umbrella:"),
	X(ChatColor.of("#ff0000") + "❌§r", ":x:"),
	CHECK(ChatColor.of("#00ff00") + "✔§r", ":check:"),
	STAR("§e⭐§r", ":star:"),
	PICKAXE("⛏", ":pickaxe:", ":pick:"),
	ANCHOR("§7⚓§r", ":anchor:"),
	RAINCLOUD(ChatColor.of("#8ff0ff") + "\uD83C\uDF27§r", ":raincloud:"),
	COMET("☄", ":comet:", ":meteor:", ":meteorite:"),
	CLOCK("⌚", ":clock:", ":time:"),
	FLAG("⚐", ":flag:"),
	PENCIL("✎", ":pencil:"),
	SPADE_SUIT("♠", ":spadesuit:", ":club_suit:"),
	CLUB_SUIT("♣", ":clubsuit:", ":club_suit:"),
	HEART_SUIT("♥", ":heartsuit:", ":heart_suit:"),
	DIAMOND_SUIT("♦", ":diamondsuit:", ":diamond_suit:"),
	SIGMA("∑", ":sigma:", ":sum:"),
	NOT_EQUAL("≠", ":notequal:", ":not_equal:"),
	SKULL("☠", ":skull:"),
	LEFT("←", ":left:"),
	UP("↑", ":up:"),
	RIGHT("→", ":right:"),
	DOWN("↓", ":down:"),
	LEFT_RIGHT("↔", ":leftright:"),
	SWORDS("⚔", ":swords:"),
	EJECT("⏏", ":eject:"),
	MAIL("✉", ":mail:", ":email:"),
	SMILE("☺", ":)", "(:"),
	FROWN("☹", ":(", "):"),
	INFINITY("∞", ":infinity:"),
	PATENT("℗", "(p)"),
	COPYRIGHT("©", "(c)"),
	REGISTERED_TRADEMARK("®", "(r)"),
	TRADEMARK("™", ":tm:", ":trademark:"),
	MULTIPLY("×", ":multiply:", ":times:"),
	DIVIDE("÷", ":divide:"),
	SQUARE_ROOT("√", ":sqrt:", ":squareroot:", ":square_root:"),
	APPROX("≈", ":approx:"),
	INVERTED_QUESTION("¿", ":?:"),
	INVERTED_EXCLAMATION("¡", ":!:"),
	INTERROBANG("‽", ":interrobang:"),
	MINUS_PLUS("∓", ":-+:"),
	PLUS_MINUS("±", ":+-:"),
	MUSIC("♫", ":music:"),
	MALE("♀", ":male:"),
	FEMALE("♂", ":female:"),
	INTERSEX("⚥", ":intersex:"),
	SUN("§e☀§r", ":sun:"),
	MOON(ChatColor.of("#7f8c8d") + "☽§r", ":moon:"),
	YIN_YANG("☯", ":yinyang:", ":yin_yang:"),
	BOX("☐", ":box:"),
	CHECK_BOX("☑", ":checkbox:", ":check_box:"),
	X_BOX("☒", ":xbox:", ":x_box:"),
	PEACE("☮", ":peace:"),
	POINT_LEFT("☜", ":pointleft:", ":point_left:"),
	POINT_RIGHT("☞", ":pointright:", ":point_right:"),
	BULLSEYE("⓪", ":bullseye:"),
	WARNING("§c⚠§r", ":warning:", ":warn:"),
	POUND("£", ":pound:"),
	YEN("¥", ":yen:"),
	DIE_1("⚀", ":die1:", ":dice1:"),
	DIE_2("⚁", ":die2:", ":dice2:"),
	DIE_3("⚂", ":die3:", ":dice3:"),
	DIE_4("⚃", ":die4:", ":dice4:"),
	DIE_5("⚄", ":die5:", ":dice5:"),
	DIE_6("⚅", ":die6:", ":dice6:");
	
	public static final Access EMOTE_ACCESS = Rank.MEMBER;
	
	public final String[] keywords;
	public final String replacement;
	
	Emote(String replacement, String...keywords) {
		this.keywords = keywords;
		this.replacement = replacement;
	}
	
	public static String replaceEmotes(String message){
		return replaceEmotes(message, ChatColor.RESET);
	}
	
	public static String replaceEmotes(String message, ChatColor defaultColor){
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
	
	@Override
	public String toString() {
		return replacement;
	}
	
}
