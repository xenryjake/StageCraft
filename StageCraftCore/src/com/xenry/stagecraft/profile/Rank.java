package com.xenry.stagecraft.profile;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum Rank implements Access {
	
	MEMBER("Member", ChatColor.GRAY, 0),
	PREMIUM("Premium", ChatColor.AQUA, 10,
			MEMBER),
	ELITE("Elite", ChatColor.LIGHT_PURPLE, 15,
			PREMIUM, MEMBER),
	MOD("Mod", ChatColor.RED, 50,
			MEMBER),
	PREMIUM_MOD("Premium_Mod", ChatColor.RED, 50,
			MOD, PREMIUM, MEMBER),
	ELITE_MOD("Elite_Mod", ChatColor.RED, 50,
			PREMIUM_MOD, MOD, ELITE, PREMIUM, MEMBER),
	HEAD_MOD("Head_Mod", ChatColor.RED, 75,
			MOD, MEMBER),
	PREMIUM_HEAD_MOD("Premium_Head_Mod", ChatColor.RED, 75,
			HEAD_MOD, PREMIUM_MOD, MOD, PREMIUM, MEMBER),
	ELITE_HEAD_MOD("Elite_Head_Mod", ChatColor.RED, 75,
			PREMIUM_HEAD_MOD, HEAD_MOD, ELITE_MOD, PREMIUM_MOD, MOD, ELITE, PREMIUM, MEMBER),
	ADMIN("Admin", ChatColor.DARK_RED, 100,
			ELITE_HEAD_MOD, PREMIUM_HEAD_MOD, HEAD_MOD, ELITE_MOD, PREMIUM_MOD, MOD, ELITE, PREMIUM, MEMBER);
	
	private final String name;
	private final ChatColor color;
	private final int weight;
	private final List<Rank> inherits;
	private List<ChatColor> availableColors;
	private List<String> availableColorNames;
	
	Rank(String name, ChatColor color, int weight, Rank...inherits){
		this.name = name;
		this.color = color;
		this.weight = weight;
		this.inherits = Arrays.asList(inherits);
	}
	
	public boolean hasAccessToColor(ChatColor color){
		return getAvailableColors().contains(color);
	}
	
	public List<ChatColor> getAvailableColors(){
		if(availableColors == null){
			availableColors = new ArrayList<>();
			for(Rank r : values()){
				if(this.check(r) && !availableColors.contains(r.color)){
					availableColors.add(r.color);
				}
			}
		}
		return availableColors;
	}
	
	public List<String> getAvailableColorNames(){
		if(availableColorNames == null){
			availableColorNames = new ArrayList<>();
			for(ChatColor color : getAvailableColors()){
				availableColorNames.add(color.getName());
			}
		}
		return availableColorNames;
	}
	
	public String getName(){
		return name;
	}
	
	public ChatColor getColor(){
		return color;
	}
	
	public String getColoredName(){
		return color + name;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public List<Rank> getInherits() {
		return inherits;
	}
	
	public boolean checkWeight(Rank rank){
		return checkWeight(rank, 0);
	}
	
	public boolean checkWeight(Rank rank, int mod){
		return this.weight + mod >= rank.weight;
	}
	
	public boolean check(Rank rank){
		return rank == this || this.inherits.contains(rank);
	}
	
	@Override
	public boolean has(@NotNull Profile profile) {
		return profile.getRank().check(this);
	}
	
	@Override
	public boolean has(@NotNull CommandSender sender) {
		if(sender instanceof Player) {
			Profile profile = Core.getInstance().getProfileManager().getProfile((Player)sender);
			if(profile == null){
				return false;
			}
			return has(profile);
		}
		return true;
	}
	
	public static List<String> getRankNames(){
		List<String> names = new ArrayList<>();
		for(Rank rank : values()){
			names.add(rank.getName());
		}
		return names;
	}
	
	@Override
	public String toString() {
		return name();
	}
	
}
