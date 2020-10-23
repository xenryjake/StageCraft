package com.xenry.stagecraft.punishment.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeLength;
import com.xenry.stagecraft.util.time.TimeUtil;
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
public final class PunishmentViewCommand extends Command<Core,PunishmentManager> {
	
	public static final int ITEMS_PER_PAGE = 10;
	
	public PunishmentViewCommand(PunishmentManager manager){
		super(manager, Rank.MOD, "view", "see", "lookup");
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
				type = Punishment.Type.KICK;
				break;
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
		
		//this downloads from db when player is offline
		List<Punishment> list = manager.getPunishments(target, type);
		if(list.isEmpty()){
			sender.sendMessage(M.error(M.elm + target.getLatestUsername() + M.err + " doesn't have any " + M.elm + type.name + M.err + " punishments on their record."));
			return;
		}
		
		int page = 1;
		if(args.length > 2){
			try{
				page = Integer.parseInt(args[2]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Please enter a valid integer."));
				return;
			}
		}
		
		int maxPages = (int) Math.ceil(list.size() / (double)ITEMS_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		
		int firstItem = (page-1) * ITEMS_PER_PAGE;
		int itemsOnThisPage = Math.min(list.size() - firstItem, ITEMS_PER_PAGE);
		if(list.size() > ITEMS_PER_PAGE){
			sender.sendMessage(M.msg + "Showing " + M.elm + type.name + M.msg + " punishments, page " + M.elm + page + M.msg + " of " + M.elm + maxPages + M.msg + " (" + M.elm + (firstItem+1) + M.msg + "-" + M.elm + (firstItem + itemsOnThisPage) + M.msg + " of " + M.elm + list.size() + M.msg + "):");
		}else{
			sender.sendMessage(M.msg + "Showing all " + M.elm + type.name + M.msg + " punishments:");
		}
		List<Punishment> punishmentsToDisplay = list.subList(firstItem, firstItem + itemsOnThisPage);
		for(Punishment pun : punishmentsToDisplay){
			String punishedByString;
			if(pun.getPunishedByUUID().equals(M.CONSOLE_NAME)){
				punishedByString = M.CONSOLE_NAME;
			}else{
				Profile punishedBy = manager.plugin.getProfileManager().getProfileByUUID(pun.getPunishedByUUID());
				if(punishedBy == null){
					punishedByString = pun.getPunishedByUUID();
				}else{
					punishedByString = punishedBy.getLatestUsername();
				}
			}
			sender.sendMessage(M.arrow((pun.isActive() ? "§a§lACTIVE§r " + M.msg : "")) + type.name + " at " + M.elm + TimeUtil.dateFormat(pun.getTimestamp() * TimeLength.SECOND) + M.msg + " by " + M.elm + punishedByString + M.msg + (pun.getReason().isEmpty() ? "" : " Reason: " + M.WHITE + pun.getReason()));
		}
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
				return Arrays.asList("ban", "mute", "kick");
			default:
				return Collections.emptyList();
		}
	}
	
}
