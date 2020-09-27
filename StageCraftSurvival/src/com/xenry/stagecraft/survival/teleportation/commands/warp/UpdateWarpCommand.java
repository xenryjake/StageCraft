package com.xenry.stagecraft.survival.teleportation.commands.warp;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.survival.teleportation.Warp;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class UpdateWarpCommand extends Command<Survival,TeleportationManager> {
	
	public UpdateWarpCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "updatewarp", "warpupdate", "changewarp", "warpchange");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <name>"));
			return;
		}
		if(SetWarpCommand.ILLEGAL_NAMES.contains(args[0].toLowerCase())){
			profile.sendMessage(M.error("You can't use " + M.elm + args[0] + M.err + " as a warp name."));
			return;
		}
		Warp warp = manager.getWarpHandler().getWarp(args[0]);
		if(warp == null){
			profile.sendMessage(M.error("That warp does not exist: " + args[0]));
			return;
		}
		if(warp.setLocation(profile.getPlayer().getLocation())){
			profile.sendMessage(M.error("Invalid world."));
			return;
		}
		profile.sendMessage(M.msg + "You updated warp " + M.elm + warp.getName() + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? manager.getWarpHandler().getWarpNameList() : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? manager.getWarpHandler().getWarpNameList() : Collections.emptyList();
	}
	
}