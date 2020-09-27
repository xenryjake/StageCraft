package com.xenry.stagecraft.survival.gameplay.pvptoggle;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.Setting;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PvPCommand extends Command<Survival,GameplayManager> {
	
	//todo add ** selector
	
	public static final Access OTHER_ACCESS = Rank.ADMIN;
	
	public PvPCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "pvp");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <on|off> <player>"));
			return;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if(target == null){
			sender.sendMessage(M.error("Could not find player."));
			return;
		}
		Profile profile = getCore().getProfileManager().getProfile(target);
		if(profile == null){
			sender.sendMessage(M.error("Cannot find profile."));
			return;
		}
		args[0] = args[0].toLowerCase();
		boolean value;
		if(args[0].startsWith("on") || args[0].startsWith("en")){
			value = true;
		}else if(args[0].startsWith("off") || args[0].startsWith("dis")){
			value = false;
		}else{
			value = profile.getSetting(Setting.PVP_ENABLED);
			sender.sendMessage(M.elm + target.getName() + M.msg + "'s PvP is currently " + (value ? "§aon" : "§coff") + M.msg + ".");
			return;
		}
		profile.setSetting(Setting.PVP_ENABLED, value);
		sender.sendMessage(M.elm + target.getName() + M.msg + "'s PvP is now " + (value ? "§aon" : "§coff") + M.msg + ".");
		target.sendMessage(M.msg + "Your PvP is now " + (value ? "§aon" : "§coff") + M.msg + ".");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			if(OTHER_ACCESS.has(profile)){
				profile.sendMessage(M.usage("/" + label + " <on|off> [player]"));
			}else{
				profile.sendMessage(M.usage("/" + label + " <on|off>"));
			}
			return;
		}
		Player target = profile.getPlayer();
		if(args.length > 1 && OTHER_ACCESS.has(profile)){
			target = Bukkit.getPlayer(args[1]);
			if(target == null){
				profile.sendMessage(M.error("Could not find player."));
				return;
			}
		}
		Profile targetProfile = getCore().getProfileManager().getProfile(target);
		if(targetProfile == null){
			profile.sendMessage(M.error("Cannot find profile."));
			return;
		}
		args[0] = args[0].toLowerCase();
		boolean value;
		if(args[0].startsWith("on") || args[0].startsWith("en")){
			value = true;
		}else if(args[0].startsWith("off") || args[0].startsWith("dis")){
			value = false;
		}else{
			value = targetProfile.getSetting(Setting.PVP_ENABLED);
			profile.sendMessage((profile == targetProfile ? M.msg + "Your" : M.elm + target.getName() + M.msg + "'s") + " PvP is currently " + (value ? "§aon" : "§coff") + M.msg + ".");
		}
		targetProfile.setSetting(Setting.PVP_ENABLED, value);
		if(profile == targetProfile){
			profile.sendMessage(M.msg + "Your PvP is now " + (value ? "§aon" : "§coff") + M.msg + ".");
		}else{
			profile.sendMessage(M.elm + target.getName() + M.msg + "'s PvP is now " + (value ? "§aon" : "§coff") + M.msg + ".");
			target.sendMessage(M.msg + "Your PvP is now " + (value ? "§aon" : "§coff") + M.msg + ".");
		}
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
				return Arrays.asList("on", "off");
			case 2:
				return null;
			default:
				return Collections.emptyList();
		}
	}
	
}
