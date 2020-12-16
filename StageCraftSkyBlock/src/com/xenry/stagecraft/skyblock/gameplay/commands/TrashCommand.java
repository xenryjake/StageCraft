package com.xenry.stagecraft.skyblock.gameplay.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.gameplay.GameplayManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TrashCommand extends PlayerCommand<SkyBlock,GameplayManager> {
	
	public TrashCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "trash", "trashcan", "disposal", "d", "shea");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.getPlayer().openInventory(manager.plugin.getServer().createInventory(profile.getPlayer(), 36,
				"Trash Can"));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
