package com.xenry.stagecraft.skyblock.island.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.util.LocationVector;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.NumberUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandInfoCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandInfoCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "info");
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		Island island;
		if(args.length < 1){
			island = manager.getIsland(sender.getPlayer().getLocation());
			if(island == null){
				sender.sendMessage(M.error("You aren't standing in an island! Stand in an island or specify an island to lookup."));
				sender.sendMessage(M.usage("/island info [name]"));
				return;
			}
		}else{
			island = manager.getIsland(args[0]);
			if(island == null){
				sender.sendMessage(M.error("Could not find island: " + args[0]));
				return;
			}
		}
		Profile owner = manager.getCore().getProfileManager().getProfileByUUID(island.getOwnerUUID());
		if(owner == null){
			sender.sendMessage(M.error("Couldn't find owner profile: " + island.getOwnerUUID()));
			return;
		}
		
		List<String> members = new ArrayList<>();
		for(String uuid : island.getMemberUUIDs()){
			Profile profile = manager.getCore().getProfileManager().getProfileByUUID(uuid);
			if(profile == null){
				sender.sendMessage(M.error("Couldn't find member profile: " + uuid));
				return;
			}
			members.add(profile.getDisplayName());
		}
		
		sender.sendMessage(M.msg + "Information about island " + M.elm + island.getID() + M.msg + ":");
		sender.sendMessage(M.arrow("Name: " + M.WHITE + island.getName()));
		sender.sendMessage(M.arrow("Position: " + M.WHITE + island.getX() + M.msg + ", " + M.WHITE + island.getZ()));
		sender.sendMessage(M.arrow("Owner: " + M.WHITE + owner.getDisplayName()));
		sender.sendMessage(M.arrow("Members: " + M.WHITE + Joiner.on(M.gry + ", " + M.WHITE).join(members)));
		LocationVector vector = island.getHomeVector();
		if(!vector.isZero()){
			sender.sendMessage(M.arrow("Home: " + M.elm + NumberUtil.displayAsHundredths(vector.x) + M.msg + ", " + M.elm + NumberUtil.displayAsHundredths(vector.y) + M.msg + ", " + M.elm + NumberUtil.displayAsHundredths(vector.z)));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
