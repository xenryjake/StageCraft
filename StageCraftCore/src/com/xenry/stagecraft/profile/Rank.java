package com.xenry.stagecraft.profile;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Access;
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
	DONOR("Donor", ChatColor.AQUA, 10, MEMBER),
	PREMIUM("Premium", ChatColor.LIGHT_PURPLE, 20, DONOR, MEMBER),
	MOD("Mod", ChatColor.RED, 50, MEMBER),
	DONOR_MOD("Donor_Mod", ChatColor.RED, 50, MOD, DONOR, MEMBER),
	PREMIUM_MOD("Premium_Mod", ChatColor.RED, 50, MOD, PREMIUM, DONOR, MEMBER),
	ADMIN("Admin", ChatColor.DARK_RED, 100, MOD, PREMIUM, DONOR, MEMBER);
	
	private final String name;
	private final ChatColor color;
	private final int weight;
	private final List<Rank> inherits;
	
	Rank(String name, ChatColor color, int weight, Rank...inherits){
		this.name = name;
		this.color = color;
		this.weight = weight;
		this.inherits = Arrays.asList(inherits);
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
	
	//
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
