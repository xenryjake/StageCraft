package com.xenry.stagecraft.survival.gameplay.sign;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SignCommand extends PlayerCommand<Survival,GameplayManager> {
	
	public SignCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "sign");
		addSubCommand(new SignClearCommand(manager));
		addSubCommand(new SignEditCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.sendMessage(M.msg + M.BOLD + "Sign Commands:");
		profile.sendMessage(M.help(label + " edit <line> <text>", "Edit the text on a sign."));
		profile.sendMessage(M.help(label + " clear [line]", "Clear text from a sign."));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return filter(Arrays.asList("edit", "clear"), args[0]);
	}
	
}
