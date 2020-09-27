package com.xenry.stagecraft.survival.teleportation.commands.warp;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.survival.teleportation.Warp;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

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
public final class SetWarpCommand extends Command<Survival,TeleportationManager> {
	
	public static final int MAX_WARPS = 1000;
	
	public static final List<String> ILLEGAL_NAMES = Arrays.asList("list","l","spawn");
	
	public SetWarpCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "setwarp", "addwarp", "warpset", "warpadd");
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
		String name = args[0];
		if(!name.matches("^[A-Za-z0-9_]+$")){
			profile.sendMessage(M.error("Warp names can only contain alphanumeric characters and underscores."));
			return;
		}
		if(name.replaceAll("[0-9_]", "").isEmpty()){
			profile.sendMessage(M.error("Warp names must contain at least one letter."));
			return;
		}
		if(name.length() > 32 || name.length() < 3){
			profile.sendMessage(M.error("Warp names must be between 3 and 32 characters long."));
			return;
		}
		if(ILLEGAL_NAMES.contains(name.toLowerCase())){
			profile.sendMessage(M.error("You can't use " + M.elm + name + M.err + " as a warp name."));
			return;
		}
		if(manager.getWarpHandler().getWarp(name) != null){
			profile.sendMessage(M.error("A warp with that name already exists."));
			return;
		}
		if(manager.getWarpHandler().getWarps().size() >= MAX_WARPS){
			profile.sendMessage(M.error("The server has reached its maximum number of warps."));
			return;
		}
		manager.getWarpHandler().addWarp(new Warp(name, profile.getPlayer().getLocation()));
		profile.sendMessage(M.msg + "You created a new warp: " + M.elm + name + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
