package com.xenry.stagecraft.hologram.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.hologram.HologramManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/8/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HologramDeSpawnCommand extends Command<Core,HologramManager> {
	
	public HologramDeSpawnCommand(HologramManager manager){
		super(manager, Rank.ADMIN, "despawn");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/hologram " + label + " <world|all> [world2] [world3]..."));
			return;
		}
		List<World> worlds = new ArrayList<>();
		if(args[0].equalsIgnoreCase("all")){
			worlds = Bukkit.getWorlds();
		}else{
			for(String arg : args){
				World world = Bukkit.getWorld(arg);
				if(world == null){
					sender.sendMessage(M.error("World not found: " + arg));
					return;
				}
				worlds.add(world);
			}
		}
		if(worlds.isEmpty()){
			sender.sendMessage(M.error("Please specify at least one world."));
			return;
		}
		int i = 0;
		for(World world : worlds){
			for(Entity entity : world.getEntities()){
				if(!manager.isHologram(entity)){
					continue;
				}
				entity.remove();
				i++;
			}
		}
		sender.sendMessage(M.msg + "Despawned " + M.elm + i + M.msg + " holograms in " + M.elm + worlds.size() + M.msg
				+ (worlds.size() == 1 ? " world." : " worlds."));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		List<String> worlds = LocationUtil.getAllWorldNames();
		worlds.add("all");
		return worlds;
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		List<String> worlds = LocationUtil.getAllWorldNames();
		worlds.add("all");
		return worlds;
	}
	
}
