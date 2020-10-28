package com.xenry.stagecraft.creative.teleportation.commands;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPCommand extends Command<Creative,TeleportationManager> {
	
	public static final Access SELF_RANK = Rank.ADMIN;
	public static final Access OTHER_RANK = Rank.ADMIN;
	
	public TPCommand(TeleportationManager manager){
		super(manager, SELF_RANK, "tp", "tpo");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " [player] <player-to> "));
			return;
		}
		if(args.length > 1 && OTHER_RANK.has(profile)){
			serverPerform(profile.getPlayer(), args, label);
			return;
		}
		Player to = Bukkit.getPlayer(args[0]);
		if(to == null){
			profile.sendMessage(M.error("Player not found: " + args[0]));
			return;
		}
		doTeleport(profile.getPlayer(), profile.getPlayer(), to, !label.equalsIgnoreCase("tpo"));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <to>"));
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null){
			sender.sendMessage(M.error("Player not found: " + args[0]));
			return;
		}
		Player to = Bukkit.getPlayer(args[1]);
		if(to == null){
			sender.sendMessage(M.error("Player not found: " + args[1]));
			return;
		}
		doTeleport(sender, player, to, !label.equalsIgnoreCase("tpo"));
	}
	
	private void doTeleport(CommandSender teleporter, Player player, Player to, boolean safe){
		manager.createAndExecuteTeleportation(player, teleporter, player.getLocation(), to.getLocation(), Teleportation.Type.ADMIN, safe);
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
}