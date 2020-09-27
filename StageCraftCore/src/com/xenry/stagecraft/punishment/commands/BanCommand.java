package com.xenry.stagecraft.punishment.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.punishment.PunishmentExecution;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.util.M;
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
public final class BanCommand extends Command<PunishmentManager> {
	
	public BanCommand(PunishmentManager manager){
		super(manager, Rank.MOD, "ban");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		doBan(profile.getPlayer(), args, label, profile.getUUID());
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		doBan(sender, args, label, Punishment.CONSOLE_NAME);
	}
	
	private void doBan(CommandSender sender, String[] args, String label, String punishedBy) {
		if(args.length < 2) {
			sender.sendMessage(M.usage("/" + label + " <player> <duration> [reason]"));
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
			StringBuilder sb = new StringBuilder();
			for(int i = 2; i < args.length; i++) {
				sb.append(args[i]).append(" ");
			}
			reason = sb.toString().trim();
		}
		Punishment ban = new Punishment(Punishment.Type.BAN, target.getUUID(), punishedBy, reason, duration == -1 ? -1 : TimeUtil.nowSeconds() + duration, duration);
		PunishmentExecution execution = new PunishmentExecution(manager, ban, sender);
		execution.apply();
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
			case 2:{
				args[1] = args[1].toLowerCase().replaceAll("[smhd]", "");
				try{
					int value = Integer.parseInt(args[1]);
					return Arrays.asList(value + "s", value + "m", value + "h", value + "d");
				}catch(Exception ex){
					return Collections.singletonList("forever");
				}
			}
			default:
				return Collections.emptyList();
		}
	}
	
}