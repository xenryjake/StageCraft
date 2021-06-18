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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
public final class RankRemoveCommand extends Command<Core,ProfileManager> {
	
	public RankRemoveCommand(ProfileManager manager){
		super(manager, Rank.ADMIN, "remove");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/rank remove <player> <rank>"));
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
		if(!target.hasRankExplicit(rank)){
			sender.sendMessage(M.error(target.getLatestUsername() + " does not have rank " + rank.getColoredName() + M.err + "."));
		}
		
		target.removeRank(rank);
		manager.save(target);
		target.updateDisplayName();
		
		sender.sendMessage(M.msg + "You removed rank " + rank.getColoredName() + M.msg + " from " + M.elm
				+ target.getLatestUsername());
		/*if(target.isOnline() && !sender.getName().equals(target.getOnlinePlayerName())) {
			target.getPlayer().sendMessage(M.msg + "You no longer have rank " + rank.getColoredName() + M.msg + ".");
		}*/
		
		String senderName = sender instanceof Player ? sender.getName() : M.CONSOLE_NAME;
		String rankUpdateMessage = M.elm + senderName + M.msg + " removed rank " + rank.getColoredName()
				+ M.msg + " from " + M.elm + target.getLatestUsername();
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
		manager.getProfileRanksUpdatePMSC().send(pluginMessageSender, target, rank,
				ProfileRanksUpdateEvent.Action.REMOVE, 0L, senderName);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				return networkPlayers(args[0]);
			case 2:
				return Rank.getRankNames(args[1]);
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
			default:
				return Collections.emptyList();
		}
	}
	
}
