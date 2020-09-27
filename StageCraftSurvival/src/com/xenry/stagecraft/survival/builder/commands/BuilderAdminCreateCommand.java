package com.xenry.stagecraft.survival.builder.commands;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuildArea;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderAdminCreateCommand extends Command<Survival,BuilderManager> {
	
	public BuilderAdminCreateCommand(BuilderManager manager){
		super(manager, Rank.ADMIN, "create");
	}
	
	/*
	[0] = name
	[1] = AX
	[2] = AZ
	[3] = BX
	[4] = BZ
	[5] = world (optional)
	 */
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 5){
			profile.sendMessage(M.usage("/builderadmin create <name> <x1> <z1> <x2> <z2> [world]"));
			return;
		}
		String name = args[0];
		if(name.replaceAll("[a-zA-Z0-9_]", "").equals(name)){
			profile.sendMessage(M.error("Build Area names can only contain alphanumeric characters and underscores."));
			return;
		}
		int ax, az, bx, bz;
		try{
			ax = Integer.parseInt(args[1]);
			az = Integer.parseInt(args[2]);
			bx = Integer.parseInt(args[3]);
			bz = Integer.parseInt(args[4]);
		}catch(Exception ex){
			profile.sendMessage(M.error("Invalid integer."));
			return;
		}
		World world;
		if(args.length > 5){
			world = Bukkit.getWorld(args[5]);
		}else{
			world = profile.getPlayer().getLocation().getWorld();
		}
		if(world == null){
			profile.sendMessage(M.error("That world does not exist."));
			return;
		}
		BuildArea area = new BuildArea(name, world.getName(), ax, az, bx, bz);
		if(manager.addBuildArea(area)){
			profile.sendMessage(M.msg + "Successfully added build area " + M.elm + name + M.msg + ".");
		}else{
			profile.sendMessage(M.error("An area with that name already exists."));
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 6){
			sender.sendMessage(M.usage("/builderadmin create <name> <x1> <z1> <x2> <z2> <world>"));
			return;
		}
		String name = args[0];
		int ax, az, bx, bz;
		try{
			ax = Integer.parseInt(args[1]);
			az = Integer.parseInt(args[2]);
			bx = Integer.parseInt(args[3]);
			bz = Integer.parseInt(args[4]);
		}catch(Exception ex){
			sender.sendMessage(M.error("Invalid integer."));
			return;
		}
		World world = Bukkit.getWorld(args[5]);
		if(world == null){
			sender.sendMessage(M.error("That world does not exist."));
			return;
		}
		BuildArea area = new BuildArea(name, world.getName(), ax, az, bx, bz);
		if(manager.addBuildArea(area)){
			sender.sendMessage(M.msg + "Successfully added build area " + M.elm + name + M.msg + ".");
		}else{
			sender.sendMessage(M.error("An area with that name already exists."));
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		switch(args.length){
			case 2:
			case 4:
				return new ArrayList<>(Collections.singleton(String.valueOf(player.getLocation().getBlockX())));
			case 3:
			case 5:
				return new ArrayList<>(Collections.singleton(String.valueOf(player.getLocation().getBlockZ())));
			case 6:
				return LocationUtil.getAllWorldNames();
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		if(args.length == 6){
			return LocationUtil.getAllWorldNames();
		}
		return Collections.emptyList();
	}
	
}
