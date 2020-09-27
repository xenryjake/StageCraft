package com.xenry.stagecraft.punishment.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PunishmentCommand extends Command<PunishmentManager> {
	
	public PunishmentCommand(PunishmentManager manager){
		super(manager, Rank.MOD, "punishment", "punishments", "punish", "pun", "punishs", "puns");
		addSubCommand(new PunishmentUpdateCommand(manager));
		addSubCommand(new PunishmentViewCommand(manager));
		addSubCommand(new PunishmentRemoveCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "§lPunishment Commands:");
		sender.sendMessage(M.help(label + " update", "Update punishments from the database."));
		sender.sendMessage(M.help(label + " view <player> <type>", "View punishments for a player."));
		sender.sendMessage(M.help(label + " remove <player> <type>", "Remove an active punishment from a player, but keep it in the database."));
		sender.sendMessage(M.help("kick <player> [reason]", "Kick a player from the server."));
		sender.sendMessage(M.help("mute <player> <duration> [reason]", "Mute a player from using chat."));
		sender.sendMessage(M.help("ban <player> <duration> [reason]", "Ban a player from the server."));
		sender.sendMessage(M.help("jail <player> <duration> <jail-name>", "Send a player to jail."));
		sender.sendMessage(M.help("setjail <name>", "Set the location for a new jail."));
		sender.sendMessage(M.help("deljail <name>", "Delete an existing jail."));
		sender.sendMessage(M.help("jails", "View the list of jails."));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("update", "view", "remove") : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("update", "view", "remove") : Collections.emptyList();
	}
}