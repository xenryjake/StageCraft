package com.xenry.stagecraft.survival.teleportation.commands.warp;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.survival.teleportation.Warp;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.NumberUtil;
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
public final class WarpInfoCommand extends Command<Survival,TeleportationManager> {
	
	public WarpInfoCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "warpinfo", "winfo", "warpi", "wi");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <warp>"));
			return;
		}
		Warp warp = manager.getWarpHandler().getWarp(args[0]);
		if(warp == null){
			sender.sendMessage(M.error("That warp does not exist: " + args[0]));
			return;
		}
		
		String aliasString = "§fnone";
		List<String> aliases = warp.getAliases();
		if(!aliases.isEmpty()){
			StringBuilder sb = new StringBuilder();
			for(String alias : warp.getAliases()){
				sb.append(M.WHITE).append(alias).append(M.DGRAY).append(", ");
			}
			aliasString = sb.toString().trim();
			if(aliasString.endsWith(",")){
				aliasString = aliasString.substring(0, aliasString.length() - 1);
			}
		}
		
		sender.sendMessage(M.msg + "Info about warp " + M.elm + warp.getName() + M.msg + ":");
		sender.sendMessage(M.arrow("Aliases: " + aliasString));
		sender.sendMessage(M.arrow("Location: " + M.WHITE + "(" + NumberUtil.displayAsHundredths(warp.getX())
				+ "," + NumberUtil.displayAsHundredths(warp.getY()))
				+ "," + NumberUtil.displayAsHundredths(warp.getZ())
				+ ") [" + warp.getWorldName() + "]");
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
