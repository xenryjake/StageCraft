package com.xenry.stagecraft.creative.heads.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.HeadsManager;
import com.xenry.stagecraft.creative.heads.ui.HeadsTagListMenu;
import com.xenry.stagecraft.creative.heads.ui.HeadsTagMenu;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
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
public class HeadsTagCommand extends PlayerCommand<Creative,HeadsManager> {
	
	public HeadsTagCommand(HeadsManager manager){
		super(manager, Rank.MEMBER, "tag", "tags");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			new HeadsTagListMenu(manager, profile.getUUID()).open(profile.getPlayer());
			return;
		}
		String input = Joiner.on(' ').join(args);
		String tag = manager.getTag(input);
		if(tag == null){
			profile.sendMessage(M.error("Tag does not exist: " + input));
			return;
		}
		new HeadsTagMenu(manager, tag, profile.getUUID()).open(profile.getPlayer());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(manager.getAllTags(), args[0]) : Collections.emptyList();
	}
	
}
