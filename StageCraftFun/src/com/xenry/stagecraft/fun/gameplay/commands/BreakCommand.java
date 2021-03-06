package com.xenry.stagecraft.fun.gameplay.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.fun.Fun;
import com.xenry.stagecraft.fun.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
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
public final class BreakCommand extends PlayerCommand<Fun,GameplayManager> {
	
	public BreakCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "break");
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		FluidCollisionMode mode = FluidCollisionMode.NEVER;
		if(args.length > 0){
			switch(args[0].toLowerCase()){
				case "false":
				case "never":
				case "off":
					break;
				case "source":
				case "src":
					mode = FluidCollisionMode.SOURCE_ONLY;
					break;
				case "true":
				case "on":
				case "always":
					mode = FluidCollisionMode.ALWAYS;
					break;
				default:
					sender.sendMessage(M.usage("/" + label + " [fluid-collision: never|source|always]"));
					return;
			}
		}
		
		Player player = sender.getPlayer();
		Block block = player.getTargetBlockExact(20, mode);
		if(block == null || block.getType() == Material.AIR){
			sender.sendMessage(M.error("Couldn't find a block to break."));
			return;
		}
		BlockBreakEvent bbe = new BlockBreakEvent(block, player);
		manager.plugin.getServer().getPluginManager().callEvent(bbe);
		if(!bbe.isCancelled()){
			block.setType(Material.AIR);
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
