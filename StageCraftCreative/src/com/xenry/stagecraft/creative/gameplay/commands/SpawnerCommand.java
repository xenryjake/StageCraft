package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.entity.Entities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SpawnerCommand extends Command<Creative,GameplayManager> {
	
	public SpawnerCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "spawner", "mobspawner");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <type> [delay]"));
			return;
		}
		Player player = sender.getPlayer();
		Block targetBlock = player.getTargetBlock(LocationUtil.TRANSPARENT_MATERIALS, 300);
		if(targetBlock.getType() != Material.SPAWNER){
			sender.sendMessage(M.error("You aren't looking at a mob spawner."));
			return;
		}
		Integer delay = null;
		if(args.length > 1){
			try{
				delay = Integer.parseInt(args[1]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Invalid integer: " + args[1]));
				return;
			}
		}
		
		
		Entities mob = Entities.getMob(args[0]);
		if(mob == null){
			sender.sendMessage(M.error("Invalid mob: " + args[0]));
			return;
		}
		
		BlockState blockState = targetBlock.getState();
		if(!(blockState instanceof CreatureSpawner)){
			sender.sendMessage(M.error("Cannot change mob type?"));
			return;
		}
		
		CreatureSpawner spawner = (CreatureSpawner) blockState;
		spawner.setSpawnedType(mob.type);
		if(delay != null){
			spawner.setDelay(delay);
		}
		spawner.update();
		
		sender.sendMessage(M.msg + "You set the spawner type to " + M.elm + mob.name + M.msg + " with delay "
				+ M.elm + spawner.getDelay() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? Entities.getAllMobIdentifiers() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
