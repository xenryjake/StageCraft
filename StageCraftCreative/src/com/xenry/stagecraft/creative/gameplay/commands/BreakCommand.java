package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BreakCommand extends Command<Creative,GameplayManager> {
	
	public BreakCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "break");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
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
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}