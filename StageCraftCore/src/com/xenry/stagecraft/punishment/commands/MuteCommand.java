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
import com.xenry.stagecraft.util.time.TimeUtil;
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
public final class MuteCommand extends Command<Core,PunishmentManager> {
	
	public MuteCommand(PunishmentManager manager) {
		super(manager, Rank.MOD, "mute", "tempmute");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		doMute(player, args, label, profile.getUUID(), player, !Punishment.CAN_PUNISH_WITHOUT_REASON.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		Player pmscSender = PlayerUtil.getAnyPlayer();
		if(pmscSender == null){
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_RED + "WARNING! " + M.err + "No players are online this instance. If the player is online another instance, changes will not take effect until the player switches servers or relogs.");
			sender.sendMessage("");
		}
		doMute(sender, args, label, M.CONSOLE_NAME, pmscSender, false);
	}
	
	private void doMute(CommandSender sender, String[] args, String label, String punishedBy, Player pmscSender,
						boolean requiresReason) {
		if(args.length < 2) {
			sender.sendMessage(M.usage("/" + label + " <player> <duration> [reason]"));
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
		if(target == null) {
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		if(Punishment.IMMUNITY.has(target)){
			sender.sendMessage(M.error(target.getLatestUsername() + " is immune to punishment."));
			return;
		}
		
		Integer duration;
		if(args[1].equalsIgnoreCase("forever") || args[1].toLowerCase().startsWith("perm")){
			duration = -1;
		}else{
			duration = Punishment.parseTime(args[1]);
			if(duration == null){
				sender.sendMessage(M.error("Invalid time: " + args[1]));
				return;
			}
		}
		
		String reason = "";
		if(args.length > 2) {
			reason = Joiner.on(' ').join(Arrays.copyOfRange(args, 2, args.length));
		}else if(requiresReason){
			sender.sendMessage(M.error("You must specify a reason."));
			return;
		}
		Punishment mute = new Punishment(Punishment.Type.MUTE, target.getUUID(), punishedBy, reason, duration == -1 ? -1 : TimeUtil.nowSeconds() + duration, duration);
		LocalPunishmentExecution execution = new LocalPunishmentExecution(manager, mute, sender, pmscSender);
		execution.apply();
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return serverTabComplete(profile.getPlayer(), args, label);
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 1:
				return networkPlayers(args[0]);
			case 2:{
				args[1] = args[1].toLowerCase().replaceAll("[smhd]", "");
				try{
					int value = Integer.parseInt(args[1]);
					return filter(Arrays.asList(value + "s", value + "m", value + "h", value + "d"), args[1]);
				}catch(Exception ex){
					return filter(Collections.singletonList("forever"), args[1]);
				}
			}
			default:
				return Collections.emptyList();
		}
	}
	
}
