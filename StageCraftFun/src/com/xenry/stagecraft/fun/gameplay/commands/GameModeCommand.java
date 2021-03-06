package com.xenry.stagecraft.fun.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.fun.Fun;
import com.xenry.stagecraft.fun.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.ArrayUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
public final class GameModeCommand extends Command<Fun,GameplayManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	
	public GameModeCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "gamemode", "gm", "gmc", "gms", "gmsp", "gma");
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
		String targetName = profile.getLatestUsername();
		if(args.length >= 2 && OTHERS.has(profile)){
			if(args[1].equals("**")){
				target = null;
				targetName = "everyone";
			}else{
				target = Bukkit.getPlayer(args[1]);
				if(target == null){
					profile.sendMessage(M.playerNotFound(args[1]));
					return;
				}
				targetName = target.getName();
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
		setGameMode(target, gameMode);
		if(profile.getPlayer() != target){
			profile.sendMessage(M.msg + "You have updated " + M.elm + targetName + M.msg + "'s gamemode to " + M.elm
					+ gameMode.toString().toLowerCase() + M.msg + ".");
		}
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
		Player target;
		String targetName;
		if(args[1].equals("**")){
			target = null;
			targetName = "everyone";
		}else{
			target = Bukkit.getPlayer(args[1]);
			if(target == null){
				sender.sendMessage(M.playerNotFound(args[1]));
				return;
			}
			targetName = target.getName();
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
		setGameMode(target, gameMode);
		sender.sendMessage(M.msg + "You have updated " + M.elm + targetName + M.msg + "'s gamemode to " + M.elm
				+ gameMode.toString().toLowerCase() + M.msg + ".");
	}
	
	private void setGameMode(@Nullable Player target, GameMode gameMode){
		List<Player> targets;
		if(target == null){
			targets = new ArrayList<>(Bukkit.getOnlinePlayers());
		}else{
			targets = new ArrayList<>();
			targets.add(target);
		}
		for(Player player : targets){
			player.setGameMode(gameMode);
			player.sendMessage(M.msg + "Your gamemode is now " + M.elm + gameMode.toString().toLowerCase() + M.msg
					+ ".");
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		label = label.replace("gm", "");
		int mod = label.startsWith("c") || label.startsWith("s") || label.startsWith("a") ? 1 : 0;
		switch(args.length + mod){
			case 1:
				return filter(Arrays.asList("creative", "survival", "adventure", "spectator"), args[-mod]);
			case 2:
				return OTHERS.has(profile) ? localPlayers(args[1-mod], "**") : Collections.emptyList();
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		label = label.replace("gm", "");
		int mod = label.startsWith("c") || label.startsWith("s") || label.startsWith("a") ? 1 : 0;
		switch(args.length + mod){
			case 1:
				return filter(Arrays.asList("creative", "survival", "adventure", "spectator"), args[-mod]);
			case 2:
				return localPlayers(args[1-mod], "**");
			default:
				return Collections.emptyList();
		}
	}
	
}
