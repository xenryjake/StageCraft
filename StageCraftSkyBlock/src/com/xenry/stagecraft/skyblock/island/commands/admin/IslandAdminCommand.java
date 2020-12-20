package com.xenry.stagecraft.skyblock.island.commands.admin;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class IslandAdminCommand extends Command<SkyBlock,IslandManager> {
	
	public IslandAdminCommand(IslandManager manager){
		super(manager, Rank.ADMIN, "islandadmin", "islandsadmin", "isa");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length > 0 && args[0].equalsIgnoreCase("testpaste")){
			player.sendMessage(M.msg + "pasting...");
			manager.getSchematicHandler().pasteMainIsland(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
		}else if(args.length > 0 && args[0].equalsIgnoreCase("testload")){
			player.sendMessage(M.msg + "loading...");
			manager.getSchematicHandler().loadMainIslandSchematic();
		}else if(args.length > 0 && args[0].equalsIgnoreCase("clear")){
			if(args.length < 2){
				player.sendMessage(M.error("specify size"));
				return;
			}
			int x1;
			int z1;
			int x2;
			int z2;
			if(args[1].equalsIgnoreCase("chunk")){
				x1 = player.getChunk().getX() * 16;
				z1 = player.getChunk().getZ() * 16;
				x2 = player.getChunk().getX() * 16 + 15;
				z2 = player.getChunk().getZ() * 16 + 15;
			}else if(args[1].equalsIgnoreCase("col") || args[1].equalsIgnoreCase("column")){
				x1 = player.getLocation().getBlockX();
				z1 = player.getLocation().getBlockZ();
				x2 = x1;
				z2 = z1;
			}else if(args[1].equalsIgnoreCase("island")){
				int islandX = Island.actualToIsland(player.getLocation().getBlockX());
				int islandZ = Island.actualToIsland(player.getLocation().getBlockZ());
				x1 = Island.islandToActual1(islandX);
				z1 = Island.islandToActual1(islandZ);
				x2 = Island.islandToActual2(islandX);
				z2 = Island.islandToActual2(islandZ);
			}else if(args[1].equalsIgnoreCase("2x2")){
				x1 = player.getChunk().getX() * 16;
				z1 = player.getChunk().getZ() * 16;
				x2 = player.getChunk().getX() * 16 + 31;
				z2 = player.getChunk().getZ() * 16 + 31;
			}else if(args[1].equalsIgnoreCase("4x4")){
				x1 = player.getChunk().getX() * 16;
				z1 = player.getChunk().getZ() * 16;
				x2 = player.getChunk().getX() * 16 + 63;
				z2 = player.getChunk().getZ() * 16 + 63;
			}else{
				player.sendMessage(M.error("specify region type (chunk, col, island)"));
				return;
			}
			player.sendMessage(M.msg + "clearing (" + x1 + "," + z1 + ") -> (" + x2 + "," + z2 + ")");
			manager.getSchematicHandler().clearRegion(manager.getWorld(), x1, z1, x2, z2);
		}else{
			player.sendMessage(M.error("unknown subcmd"));
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
	
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
