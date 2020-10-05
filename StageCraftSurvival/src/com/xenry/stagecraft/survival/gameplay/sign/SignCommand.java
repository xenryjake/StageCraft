package com.xenry.stagecraft.survival.gameplay.sign;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SignCommand extends Command<Survival,GameplayManager> {
	
	public SignCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "sign");
		addSubCommand(new SignClearCommand(manager));
		addSubCommand(new SignEditCommand(manager));
		addSubCommand(new SignWhiteBlackCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.sendMessage(M.msg + M.BOLD + "Sign Commands:");
		profile.sendMessage(M.help(label + " edit <line> <text>", "Edit the text on a sign."));
		profile.sendMessage(M.help(label + " clear [line]", "Clear text from a sign."));
		profile.sendMessage(M.help(label + " <white|black>", "Change your default sign text color."));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Arrays.asList("edit", "clear", "white", "black");
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
