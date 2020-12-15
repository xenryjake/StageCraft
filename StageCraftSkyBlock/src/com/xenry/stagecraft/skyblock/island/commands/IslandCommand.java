package com.xenry.stagecraft.skyblock.island.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class IslandCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "island", "islands", "is");
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Island Commands:");
		sender.sendMessage(M.help(label + " create", "Create a new island"));
		sender.sendMessage(M.help(label + " invite", "Invite a player to your current island"));
		sender.sendMessage(M.help(label + " join", "Join an island you've been invited to"));
		sender.sendMessage(M.help(label + " leave", "Leave your current island"));
		sender.sendMessage(M.help(label + " reset", "Reset your current island"));
		sender.sendMessage(M.help(label + " delete", "Delete your current island"));
		sender.sendMessage(M.help(label + " kick", "Kick a player from your current island"));
		sender.sendMessage(M.help(label + " transfer", "Transfer your current island to another player"));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("create", "invite", "join", "leave", "reset", "delete", "kick", "transfer"), args[0]) : Collections.emptyList();
	}
	
}
