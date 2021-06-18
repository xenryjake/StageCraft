package com.xenry.stagecraft.profile.commands.rank;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.ProfileRanksUpdateEvent;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.xenry.stagecraft.profile.ProfileRanksUpdatePMSC.SEE_RANK_UPDATES;

/**
 * MineTogether created by Henry Blasingame (Xenry) on 3/18/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RankTemporaryCommand extends Command<Core,ProfileManager> {

	public RankTemporaryCommand(ProfileManager manager){
		super(manager, Rank.ADMIN, "temporary", "temp");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 3){
			sender.sendMessage(M.usage("/rank temp <player> <rank> <time-adjustment>"));
			return;
		}
		
		Profile target;
		if(Bukkit.getPlayer(args[0]) != null) {
			target = manager.plugin.getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
		}else{
			target = manager.plugin.getProfileManager().getProfileByLatestUsername(args[0]);
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for " + args[0] + "."));
			return;
		}
		
		Rank rank;
		try{
			rank = Rank.valueOf(args[1].toUpperCase());
		}catch(Exception ex){
			sender.sendMessage(M.error(args[1].toUpperCase() + " is not a valid rank."));
			return;
		}
		if(rank == Rank.DEFAULT){
			sender.sendMessage(M.error("You can't modify the default rank."));
			return;
		}
		if(!rank.playersCanAssign && sender instanceof Player){
			sender.sendMessage(M.error("This rank can't be assigned in-game."));
			return;
		}
		
		Integer duration = parseTime(args[2]);
		if(duration == null){
			sender.sendMessage(M.error("Invalid time: " + args[2]));
			return;
		}
		if(duration == 0){
			sender.sendMessage(
					M.error("Please specify a nonzero time. If you want to remove a rank, use /rank remove."));
			return;
		}
		
		if(target.getSecondsUntilExpiry(rank) == -1L){
			sender.sendMessage(M.error(target.getLatestUsername() + " has rank " + rank.getColoredName() + M.err
					+ " permanently."));
			return;
		}
		long now = TimeUtil.nowSeconds();
		ProfileRanksUpdateEvent.Action action;
		boolean has = target.hasRankExplicit(rank);
		if(duration <= 0){
			if(!has){
				sender.sendMessage(M.error(target.getLatestUsername() + " does not have rank " + rank.getColoredName()
						+ M.err + ". Cannot remove time from non-assigned rank."));
				return;
			}
			long expiry = target.getExpiry(rank);
			if(expiry + duration <= now){
				target.removeRank(rank);
				action = ProfileRanksUpdateEvent.Action.REMOVE;
			}else{
				target.rankTemporarily(rank, duration);
				action = ProfileRanksUpdateEvent.Action.ADJUST;
			}
		}else{
			target.rankTemporarily(rank, duration);
			if(has){
				action = ProfileRanksUpdateEvent.Action.ADJUST;
			}else{
				action = ProfileRanksUpdateEvent.Action.ADD;
			}
		}
		manager.save(target);
		target.updateDisplayName();
		
		String timeString = M.arrow(duration >= 0 ? "+ " : "- ") + TimeUtil.simplerString(Math.abs(duration));
		
		sender.sendMessage(M.msg + "You " + action.pastTenseVerb + " rank " + rank.getColoredName() + M.msg + " "
				+ action.toFrom + " " + M.elm + target.getLatestUsername());
		if(duration > 0 || action == ProfileRanksUpdateEvent.Action.ADJUST){
			sender.sendMessage(timeString);
		}
		/*if(action != ProfileRanksUpdateEvent.Action.ADJUST && target.isOnline()
				&& !sender.getName().equals(target.getOnlinePlayerName())) {
			target.getPlayer().sendMessage(M.msg + "You now have rank " + rank.getColoredName() + M.msg + ".");
			//to do fix
		}*/
		
		String senderName = sender instanceof Player ? sender.getName() : M.CONSOLE_NAME;
		String rankUpdateMessage = M.elm + senderName + M.msg + " " + action.pastTenseVerb + " rank "
				+ rank.getColoredName() + M.msg + " " + action.toFrom + " " + M.elm + target.getLatestUsername();
		if(duration > 0 || action == ProfileRanksUpdateEvent.Action.ADJUST){
			rankUpdateMessage += "§r\n§r" + timeString;
		}
		Log.toCS(rankUpdateMessage);
		for(Profile profile : manager.getOnlineProfiles()){
			if(SEE_RANK_UPDATES.has(profile) && profile != target && profile.getPlayer() != sender){
				profile.sendMessage(rankUpdateMessage);
			}
		}
		
		Player pluginMessageSender = sender instanceof Player ? (Player)sender : PlayerUtil.getAnyPlayer();
		if(pluginMessageSender == null){
			sender.sendMessage(M.err + M.BOLD + "WARNING!" + M.err + " No players are online this instance." +
					" If the player is online another instance, the rank update will likely be overwritten.");
			return;
		}
		manager.getProfileRanksUpdatePMSC().send(pluginMessageSender, target, rank, action,
				action == ProfileRanksUpdateEvent.Action.REMOVE ? 0L : duration, senderName);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				return networkPlayers(args[0]);
			case 2:
				return Rank.getRankNames(args[1]);
			case 3:{
				args[2] = args[2].toLowerCase().replaceAll("[smhd]", "");
				try{
					int value = Integer.parseInt(args[2]);
					return filter(Arrays.asList(value + "s", value + "m", value + "h", value + "d"), args[2]);
				}catch(Exception ex){
					return Collections.emptyList();
				}
			}
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 1:
				return networkPlayers(args[0]);
			case 2:
				return Rank.getRankNames(args[1]);
			case 3:{
				args[2] = args[2].toLowerCase().replaceAll("[smhd]", "");
				try{
					int value = Integer.parseInt(args[2]);
					return filter(Arrays.asList(value + "s", value + "m", value + "h", value + "d"), args[2]);
				}catch(Exception ex){
					return Collections.emptyList();
				}
			}
			default:
				return Collections.emptyList();
		}
	}
	
	public static Integer parseTime(String string) {
		string = string.toLowerCase();
		if(!string.matches("^-?[0-9]*[a-z]*$")) {
			return null;
		}
		int value;
		try {
			value = Integer.parseInt(string.replaceAll("[^0-9-]", ""));
		} catch(Exception ex) {
			return null;
		}
		String unit = string.replaceAll("[^a-z]", "");
		if(unit.startsWith("mo")) {
			return null;
		}
		
		switch (unit) {
			case "seconds":
			case "second":
			case "secs":
			case "sec":
			case "s":
				return value;
			case "minutes":
			case "minute":
			case "mins":
			case "min":
			case "m":
				return value * 60;
			case "hours":
			case "hour":
			case "hrs":
			case "hr":
			case "h":
				return value * 3600;
			case "days":
			case "day":
			case "d":
				return value * 86400;
			default:
				return null;
		}
		
	}
	
}
