package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandTransferCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandTransferCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "transfer", "setowner");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/island transfer <player>"));
			return;
		}
		SkyBlockProfile sbProfile = manager.plugin.getSkyBlockProfileManager().getProfile(profile);
		if(sbProfile == null){
			profile.sendMessage(M.error("Could not find your SkyBlock profile."));
			return;
		}
		Island island = sbProfile.getActiveIsland(manager);
		if(island == null){
			profile.sendMessage(M.error("You do not have an active island."));
			return;
		}
		if(!island.getOwnerUUID().equals(profile.getUUID())){
			profile.sendMessage(M.error("You are not the owner of this island."));
			return;
		}
		Profile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = manager.getCore().getProfileManager().getProfileByLatestUsername(args[0]);
			}
		}else{
			target = manager.getCore().getProfileManager().getProfileByUUID(args[0]);
		}
		if(target == null){
			profile.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		if(!island.isMember(target.getUUID())){
			profile.sendMessage(M.error("This player isn't a member of your island."));
			return;
		}
		if(manager.getOwnedIslands(target).size() >= IslandManager.MAX_OWNED_ISLANDS_PER_PLAYER){
			profile.sendMessage(M.error(target.getLatestUsername() + " can't own any more islands."));
			return;
		}
		island.setOwnerUUID(target.getUUID());
		profile.sendMessage(M.msg + "You have transferred this island, " + M.elm + island.getID() + M.msg + ", to " + M.elm + target.getLatestUsername() + M.msg + ". You can no longer manage this island.");
		if(target.isOnline()){
			target.sendMessage(M.elm + target.getLatestUsername() + M.msg + " has transferred their island, " + M.elm + island.getID() + M.msg + ", to you. You can now manage this island.");
		}
		manager.sendMessageToIsland(island, M.msg + "Ownership of island " + M.elm + island.getID() + M.msg + " was transferred to " + M.elm + target.getLatestUsername() + M.msg + " by " + M.elm + profile.getLatestUsername() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
