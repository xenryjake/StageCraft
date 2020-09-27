package com.xenry.stagecraft.survival.builder.commands;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderGameModeCommand extends Command<Survival,BuilderManager> {
	
	public BuilderGameModeCommand(BuilderManager manager){
		super(manager, Rank.MEMBER, "buildergamemode", "bgm", "bgmc", "bgms", "bgmsp", "bgma");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(!manager.isBuilder(profile.getPlayer())){
			profile.sendMessage(M.error("You are not in builder mode."));
			return;
		}
		GameMode gameMode;
		if(label.equalsIgnoreCase("bgmc")){
			gameMode = GameMode.CREATIVE;
		}else if(label.equalsIgnoreCase("bgms")){
			gameMode = GameMode.SURVIVAL;
		}else if(label.equalsIgnoreCase("bgmsp")){
			gameMode = GameMode.SPECTATOR;
		}else if(label.equalsIgnoreCase("bgma")){
			gameMode = GameMode.ADVENTURE;
		}else{
			if(args.length < 1){
				profile.sendMessage(M.error("Please specify a game mode."));
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
				profile.sendMessage(M.error("Invalid game mode."));
				return;
			}
		}
		profile.getPlayer().setGameMode(gameMode);
		profile.sendMessage(M.msg + "Your game mode is now " + M.elm + gameMode.toString() + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length <= 1 && (label.equals("bgm") || label.equals("buildergamemode"))){
			return Arrays.asList("creative", "survival", "adventure", "spectator");
		}
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
