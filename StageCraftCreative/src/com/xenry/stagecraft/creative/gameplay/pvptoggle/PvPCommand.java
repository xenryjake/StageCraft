package com.xenry.stagecraft.creative.gameplay.pvptoggle;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.Setting;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public final class PvPCommand extends Command<Creative,GameplayManager> {
	
	public static final Access OTHERS = Rank.HEAD_MOD;
	
	public PvPCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "pvp");
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
			value = profile.getSetting(Setting.CREATIVE_PVP_ENABLED);
			sender.sendMessage(M.elm + target.getName() + M.msg + "'s PvP is currently " + M.onOff(value) + M.msg + ".");
			return;
		}
		profile.setSetting(Setting.CREATIVE_PVP_ENABLED, value);
		sender.sendMessage(M.elm + target.getName() + M.msg + "'s PvP is now " + M.onOff(value) + M.msg + ".");
		target.sendMessage(M.msg + "Your PvP is now " + M.onOff(value) + M.msg + ".");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			if(OTHERS.has(profile)){
				profile.sendMessage(M.usage("/" + label + " <on|off> [player]"));
			}else{
				profile.sendMessage(M.usage("/" + label + " <on|off>"));
			}
			return;
		}
		Player target = profile.getPlayer();
		Profile targetProfile = profile;
		if(args.length > 1 && OTHERS.has(profile)){
			target = Bukkit.getPlayer(args[1]);
			if(target == null){
				profile.sendMessage(M.error("Could not find player."));
				return;
			}
			targetProfile = getCore().getProfileManager().getProfile(target);
			if(targetProfile == null){
				profile.sendMessage(M.error("Cannot find profile."));
				return;
			}
		}
		args[0] = args[0].toLowerCase();
		boolean value;
		if(args[0].startsWith("on") || args[0].startsWith("en")){
			value = true;
		}else if(args[0].startsWith("off") || args[0].startsWith("dis")){
			value = false;
		}else{
			value = targetProfile.getSetting(Setting.CREATIVE_PVP_ENABLED);
			profile.sendMessage((profile == targetProfile ? M.msg + "Your" : M.elm + target.getName() + M.msg + "'s") + " PvP is currently " + M.onOff(value) + M.msg + ".");
		}
		targetProfile.setSetting(Setting.CREATIVE_PVP_ENABLED, value);
		if(profile == targetProfile){
			profile.sendMessage(M.msg + "Your PvP is now " + M.onOff(value) + M.msg + ".");
		}else{
			profile.sendMessage(M.elm + target.getName() + M.msg + "'s PvP is now " + M.onOff(value) + M.msg + ".");
			target.sendMessage(M.msg + "Your PvP is now " + M.onOff(value) + M.msg + ".");
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				List<String> values = new ArrayList<>(Arrays.asList("on", "off"));
				if(OTHERS.has(profile)){
					values.add("view");
				}
				return filter(values, args[0]);
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
				return filter(new ArrayList<>(Arrays.asList("on", "off", "view")), args[0]);
			case 2:
				return localPlayers(args[1]);
			default:
				return Collections.emptyList();
		}
	}
	
}
