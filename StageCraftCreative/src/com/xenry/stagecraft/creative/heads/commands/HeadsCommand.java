package com.xenry.stagecraft.creative.heads.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.Head;
import com.xenry.stagecraft.creative.heads.HeadsManager;
import com.xenry.stagecraft.creative.heads.ui.HeadsCategoryMenu;
import com.xenry.stagecraft.creative.heads.ui.HeadsFavoriteMenu;
import com.xenry.stagecraft.creative.heads.ui.HeadsCategoryListMenu;
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
public final class HeadsCommand extends PlayerCommand<Creative,HeadsManager> {
	
	public HeadsCommand(HeadsManager manager) {
		super(manager, Rank.MEMBER, "head", "skull", "heads", "skulls");
		addSubCommand(new HeadsTagCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			new HeadsCategoryListMenu(manager, profile.getUUID()).open(profile.getPlayer());
			return;
		}
		Head.Category category = Head.Category.fromID(args[0]);
		if(args[0].equalsIgnoreCase("favorite") || args[0].equalsIgnoreCase("favorites")){
			new HeadsFavoriteMenu(manager, profile.getUUID()).open(profile.getPlayer());
			return;
		}
		if(category == null){
			profile.sendMessage(M.error("Invalid category: " + args[0]));
			return;
		}
		new HeadsCategoryMenu(manager, category, profile.getUUID()).open(profile.getPlayer());
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
