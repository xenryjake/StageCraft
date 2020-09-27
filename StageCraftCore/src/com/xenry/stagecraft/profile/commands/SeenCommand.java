package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.Vector3D;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

import static com.xenry.stagecraft.profile.commands.LookupCommand.SENSITIVE_VIEW_ACCESS;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SeenCommand extends Command<Core,ProfileManager> {
	
	public static final Access DETAILED_VIEW_ACCESS = Rank.MOD;
	
	public SeenCommand(ProfileManager manager){
		super(manager, Rank.MEMBER, "seen");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		doCommand(profile.getPlayer(), args, label, DETAILED_VIEW_ACCESS.has(profile), SENSITIVE_VIEW_ACCESS.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		doCommand(sender, args, label, true, true);
	}
	
	private void doCommand(CommandSender sender, String[] args, String label, boolean detailedViewAccess, boolean sensitiveViewAccess){
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Profile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = manager.getProfileByLatestUsername(args[0]);
			}
		}else{
			target = manager.getProfileByUUID(args[0]);
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		boolean online = target.isOnline();
		sender.sendMessage(M.elm + target.getLatestUsername() + M.msg + " has been " + (online ? "§aonline" : "§coffline") + M.msg + " for " + M.elm + (online ? TimeUtil.simplerString(target.getSecondsSinceLastLogin()) : TimeUtil.simplerString(target.getSecondsSinceLastLogout())));
		if(detailedViewAccess && !online){
			Vector3D lastLocation = target.getLastLocation();
			sender.sendMessage(M.arrow("Last location: " + M.WHITE + "(" + lastLocation.x + "," + lastLocation.y + "," + lastLocation.z + ") [" + target.getLastLocationWorldName() + "]"));
		}
		if(sensitiveViewAccess){
			sender.sendMessage(M.arrow("Latest address: " + M.WHITE + target.getLatestAddress()));
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
}
