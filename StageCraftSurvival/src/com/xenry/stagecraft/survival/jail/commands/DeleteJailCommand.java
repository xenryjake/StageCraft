package com.xenry.stagecraft.survival.jail.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.Jail;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DeleteJailCommand extends Command<Survival,JailManager> {
	
	public DeleteJailCommand(JailManager manager){
		super(manager, Rank.MOD, "deletejail", "removejail", "deljail", "jaildelete", "jailremove", "jaildel");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <name>"));
			return;
		}
		Jail jail = manager.getJailHandler().getJail(args[0]);
		if(jail == null){
			sender.sendMessage(M.error("That jail does not exist."));
			return;
		}
		manager.getJailHandler().deleteJail(jail);
		sender.sendMessage(M.msg + "Deleted jail " + M.elm + jail.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getJailHandler().getJailNameList() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getJailHandler().getJailNameList() : Collections.emptyList();
	}
	
}
