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
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DeleteWarpAliasCommand extends Command<Survival,TeleportationManager> {
	
	public DeleteWarpAliasCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "deletewarpalias", "removewarpalias", "delwarpalias");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <warp> <alias>"));
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
		String alias = args[1].toLowerCase();
		if(!warp.getAliases().contains(alias)){
			sender.sendMessage(M.error(M.elm + alias + M.err + " is not an alias for warp " + M.elm + warp.getName() + M.err + "."));
			return;
		}
		warp.removeAlias(alias);
		sender.sendMessage(M.msg + "You removed " + M.elm + alias + M.msg + " as an alias to warp " + M.elm + warp.getName() + M.msg + ".");
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
