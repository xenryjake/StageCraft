package com.xenry.stagecraft.skyblock.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.teleportation.Teleportation;
import com.xenry.stagecraft.skyblock.teleportation.TeleportationManager;
import com.xenry.stagecraft.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class JumpCommand extends PlayerCommand<SkyBlock,TeleportationManager> {
	
	public JumpCommand(TeleportationManager manager){
		super(manager, Rank.HEAD_MOD, "jump", "jumpto", "j");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		Block block = player.getTargetBlock(LocationUtil.TRANSPARENT_MATERIALS, 256);
		Location location = block.getLocation();
		location.setY(location.getY() + 1);
		location.setYaw(player.getLocation().getYaw());
		location.setPitch(player.getLocation().getPitch());
		manager.createAndExecuteTeleportation(player, player, player.getLocation(), location, Teleportation.Type.ADMIN, true);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
