package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.ArrayUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
public final class GameModeCommand extends Command<Creative,GameplayManager> {
	
	public static final Access OTHERS = Rank.HEAD_MOD;
	
	public GameModeCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "gamemode", "gm", "gmc", "gms", "gmsp", "gma");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		{
			String argToAdd = null;
			switch(label){
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
			profile.sendMessage(M.usage("/" + label + " <mode>" + (OTHERS.has(profile) ? " [player]" : "")));
			return;
		}
		
		Player target = profile.getPlayer();
		if(args.length >= 2 && OTHERS.has(profile)){
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
		
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <mode> <player>"));
			return;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[1]));
			return;
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
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		int len = args.length;
		label = label.replace("gm", "");
		if(label.startsWith("c") || label.startsWith("s") || label.startsWith("a")){
			len++;
		}
		switch(len){
			case 0:
			case 1:
				return Arrays.asList("creative", "survival", "adventure", "spectator");
			case 2:
				return OTHERS.has(profile) ? allLocalPlayers() : Collections.emptyList();
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		int len = args.length;
		label = label.replace("gm", "");
		if(label.startsWith("c") || label.startsWith("s") || label.startsWith("a")){
			len++;
		}
		switch(len){
			case 0:
			case 1:
				return Arrays.asList("creative", "survival", "adventure", "spectator");
			case 2:
				return allLocalPlayers();
			default:
				return Collections.emptyList();
		}
	}
	
}
