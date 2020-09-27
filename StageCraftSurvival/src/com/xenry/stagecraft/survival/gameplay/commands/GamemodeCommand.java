package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.ArrayUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class GamemodeCommand extends Command<Survival,GameplayManager> {
	
	//todo add ** selector
	
	public GamemodeCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "gamemode", "gm", "gmc", "gms", "gmsp", "gma", "creative", "survival",
				"spectator", "adventure");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		{
			String argToAdd = null;
			switch(label){
				case "creative":
				case "survival":
				case "spectator":
				case "adventure":
					argToAdd = label;
					break;
				case "gmc":
				case "gms":
				case "gmsp":
				case "gma":
					argToAdd = label.replace("gm", "");
					break;
			}
			if(argToAdd != null){
				args = ArrayUtil.insertAtStart(args, argToAdd);
				label = "gamemode";
			}
		}
		
		if(args.length < 1) {
			profile.sendMessage(M.usage("/" + label + " <mode> [player]"));
			return;
		}
		
		Player target = profile.getPlayer();
		if(args.length >= 2 && profile.check(Rank.ADMIN)){
			target = Bukkit.getPlayer(args[1]);
			if(target == null){
				profile.sendMessage(M.playerNotFound(args[1]));
				return;
			}
		}
		
		GameMode gameMode;
		if(args[0].toLowerCase().startsWith("a")){
			gameMode = GameMode.ADVENTURE;
		}else if(args[0].toLowerCase().startsWith("c")){
			gameMode = GameMode.CREATIVE;
		}else if(args[0].toLowerCase().startsWith("sp")){
			gameMode = GameMode.SPECTATOR;
		}else if(args[0].toLowerCase().startsWith("s")){
			gameMode = GameMode.SURVIVAL;
		}else{
			profile.sendMessage(M.error("Invalid gamemode."));
			return;
		}
		target.setGameMode(gameMode);
		if(profile.getPlayer() != target){
			profile.sendMessage(M.msg + "You have updated " + M.elm + target.getName() + M.msg + "'s gamemode to "
					+ M.elm + gameMode.toString().toLowerCase() + M.msg + ".");
		}
		target.sendMessage(M.msg + "Your gamemode is now " + M.elm + gameMode.toString().toLowerCase() + M.msg
				+ ".");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		{
			String argToAdd = null;
			switch(label){
				case "creative":
				case "survival":
				case "spectator":
				case "adventure":
					argToAdd = label;
					break;
				case "gmc":
				case "gms":
				case "gmsp":
				case "gma":
					argToAdd = label.replace("gm", "");
					break;
			}
			if(argToAdd != null){
				args = ArrayUtil.insertAtStart(args, argToAdd);
				label = "gamemode";
			}
		}
		
		GameMode gameMode;
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <mode> <player>"));
			return;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[1]));
			return;
		}
		if(args[0].toLowerCase().startsWith("a")){
			gameMode = GameMode.ADVENTURE;
		}else if(args[0].toLowerCase().startsWith("c")){
			gameMode = GameMode.CREATIVE;
		}else if(args[0].toLowerCase().startsWith("sp")){
			gameMode = GameMode.SPECTATOR;
		}else if(args[0].toLowerCase().startsWith("s")){
			gameMode = GameMode.SURVIVAL;
		}else{
			sender.sendMessage(M.error("Invalid gamemode."));
			return;
		}
		target.setGameMode(gameMode);
		sender.sendMessage(M.msg + "You have updated " + M.elm + target.getName() + M.msg + "'s gamemode to "
				+ M.elm + gameMode.toString().toLowerCase() + M.msg + ".");
		target.sendMessage(M.msg + "Your gamemode is now " + M.elm + gameMode.toString().toLowerCase() + M.msg
				+ ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return Arrays.asList("creative", "survival", "adventure", "spectator");
			case 2:
				return null;
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return Arrays.asList("creative", "survival", "adventure", "spectator");
			case 2:
				return null;
			default:
				return Collections.emptyList();
		}
	}
	
}
