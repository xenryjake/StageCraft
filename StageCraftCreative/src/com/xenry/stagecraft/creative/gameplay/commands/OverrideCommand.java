package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class OverrideCommand extends Command<Creative,GameplayManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	
	public OverrideCommand(GameplayManager manager) {
		super(manager, Rank.HEAD_MOD, "override", "ov");
		addSubCommand(new OverrideListCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player sender = profile.getPlayer();
		if(args.length < 1){
			if(OTHERS.has(profile)){
				sender.sendMessage(M.usage("/" + label + " <on|off> [player]"));
			}else{
				sender.sendMessage(M.usage("/" + label + " <on|off>"));
			}
			return;
		}
		if(args.length > 1 && OTHERS.has(profile)) {
			serverPerform(sender, args, label);
			return;
		}
		boolean state;
		switch(args[0].toLowerCase()){
			case "on":
			case "en":
			case "enable":
				state = true;
				break;
			case "off":
			case "dis":
			case "disable":
				state = false;
				break;
			case "t":
			case "toggle":
				state = !manager.isPlayerOverride(sender);
				break;
			default:
				sender.sendMessage(M.error("Invalid state. Please specify on, off, or toggle."));
				return;
		}
		String enableDisable = M.enabledDisabled(state);
		if(state == manager.isPlayerOverride(sender)){
			sender.sendMessage(M.error("You already have override mode " + enableDisable + M.err + "."));
			return;
		}
		manager.setPlayerOverride(sender, state);
		sender.sendMessage(M.msg + "Your override mode has been " + enableDisable + M.msg + ".");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <on|off> <player>"));
			return;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[1]));
			return;
		}
		boolean self = sender == target;
		boolean state;
		switch(args[0].toLowerCase()){
			case "on":
			case "en":
			case "enable":
				state = true;
				break;
			case "off":
			case "dis":
			case "disable":
				state = false;
				break;
			case "t":
			case "toggle":
				state = !manager.isPlayerOverride(target);
				break;
			default:
				sender.sendMessage(M.error("Invalid state. Please specify on, off, or toggle."));
				return;
		}
		String enableDisable = M.enabledDisabled(state);
		if(state == manager.isPlayerOverride(target)){
			sender.sendMessage(M.error((self ? "You already have" : M.elm + target.getName() + M.err + " already has")
					+ " override mode " + enableDisable + M.err + "."));
			return;
		}
		manager.setPlayerOverride(target, state);
		target.sendMessage(M.msg + "Your override mode has been " + enableDisable + M.msg + ".");
		if(!self){
			sender.sendMessage(M.msg + "You have " + enableDisable + M.msg + " override mode for " + M.elm
					+ target.getName() + M.msg + ".");
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				return filter(Arrays.asList("on", "off", "toggle"), args[0]);
			case 2:
				return localPlayers(args[1]);
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 1:
				return filter(Arrays.asList("on", "off", "toggle"), args[0]);
			case 2:
				return localPlayers(args[1]);
			default:
				return Collections.emptyList();
		}
	}
	
}
