package com.xenry.stagecraft.fun.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.fun.Fun;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.fun.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
public final class MoreCommand extends PlayerCommand<Fun,GameplayManager> {
	
	public final static Access UNSAFE_ACCESS = Rank.ADMIN;
	
	public MoreCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "more");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		boolean unsafe = UNSAFE_ACCESS.has(profile) && args.length >= 1 && args[0].toLowerCase().startsWith("u");
		Player player = profile.getPlayer();
		ItemStack is = player.getInventory().getItemInMainHand();
		int max = unsafe ? 64 : is.getMaxStackSize();
		if(is.getType() != Material.AIR && is.getAmount() < max){
			is.setAmount(max);
		}else{
			player.sendMessage(M.error("Cannot give any more of item " + is.getType().name()));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 && UNSAFE_ACCESS.has(profile) ? filter(Collections.singletonList("unsafe"), args[0]) : Collections.emptyList();
	}
	
}
