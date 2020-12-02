package com.xenry.stagecraft.profile.commands.rank;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.ProfileRankChangeEvent;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RankSetCommand extends Command<Core,ProfileManager> {
	
	public static final Access SEE_RANK_UPDATES = Rank.MOD;
	
	public RankSetCommand(ProfileManager manager) {
		super(manager, Rank.ADMIN, "set");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/rank set <player> <rank>"));
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
		Rank oldRank = target.getRank();
		target.setRank(rank);
		sender.sendMessage(M.msg + "You have set " + M.elm + target.getLatestUsername() + M.msg + "'s rank to " + rank.getColoredName() + M.msg + ".");
		if(target.isOnline() && !sender.getName().equals(target.getOnlinePlayerName())) {
			target.getPlayer().sendMessage(M.msg + "Your rank is now " + rank.getColoredName() + M.msg + ".");
		}
		manager.save(target);
		target.updateDisplayName();
		
		ProfileRankChangeEvent event = new ProfileRankChangeEvent(target, oldRank, rank);
		manager.plugin.getServer().getPluginManager().callEvent(event);
		
		String senderName = sender instanceof Player ? sender.getName() : M.CONSOLE_NAME;
		String rankUpdateMessage = M.elm + senderName + M.msg + " set " + M.elm + target.getLatestUsername() + M.msg + "'s rank to " + rank.getColoredName() + M.msg + ".";
		Log.toCS(rankUpdateMessage);
		for(Profile profile : manager.plugin.getProfileManager().getOnlineProfiles()){
			if(SEE_RANK_UPDATES.has(profile) && profile != target && profile.getPlayer() != sender){
				profile.sendMessage(rankUpdateMessage);
			}
		}
		
		Player pluginMessageSender = sender instanceof Player ? (Player)sender : PlayerUtil.getAnyPlayer();
		if(pluginMessageSender == null){
			sender.sendMessage(M.err + M.BOLD + "WARNING!" + M.err + " No players are online this instance. If the player is online another instance, the rank update will likely be overwritten.");
			return;
		}
		manager.getProfileRankUpdatePMSC().send(pluginMessageSender, target, senderName);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return serverTabComplete(profile.getPlayer(), args, label);
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return allNetworkPlayers();
			case 2:
				return Rank.getRankNames();
			default:
				return Collections.emptyList();
		}
	}
	
}
