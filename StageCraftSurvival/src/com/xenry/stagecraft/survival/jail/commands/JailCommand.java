package com.xenry.stagecraft.survival.jail.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.Punishment;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.Jail;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.survival.jail.Sentence;
import com.xenry.stagecraft.survival.jail.SentenceExecution;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class JailCommand extends Command<Survival,JailManager> {
	
	public JailCommand(JailManager manager){
		super(manager, Rank.MOD, "jail");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		doJail(profile.getPlayer(), args, label, profile.getUUID());
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		doJail(sender, args, label, M.CONSOLE_NAME);
	}
	
	private void doJail(CommandSender sender, String[] args, String label, String punishedBy){
		if(args.length < 3){
			sender.sendMessage(M.usage("/" + label + " <player> <duration> <jail-name> [reason]"));
			return;
		}
		Profile target;
		if(args[0].length() <= 17) {
			if(Bukkit.getPlayer(args[0]) != null) {
				target = getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
			} else {
				target = getCore().getProfileManager().getProfileByLatestUsername(args[0]);
			}
		} else {
			target = getCore().getProfileManager().getProfileByUUID(args[0]);
		}
		if(target == null) {
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		if(Punishment.IMMUNITY.has(target) && target.getPlayer() != sender){
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
		
		Jail jail = manager.getJailHandler().getJail(args[2]);
		if(jail == null){
			sender.sendMessage(M.error("That jail doesn't exist: " + args[2]));
			return;
		}
		
		String reason = "";
		if(args.length > 3) {
			reason = Joiner.on(' ').join(Arrays.copyOfRange(args, 3, args.length));
		}
		
		Sentence sentence = new Sentence(jail, target.getUUID(), punishedBy, reason, duration == -1 ? -1 : TimeUtil.nowSeconds() + duration, duration);
		SentenceExecution execution = new SentenceExecution(manager, sentence, sender);
		execution.apply();
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return serverTabComplete(profile.getPlayer(), args, label);
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return allLocalPlayers();
			case 2:{
				args[1] = args[1].toLowerCase().replaceAll("[smhd]", "");
				try{
					int value = Integer.parseInt(args[1]);
					return Arrays.asList(value + "s", value + "m", value + "h", value + "d");
				}catch(Exception ex){
					return Collections.singletonList("forever");
				}
			}
			case 3:
				return manager.getJailHandler().getJailNameList();
			default:
				return Collections.emptyList();
		}
	}
	
}
