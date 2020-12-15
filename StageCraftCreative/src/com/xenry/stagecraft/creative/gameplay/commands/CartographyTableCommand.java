package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class CartographyTableCommand extends PlayerCommand<Creative,GameplayManager> {
	
	public CartographyTableCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "cartographytable", "carttable", "cartography", "cart");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.getPlayer().openCartographyTable(null, true);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
