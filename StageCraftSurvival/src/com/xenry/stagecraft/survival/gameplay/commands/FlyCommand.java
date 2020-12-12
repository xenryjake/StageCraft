package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class FlyCommand extends Command<Survival,GameplayManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	
	public FlyCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "fly");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player target = profile.getPlayer();
		if(args.length >= 1 && OTHERS.has(profile)){
			target = Bukkit.getPlayer(args[0]);
			if(target == null){
				profile.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}
		
		boolean enabled = !target.getAllowFlight();
		target.setAllowFlight(enabled);
		if(!target.getAllowFlight()){
			target.setFlying(false);
		}
		target.sendMessage(M.msg + "Fly mode " + (enabled ? "§aenabled" : "§cdisabled") + M.msg + ".");
		if(profile.getPlayer() != target){
			profile.sendMessage(M.msg + "Fly mode " + (enabled ? "§aenabled" : "§cdisabled") + M.msg + " for "
					+ M.elm + target.getName() + M.msg + ".");
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		
		target.setFallDistance(0);
		boolean enabled = !target.getAllowFlight();
		target.setAllowFlight(enabled);
		if(!target.getAllowFlight()){
			target.setFlying(false);
		}
		target.sendMessage(M.msg + "Fly mode " + (enabled ? "§aenabled" : "§cdisabled") + M.msg + ".");
		sender.sendMessage(M.msg + "Fly mode " + (enabled ? "§aenabled" : "§cdisabled") + M.msg + " for "
				+ M.elm + target.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
