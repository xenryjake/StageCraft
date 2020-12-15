package com.xenry.stagecraft.creative.teleportation.commands.warp;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.creative.teleportation.Warp;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SetWarpAliasCommand extends Command<Creative,TeleportationManager> {
	
	public SetWarpAliasCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "setwarpalias", "addwarpalias");
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
		Warp warp = manager.getWarpHandler().getWarp(args[0]);
		if(warp == null){
			sender.sendMessage(M.error("That warp does not exist: " + args[0]));
			return;
		}
		String alias = args[1];
		if(!alias.matches("^[A-Za-z0-9_]+$")){
			sender.sendMessage(M.error("Warp aliases can only contain alphanumeric characters and underscores."));
			return;
		}
		if(alias.replaceAll("[0-9_]", "").isEmpty()){
			sender.sendMessage(M.error("Warp aliases must contain at least one letter."));
			return;
		}
		if(alias.length() > 32 || alias.length() < 3){
			sender.sendMessage(M.error("Warp aliases must be between 3 and 32 characters long."));
			return;
		}
		if(SetWarpCommand.ILLEGAL_NAMES.contains(alias.toLowerCase())){
			sender.sendMessage(M.error("You can't use " + M.elm + alias + M.err + " as a warp alias."));
			return;
		}
		if(manager.getWarpHandler().getWarp(alias) != null){
			sender.sendMessage(M.error("A warp with that name already exists."));
			return;
		}
		warp.addAlias(alias);
		sender.sendMessage(M.msg + "You added " + M.elm + alias + M.msg + " as an alias to warp " + M.elm + warp.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getWarpHandler().getWarpNameList(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getWarpHandler().getWarpNameList(args[0]) : Collections.emptyList();
	}
	
}
