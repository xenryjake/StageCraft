package com.xenry.stagecraft.profile.commands;
import com.google.common.collect.Lists;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.PlayerState;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
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
	
	public static final Access SENSITIVE_VIEW_ACCESS = Rank.ADMIN;
	
	private static final List<String> USERNAMES_PHRASES = Arrays.asList("usernames", "names", "username", "names");
	private static final List<String> ADDRESSES_PHRASES = Arrays.asList("addresses", "addrs", "address", "addr", "ip", "ips");
	
	public LookupCommand(ProfileManager manager){
		super(manager, Rank.MOD, "lookup", "whois");
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
		
		PlayerState state = manager.plugin.getServerManager().getPlayerState(profile.getUUID());
		boolean onlineLocal = profile.isOnline();
		boolean onlineGlobal = state != null;

		sender.sendMessage(M.msg + "Profile of " + M.elm + profile.getLatestUsername() + M.msg + ":");
		sender.sendMessage(M.arrow("UUID: " + M.WHITE + profile.getUUID()));
		sender.sendMessage(M.arrow("Rank: " + profile.getRank().getColoredName()));
		sender.sendMessage(M.arrow("Logged Usernames: " + M.elm + profile.getUsernames().size() + M.gry + M.ITALIC + " (/lookup " + profile.getLatestUsername() + " names)"));
		sender.sendMessage(M.arrow("Total Playtime: " + M.WHITE + TimeUtil.simplerString(profile.getTotalPlaytime())));
		sender.sendMessage(M.arrow("Nickname: " + profile.getDisplayName()));
		if(detailedAccess){
			sender.sendMessage(M.arrow("Address: " + M.WHITE + profile.getLatestAddress()));
		}
		if(onlineGlobal){
			sender.sendMessage(M.arrow("AFK: " + M.yesNo(state.isAFK())));
			sender.sendMessage(M.arrow("Vanished: " + M.yesNo(state.isVanished())));
		}
		
		ComponentBuilder cb = new ComponentBuilder(" » ").color(M.DGRAY).append("Status: ").color(M.msg);
		if(onlineGlobal){
			cb.append("Online").color(ChatColor.GREEN);
			cb.append(" at ").color(M.msg);
			cb.append(state.getServerName()).color(M.elm);
			if(onlineLocal){
				long time = profile.getSecondsSinceLastLogin(manager.plugin.getServerName());
				cb.append(" (").color(M.msg).append(TimeUtil.simplerString(time)).color(M.elm).append(")").color(M.msg);
			}else{
				cb.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + state.getServerName()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go to " + state.getServerName())));
			}
		}else{
			cb.append("Offline").color(ChatColor.RED);
			long time = profile.getMostRecentLogout();
			cb.append(" (").color(M.msg).append(TimeUtil.simplerString(time)).color(M.elm).append(")").color(M.msg);
		}
		sender.sendMessage(cb.create());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
}
