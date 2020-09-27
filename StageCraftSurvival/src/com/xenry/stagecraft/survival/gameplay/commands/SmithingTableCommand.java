package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.BlockSmithingTable;
import net.minecraft.server.v1_16_R2.Blocks;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SmithingTableCommand extends Command<Survival,GameplayManager> {
	
	public SmithingTableCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "smith", "smithingtable", "smithing");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		Location location = player.getLocation();
		
		((CraftPlayer)player).getHandle().openContainer(((BlockSmithingTable)Blocks.SMITHING_TABLE).getInventory(null,
				((CraftPlayer)player).getHandle().world, new BlockPosition(location.getBlockX(), location.getBlockY(),
						location.getBlockZ())));
		((CraftPlayer)player).getHandle().activeContainer.checkReachable = false;
		((CraftPlayer)player).getHandle().activeContainer.getBukkitView();
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
