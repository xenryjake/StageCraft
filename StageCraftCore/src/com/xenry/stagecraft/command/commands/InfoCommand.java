package com.xenry.stagecraft.command.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.command.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class InfoCommand extends Command<Core,CommandManager> {
	
	public InfoCommand(CommandManager manager){
		super(manager, Rank.MEMBER, "info", "information", "help");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage();
		sender.sendMessage(M.msg + "   Welcome to §9§lStage§a§lCraft§3!");
		sender.sendMessage(M.msg + "Visit our website at §emc.xenry.com");
		sender.sendMessage(M.msg + "For a list of commands, see §emc.xenry.com/info");
		sender.sendMessage(M.msg + "Before you play, please accept the rules: §emc.xenry.com/rules");
		sender.sendMessage();
		sender.sendMessage(M.msg + "We have a Survival Server and a Creative Server!");
		sender.sendMessage(M.msg + "Type " + M.elm + "/survival" + M.msg + " or " + M.elm + "/creative" + M.msg + " to change servers.");
		sender.sendMessage();
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
