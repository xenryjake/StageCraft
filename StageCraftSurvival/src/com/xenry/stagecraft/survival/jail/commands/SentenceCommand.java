package com.xenry.stagecraft.survival.jail.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SentenceCommand extends Command<Survival,JailManager> {
	
	public SentenceCommand(JailManager manager) {
		super(manager, Rank.MOD, "sentence", "jailsentence", "jailsentences");
		setCanBeDisabled(true);
		addSubCommand(new SentenceViewCommand(manager));
		addSubCommand(new SentenceRemoveCommand(manager));
		addSubCommand(new SentenceUpdateCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Jail Sentence Commands:");
		sender.sendMessage(M.help(label + " update", "Update jail sentences from the database."));
		sender.sendMessage(M.help(label + " view <player>", "View jail sentences for a player."));
		sender.sendMessage(M.help(label + " remove <player>", "Remove an active sentence from a player, but keep it in the database."));
		sender.sendMessage(M.help("jail <player> <duration> <jail-name>", "Sentence a player to jail."));
		sender.sendMessage(M.help("setjail <name>", "Set the location for a new jail."));
		sender.sendMessage(M.help("deljail <name>", "Delete an existing jail."));
		sender.sendMessage(M.help("jails", "View the list of jails."));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("update", "view", "remove") : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("update", "view", "remove") : Collections.emptyList();
	}
	
}
