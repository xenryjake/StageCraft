package com.xenry.stagecraft.creative.heads.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.Head;
import com.xenry.stagecraft.creative.heads.HeadsManager;
import com.xenry.stagecraft.creative.heads.ui.HeadCategoryMenu;
import com.xenry.stagecraft.creative.heads.ui.HeadsHomeMenu;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HeadCommand extends PlayerCommand<Creative,HeadsManager> {
	
	public HeadCommand(HeadsManager manager) {
		super(manager, Rank.MEMBER, "head", "skull", "heads", "skulls");
		addSubCommand(new HeadTagCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			new HeadsHomeMenu(manager, profile.getUUID(), label).open(profile.getPlayer());
			return;
		}
		Head.Category category = Head.Category.fromID(args[0]);
		if(args[0].equalsIgnoreCase("favorite") || args[0].equalsIgnoreCase("favorites")){
			//todo open favorites menu
			profile.sendMessage(M.err + "Favorites view coming soon.");
			return;
		}
		if(category == null){
			profile.sendMessage(M.error("Invalid category: " + args[0]));
			return;
		}
		new HeadCategoryMenu(manager, category, profile.getUUID(), label).open(profile.getPlayer());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			List<String> strings = Head.Category.getAllIDs();
			strings.add("tag");
			strings.add("favorites");
			return filter(strings, args[0]);
		}else{
			return Collections.emptyList();
		}
	}
	
}
