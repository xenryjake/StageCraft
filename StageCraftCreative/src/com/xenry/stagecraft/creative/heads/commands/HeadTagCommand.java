package com.xenry.stagecraft.creative.heads.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.HeadsManager;
import com.xenry.stagecraft.creative.heads.ui.HeadTagMenu;
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
public class HeadTagCommand extends PlayerCommand<Creative,HeadsManager> {
	
	public HeadTagCommand(HeadsManager manager){
		super(manager, Rank.MEMBER, "tag", "tags");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/head tag <tag>"));
			return;
		}
		String input = Joiner.on(' ').join(args);
		String tag = manager.getTag(input);
		if(tag == null){
			profile.sendMessage(M.error("Tag does not exist: " + input));
			return;
		}
		new HeadTagMenu(manager, tag, profile.getUUID());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getAllTags() : Collections.emptyList();
	}
	
}
