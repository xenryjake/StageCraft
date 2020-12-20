package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.Vector2DInt;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandCreateCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandCreateCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "create", "new");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/island create <id>"));
			return;
		}
		String id = args[0];
		if(!id.matches("^[A-Za-z0-9_]+$")){
			profile.sendMessage(M.error("Island IDs can only contain alphanumeric characters and underscores."));
			return;
		}
		if(id.replaceAll("[0-9_]", "").isEmpty()){
			profile.sendMessage(M.error("Island IDs names must contain at least one letter."));
			return;
		}
		if(id.length() > 32 || id.length() < 2){
			profile.sendMessage(M.error("Island IDs names must be between 2 and 32 characters long."));
			return;
		}
		if(manager.getIsland(id) != null){
			profile.sendMessage(M.error("An island with that ID already exists."));
			return;
		}
		if(manager.getOwnedIslands(profile).size() >= IslandManager.MAX_OWNED_ISLANDS_PER_PLAYER){
			profile.sendMessage(M.error("You can't own any more islands."));
			return;
		}
		if(manager.getJoinedIslands(profile).size() >= IslandManager.MAX_JOINED_ISLANDS_PER_PLAYER){
			profile.sendMessage(M.error("You can't join any more islands."));
			return;
		}
		
		Vector2DInt pos = manager.getAvailableIslandPosition();
		Island island = new Island(manager, id, profile.getPlayer(), pos.x, pos.z);
		profile.sendMessage(M.msg + "Creating new island...");
		boolean success = manager.createIsland(island);
		if(success){
			profile.sendMessage(M.msg + "Create a new island (" + M.elm + id + M.msg + ") at position " + M.elm + pos.x + M.msg + ", " + M.elm + pos.z + M.msg + ".");
			//todo teleport player, switch them to profile
		}else{
			profile.sendMessage(M.error("Island creation failed! Please message an admin."));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
