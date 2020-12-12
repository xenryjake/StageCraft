package com.xenry.stagecraft.survival.gameplay.damageindicator;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/8/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DamageIndicatorDeSpawnCommand extends Command<Survival,GameplayManager> {

	public DamageIndicatorDeSpawnCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "despawn");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/di " + label + " <world|all> [world2] [world3]..."));
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
				if(!manager.getDamageIndicatorHandler().isDamageIndicator(entity)){
					continue;
				}
				entity.remove();
				i++;
			}
		}
		sender.sendMessage(M.msg + "Despawned " + M.elm + i + M.msg + " damage indicators in " + M.elm
				+ worlds.size() + M.msg + (worlds.size() == 1 ? " world." : " worlds."));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			List<String> worlds = LocationUtil.getAllWorldNames();
			worlds.add("all");
			return filter(worlds, args[0]);
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		if(args.length == 1){
			List<String> worlds = LocationUtil.getAllWorldNames();
			worlds.add("all");
			return filter(worlds, args[0]);
		}else{
			return Collections.emptyList();
		}
	}
	
}
