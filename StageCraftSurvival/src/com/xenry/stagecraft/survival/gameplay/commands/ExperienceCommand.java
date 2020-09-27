package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ExperienceCommand extends Command<Survival,GameplayManager> {
	
	public ExperienceCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "experience", "exp", "xp");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <clear|set|add|remove|view> <player> [levels]"));
			return;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[1]));
			return;
		}
		if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("reset")){
			target.setLevel(0);
			target.setExp(0);
			sender.sendMessage(M.msg + "You cleared " + M.elm + target.getName() + M.msg + "'s experience levels.");
			return;
		}else if(args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("see")
				|| args[0].equalsIgnoreCase("get")){
			sender.sendMessage(M.elm + target.getName() + M.msg + " has " + M.elm + target.getLevel() + " levels"
					+ M.msg + ".");
			return;
		}
		int value;
		try{
			value = Integer.parseInt(args[2].toLowerCase().replace("l",""));
		}catch(Exception ex) {
			sender.sendMessage(M.error("Please specify a valid number."));
			return;
		}
		
		boolean change;
		if(args[0].equalsIgnoreCase("set")){
			change = false;
		}else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give")){
			change = true;
		}else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("take")){
			change = true;
			value *= -1;
		}else{
			sender.sendMessage(M.error("Invalid action: " + args[0]));
			return;
		}
		
		target.setLevel(Math.max(change ? target.getLevel() + value : value, 0));
		sender.sendMessage(M.elm + target.getName() + M.msg + " now has " + M.elm + target.getLevel() + " levels"
				+ M.msg + ".");
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
				return Arrays.asList("clear","set","add","remove","view");
			case 2:
				return null;
			default:
				return Collections.emptyList();
		}
	}
	
}
