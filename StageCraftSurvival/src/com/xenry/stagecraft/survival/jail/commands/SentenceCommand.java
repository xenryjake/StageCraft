package com.xenry.stagecraft.survival.jail.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.JailManager;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class JailViewCommand extends Command<Survival,JailManager> {
	
	public JailViewCommand(JailManager manager) {
		super(manager, Rank.MOD, "jailview", "viewjail", "lookupjail", "jaillookup");
	}
	
	
	
}
