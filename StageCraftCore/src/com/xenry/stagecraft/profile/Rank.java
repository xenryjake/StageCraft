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
	PREMIUM("Premium", ChatColor.AQUA, 10, MEMBER),
	ELITE("Elite", ChatColor.LIGHT_PURPLE, 20, PREMIUM, MEMBER),
	MOD("Mod", ChatColor.RED, 80, MEMBER),
	ADMIN("Admin", ChatColor.DARK_RED, 100, false, MOD, PREMIUM, MEMBER);
	
	public static final Rank DEFAULT = MEMBER;
	
	public final String name;
	public final String prefix;
	public final ChatColor color;
	public final int weight;
	public final boolean playersCanAssign;
	private final List<Rank> inherits;
	private List<ChatColor> availableColors;
	private List<String> availableColorNames;
	
	private final Explicit explicit;
	
	Rank(String name, ChatColor color, int weight, Rank...inherits){
		this(name, "", color, weight, true, inherits);
	}
	
	Rank(String name, String prefix, ChatColor color, int weight, Rank...inherits){
		this(name, prefix, color, weight, true, inherits);
	}
	
	Rank(String name, ChatColor color, int weight, boolean playersCanAssign, Rank...inherits){
		this(name, "", color, weight, playersCanAssign, inherits);
	}
	
	Rank(String name, String prefix, ChatColor color, int weight, boolean playersCanAssign, Rank...inherits){
		this.name = name;
		this.prefix = prefix;
		this.color = color;
		this.weight = weight;
		this.playersCanAssign = playersCanAssign;
		this.inherits = Arrays.asList(inherits);
		explicit = new Explicit(this);
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
	
	public String getPlainPrefix() {
		return prefix;
	}
	
	public String getColoredPrefix() {
		return color + prefix;
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
		return this == DEFAULT || profile.check(this);
	}
	
	@Override
	public boolean has(@NotNull CommandSender sender) {
		if(this == DEFAULT){
			return true;
		}
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
			names.add(rank.name());
		}
		return names;
	}
	
	public static List<String> getRankNames(String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> names = new ArrayList<>();
		for(Rank rank : values()){
			if(rank.name().toLowerCase().startsWith(startsWith)){
				names.add(rank.name());
			}
		}
		return names;
	}
	
	public Explicit explicit(){
		return explicit;
	}
	
	@Override
	public String toString() {
		return name();
	}
	
	public static final class Explicit implements Access {
		
		public final Rank rank;
		
		private Explicit(Rank rank) {
			this.rank = rank;
		}
		
		@Override
		public boolean has(@NotNull Profile profile) {
			return profile.hasRankExplicit(rank);
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
		
	}
	
}
