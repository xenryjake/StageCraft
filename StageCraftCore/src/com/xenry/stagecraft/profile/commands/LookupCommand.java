package com.xenry.stagecraft.profile.commands;
import com.earth2me.essentials.Essentials;
import com.google.common.collect.Lists;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class LookupCommand extends Command<Core,ProfileManager> {
	
	//todo work cross-server
	
	public static final Access SENSITIVE_VIEW_ACCESS = Rank.ADMIN;
	
	private static final List<String> USERNAMES_PHRASES = Arrays.asList("usernames", "names", "username", "names");
	private static final List<String> ADDRESSES_PHRASES = Arrays.asList("addresses", "addrs", "address", "addr", "ip", "ips");
	
	public LookupCommand(ProfileManager manager){
		super(manager, Rank.MOD, "lookup", "whois");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		doLookup(profile.getPlayer(), args, label, SENSITIVE_VIEW_ACCESS.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		doLookup(sender, args, label, true);
	}
	
	private void doLookup(CommandSender sender, String[] args, String label, boolean detailedAccess){
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <username|uuid> [usernames|addresses]"));
			return;
		}
		Profile profile;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				profile = manager.getProfile(Bukkit.getPlayer(args[0]));
			}else{
				profile = manager.getProfileByLatestUsername(args[0]);
			}
		}else{
			profile = manager.getProfileByUUID(args[0]);
		}
		if(profile == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		if(args.length > 1){
			if(USERNAMES_PHRASES.contains(args[1].toLowerCase())){
				List<String> names = profile.getUsernames();
				sender.sendMessage(M.msg + "Showing " + M.elm + names.size() + M.msg + " logged usernames of " + M.elm + profile.getLatestUsername() + M.msg + ":");
				StringBuilder nameString = new StringBuilder();
				for(String name : Lists.reverse(names)) {
					nameString.append(M.WHITE).append(name).append(M.msg).append(", ");
				}
				sender.sendMessage(M.arrow(nameString.toString()));
				return;
			}else if(detailedAccess && ADDRESSES_PHRASES.contains(args[1].toLowerCase())){
				List<String> addresses = profile.getAddresses();
				sender.sendMessage(M.msg + "Showing " + M.elm + addresses.size() + M.msg + " logged addresses of " + M.elm + profile.getLatestUsername() + M.msg + ":");
				StringBuilder nameString = new StringBuilder();
				for(String name : Lists.reverse(addresses)) {
					nameString.append(M.WHITE).append(name).append(M.msg).append(", ");
				}
				sender.sendMessage(M.arrow(nameString.toString()));
				return;
			}
		}
		
		//Vector3D lastLoc = profile.getLastLocation();
		boolean online = profile.isOnline();

		sender.sendMessage(M.msg + "Profile of " + M.elm + profile.getLatestUsername() + M.msg + ":");
		sender.sendMessage(M.arrow("UUID: " + M.WHITE + profile.getUUID()));
		sender.sendMessage(M.arrow("Rank: " + profile.getRank().getColoredName()));
		sender.sendMessage(M.arrow("Logged Usernames: " + M.elm + profile.getUsernames().size() + M.gry + " §o(/lookup " + profile.getLatestUsername() + " names)"));
		sender.sendMessage(M.arrow("Total Playtime: " + M.WHITE + TimeUtil.simplerString(profile.getTotalPlaytime())));
		sender.sendMessage(M.arrow("Nickname: " + profile.getDisplayName()));
		if(detailedAccess){
			sender.sendMessage(M.arrow("Address: " + M.WHITE + profile.getLatestAddress()));
		}
		if(online){
			Essentials ess = manager.plugin.getIntegrationManager().getEssentials();
			if(ess != null){
				sender.sendMessage(M.arrow("AFK: " + (ess.getUser(profile.getPlayer()).isAfk() ? "§ayes" : "§cno")));
			}
		}else{
			//todo add to survival somehow?
			//sender.sendMessage(M.arrow("Last Location: " + M.elm + "(" + lastLoc.x + "," + lastLoc.y + "," + lastLoc.z + ") [" + profile.getLastLocationWorldName() + "]"));
		}
		//todo include both network-wide and per-server
		long time = online ? profile.getSecondsSinceLastLogin(manager.plugin.getServerName()) : profile.getSecondsSinceLastLogout(manager.plugin.getServerName());
		if(time <= 0){
			sender.sendMessage(M.arrow("Status: " + (online ? "§aOnline" : "§cOffline")));
		}else{
			sender.sendMessage(M.arrow("Status: " + (online ? "§aOnline " : "§cOffline ") + M.msg + "(" + M.elm + TimeUtil.simplerString(time) + M.msg + ")"));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? allNetworkPlayers() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? allNetworkPlayers() : Collections.emptyList();
	}
	
}
