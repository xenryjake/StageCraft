package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Invite;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.skyblock.profile.SkyBlockProfile;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
public final class IslandInviteCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandInviteCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "invite", "add");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/island invite <player>"));
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
		if(manager.getJoinedIslands(profile).size() >= IslandManager.MAX_JOINED_ISLANDS_PER_PLAYER){
			profile.sendMessage(M.error(target.getLatestUsername() + " can't join any more islands."));
			return;
		}
		
		Invite invite = manager.getInvite(target.getUUID(), island.getID());
		if(invite != manager.getInvite(target.getUUID(), island.getID())){
			manager.removeInvite(invite);
			profile.sendMessage(target.getLatestUsername() + M.msg + " is no longer invited to island " + M.elm + island.getID() + M.msg + ".");
			return;
		}
		invite = new Invite(target.getUUID(), island.getID());
		manager.addInvite(invite);
		profile.sendMessage(M.msg + "Invited " + M.elm + target.getLatestUsername() + M.msg + " to island " + M.elm + island.getID() + M.msg + ".");
		profile.sendMessage(M.msg + "Run this command again to revoke the invitation.");
		if(target.isOnline()){
			ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/island join " + island.getID());
			ComponentBuilder cb = new ComponentBuilder("You have been invited to island ").color(M.msg)
					.append(island.getID()).color(M.elm)
					.append("!\n").color(M.msg);
			cb.append("CLICK HERE").color(ChatColor.GREEN).event(ce).bold(true)
					.append(" or run ").color(M.msg).event((ClickEvent)null).bold(false)
					.append("/island join " + island.getID()).color(M.elm)
					.append(" to join the island!").color(M.msg);
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
