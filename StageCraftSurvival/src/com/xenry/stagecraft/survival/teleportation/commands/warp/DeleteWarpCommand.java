package com.xenry.stagecraft.survival.teleportation.commands.warp;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.survival.teleportation.Warp;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DeleteWarpCommand extends Command<Survival,TeleportationManager> {
	
	public DeleteWarpCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "deletewarp", "removewarp", "delwarp", "warpdelete", "warpremove", "warpdel");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <name>"));
			return;
		}
		if(SetWarpCommand.ILLEGAL_NAMES.contains(args[0].toLowerCase())){
			sender.sendMessage(M.error("You can't use " + M.elm + args[0] + M.err + " as a warp name."));
			return;
		}
		Warp warp = manager.getWarpHandler().getWarp(args[0]);
		if(warp == null){
			sender.sendMessage(M.error("That warp does not exist: " + args[0]));
			return;
		}
		manager.getWarpHandler().deleteWarp(warp);
		sender.sendMessage(M.msg + "Deleted warp " + M.elm + warp.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getWarpHandler().getWarpNameList() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getWarpHandler().getWarpNameList() : Collections.emptyList();
	}
	
}
