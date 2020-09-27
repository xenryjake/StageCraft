package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameStatus;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerMode;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HNSModeCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSModeCommand(HideNSeekManager manager) {
		super(manager, Rank.MEMBER, "hide", "hider", "seek", "seeker", "spec", "spectate", "spectator");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(manager.getStatus() == GameStatus.NONE) {
			sender.sendMessage(err + "There is no game.");
			return;
		}
		PlayerMode mode;
		if(label.startsWith("h")) {
			mode = PlayerMode.HIDER;
		} else if(label.startsWith("sp")) {
			mode = PlayerMode.SPECTATOR;
		} else if(label.startsWith("s")) {
			mode = PlayerMode.SEEKER;
		} else {
			sender.sendMessage(err + "Invalid mode.");
			return;
		}
		Player target;
		if(args.length < 1) {
			sender.sendMessage(err + "Please specify a player.");
			return;
		}
		target = Bukkit.getPlayer(args[0]);
		if(target == null) {
			sender.sendMessage(err + "Could not find player " + args[0] + ".");
			return;
		}
		if(!manager.getPlayerHandler().isInGame(target)) {
			sender.sendMessage(err + target.getName() + " is not in the game.");
			return;
		}
		if(manager.getPlayerHandler().getPlayerMode(target) == mode) {
			sender.sendMessage(err + target.getName() + " is already in that mode.");
			return;
		}
		manager.getPlayerHandler().setMode(target, mode);
		sender.sendMessage(msg + target.getName() + " is now a " + mode.getColoredName() + msg + ".");
		target.sendMessage(msg + "You are now a " + mode.getColoredName() + msg + ".");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(manager.getStatus() == GameStatus.NONE) {
			profile.sendMessage(err + "There is no game.");
			return;
		}
		if(!profile.check(Rank.ADMIN) && manager.getStatus() != GameStatus.PRE_GAME) {
			profile.sendMessage(err + "You can only change mode before the game starts.");
			return;
		}
		PlayerMode mode;
		if(label.startsWith("h")) {
			mode = PlayerMode.HIDER;
		} else if(label.startsWith("sp")) {
			mode = PlayerMode.SPECTATOR;
		} else if(label.startsWith("s")) {
			mode = PlayerMode.SEEKER;
		} else {
			profile.sendMessage(err + "Invalid mode.");
			return;
		}
		Player target = profile.getPlayer();
		final boolean other = profile.check(Rank.MOD) && args.length >= 1;
		if(other) {
			target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				profile.sendMessage(err + "Could not find player " + args[0] + ".");
				return;
			}
		}
		if(!manager.getPlayerHandler().isInGame(target)) {
			profile.sendMessage(err + (other ? target.getName() + " is" : "You are") + " not in the game.");
			return;
		}
		if(manager.getPlayerHandler().getPlayerMode(target) == mode) {
			profile.sendMessage(err + (other ? target.getName() + " is" : "You are") + " already in that mode.");
			return;
		}
		manager.getPlayerHandler().setMode(target, mode);
		profile.sendMessage(msg + (other ? target.getName() + " is" : "You are") + " now a " + mode.getColoredName() + msg + ".");
		if(other) {
			target.sendMessage(msg + "You are now a " + mode.getColoredName() + msg + ".");
		}
	}
	
}
