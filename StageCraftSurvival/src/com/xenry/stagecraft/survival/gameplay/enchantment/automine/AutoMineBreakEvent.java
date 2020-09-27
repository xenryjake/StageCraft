package com.xenry.stagecraft.survival.gameplay.enchantment.automine;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class AutoMineBreakEvent extends BlockBreakEvent {
	
	public AutoMineBreakEvent(Block block, Player player){
		super(block, player);
	}
	
}
