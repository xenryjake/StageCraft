package com.xenry.stagecraft.profile.commands.rank;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RankCommand extends Command<Core,ProfileManager> {
	
	public RankCommand(ProfileManager manager){
		super(manager, Rank.HEAD_MOD, "rank");
		addSubCommand(new RankSetCommand(manager));
		addSubCommand(new RankListCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
		profile.sendMessage(M.msg + "Your rank is " + profile.getRank().getColoredName() + M.msg + ".");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "§lRank Commands:");
		sender.sendMessage(M.help(label + " list", "See all available ranks."));
		sender.sendMessage(M.help(label + " set", "Set a player's rank."));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("list", "set") : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("list", "set") : Collections.emptyList();
	}
	
}
