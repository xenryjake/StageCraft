package com.xenry.stagecraft.punishment.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.punishment.LocalPunishmentExecution;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
public final class KickCommand extends Command<Core,PunishmentManager> {
	
	public KickCommand(PunishmentManager manager){
		super(manager, Rank.MOD, "kick");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		doKick(player, args, label, profile.getUUID(), player, !Punishment.CAN_PUNISH_WITHOUT_REASON.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		Player pmscSender = PlayerUtil.getAnyPlayer();
		if(pmscSender == null){
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_RED + "WARNING! " + M.err + "No players are online this instance. If the player is online another instance, changes will not take effect until the player switches servers or relogs.");
			sender.sendMessage("");
		}
		doKick(sender, args, label, M.CONSOLE_NAME, pmscSender, false);
	}
	
	private void doKick(CommandSender sender, String[] args, String label, String punishedBy, Player pmscSender,
						boolean requiresReason){
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player> [reason]"));
			return;
		}
		Profile target;
		if(args[0].length() <= 17) {
			Player player = Bukkit.getPlayer(args[0]);
			if(player != null) {
				target = manager.plugin.getProfileManager().getProfile(player);
			} else {
				target = manager.plugin.getProfileManager().getProfileByLatestUsername(args[0]);
			}
		} else {
			target = manager.plugin.getProfileManager().getProfileByUUID(args[0]);
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		if(!manager.plugin.getServerManager().getAllNetworkPlayers().contains(target.getLatestUsername())){
			sender.sendMessage(M.error("That player is not online."));
			return;
		}
		if(Punishment.IMMUNITY.has(target)){
			sender.sendMessage(M.error(target.getLatestUsername() + " is immune to punishment."));
			return;
		}
		
		String reason = "";
		if(args.length > 1){
			reason = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		}else if(requiresReason){
			sender.sendMessage(M.error("You must specify a reason."));
			return;
		}
		Punishment kick = new Punishment(Punishment.Type.KICK, target.getUUID(), punishedBy, reason);
		LocalPunishmentExecution execution = new LocalPunishmentExecution(manager, kick, sender, pmscSender);
		execution.apply();
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
}
