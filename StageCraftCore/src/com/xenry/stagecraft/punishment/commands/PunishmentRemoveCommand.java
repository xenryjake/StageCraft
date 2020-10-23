package com.xenry.stagecraft.punishment.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
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
public final class PunishmentRemoveCommand extends Command<Core,PunishmentManager> {
	
	public PunishmentRemoveCommand(PunishmentManager manager){
		super(manager, Rank.MOD, "remove", "undo");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/punishments " + label + " <player> <type>"));
			return;
		}
		
		Profile target;
		if(args[0].length() <= 17) {
			if(Bukkit.getPlayer(args[0]) != null) {
				target = manager.plugin.getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
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
		
		Punishment.Type type;
		switch(args[1].toLowerCase()){
			case "kick":
			case "kicks":
				sender.sendMessage(M.error("Kicks cannot be removed."));
				return;
			case "ban":
			case "bans":
				type = Punishment.Type.BAN;
				break;
			case "mute":
			case "mutes":
				type = Punishment.Type.MUTE;
				break;
			default:
				sender.sendMessage(M.error("Invalid punishment type: " + args[1]));
				return;
		}
		
		//this queries db when player is offline
		List<Punishment> list = manager.getActivePunishments(target, type);
		if(list.isEmpty()){
			sender.sendMessage(M.error(M.elm + target.getLatestUsername() + M.err + " doesn't have any active " + M.elm + type.name + M.err + " punishments on their record."));
			return;
		}
		
		int i = 0;
		for(Punishment pun : list){
			i++;
			pun.setRemoved(true);
			manager.save(pun);
		}
		
		sender.sendMessage(M.msg + "Successfully removed " + M.elm + i + M.msg + " active " + M.elm + type.name + M.msg + " punishments from " + M.elm + target.getLatestUsername() + M.msg + "'s profile.");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return serverTabComplete(profile.getPlayer(), args, label);
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return null;
			case 2:
				return Arrays.asList("ban", "mute");
			default:
				return Collections.emptyList();
		}
	}
	
}
