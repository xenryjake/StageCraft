package com.xenry.stagecraft.survival.teleportation.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportMenu;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPCommand extends Command<Survival,TeleportationManager> {
	
	public static final Access SELF_RANK = Rank.HEAD_MOD;
	public static final Access OTHER_RANK = Rank.HEAD_MOD;
	
	public TPCommand(TeleportationManager manager){
		super(manager, SELF_RANK, "tp", "tpo");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			if(Bukkit.getOnlinePlayers().size() <= 1){
				profile.sendMessage(M.usage("/" + label + (OTHER_RANK.has(profile) ? " [player]" : "") + " <player-to>"));
			}else{
				new TeleportMenu(manager, profile.getUUID(), label).open(profile.getPlayer());
			}
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
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
