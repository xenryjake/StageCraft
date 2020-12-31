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
public final class IslandCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public IslandCommand(IslandManager manager){
		super(manager, Rank.MEMBER, "island", "islands", "is");
		addSubCommand(new IslandInfoCommand(manager));
		addSubCommand(new IslandCreateCommand(manager));
		addSubCommand(new IslandSwitchCommand(manager));
		addSubCommand(new IslandInviteCommand(manager));
		addSubCommand(new IslandJoinCommand(manager));
		addSubCommand(new IslandLeaveCommand(manager));
		//reset
		//delete
		addSubCommand(new IslandKickCommand(manager));
		addSubCommand(new IslandTransferCommand(manager));
		addSubCommand(new IslandRenameCommand(manager));
		addSubCommand(new IslandListCommand(manager));
		addSubCommand(new IslandHomeCommand(manager));
		addSubCommand(new IslandSetHomeCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Island Commands:");
		sender.sendMessage(M.help(label + " info", "Get info about the island you're standing on"));
		sender.sendMessage(M.help(label + " create", "Create a new island"));
		sender.sendMessage(M.help(label + " switch", "Switch to another one of your islands."));
		sender.sendMessage(M.help(label + " invite", "Invite a player to your current island"));
		sender.sendMessage(M.help(label + " join", "Join an island you've been invited to"));
		sender.sendMessage(M.help(label + " leave", "Leave your current island"));
		//sender.sendMessage(M.help(label + " reset", "Reset your current island"));
		//sender.sendMessage(M.help(label + " delete", "Delete your current island"));
		sender.sendMessage(M.help(label + " kick", "Kick a player from your current island"));
		sender.sendMessage(M.help(label + " transfer", "Transfer your current island to another player"));
		sender.sendMessage(M.help(label + " rename", "Rename your current island"));
		sender.sendMessage(M.help(label + " list", "List islands you are a part of"));
		sender.sendMessage(M.help(label + " home", "Go to the home point of your current island"));
		sender.sendMessage(M.help(label + " sethome", "Set the home point of your current island"));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("info", "create", "invite", "join", "leave", "reset", "delete", "kick", "transfer", "rename", "list", "home", "sethome"), args[0]) : Collections.emptyList();
	}
	
}
