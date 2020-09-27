package com.xenry.stagecraft.punishment.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.punishment.PunishmentExecution;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class KickCommand extends Command<PunishmentManager> {
	
	public KickCommand(PunishmentManager manager){
		super(manager, Rank.MOD, "kick");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		doKick(profile.getPlayer(), args, label, profile.getUUID());
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		doKick(sender, args, label, Punishment.CONSOLE_NAME);
	}
	
	private void doKick(CommandSender sender, String[] args, String label, String punishedBy){
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player> [reason]"));
			return;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		
		Profile targetProfile = manager.plugin.getProfileManager().getProfile(target);
		if(targetProfile == null){
			sender.sendMessage(M.error(target.getName() + " doesn't have a profile."));
			return;
		}
		if(Punishment.IMMUNITY.has(targetProfile)){
			sender.sendMessage(M.error(target.getName() + " is immune to punishment."));
			return;
		}
		
		String reason = "";
		if(args.length > 1){
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < args.length; i++){
				sb.append(args[i]).append(" ");
			}
			reason = sb.toString().trim();
		}
		Punishment kick = new Punishment(Punishment.Type.KICK, target.getUniqueId().toString(), punishedBy, reason);
		PunishmentExecution execution = new PunishmentExecution(manager, kick, sender);
		execution.apply();
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
