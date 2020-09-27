package com.xenry.stagecraft.survival.builder.commands;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderAdminCommand extends Command<Survival,BuilderManager> {
	
	public BuilderAdminCommand(BuilderManager manager){
		super(manager, Rank.MOD, "builderadmin", "ba");
		addSubCommand(new BuilderAdminListCommand(manager));
		addSubCommand(new BuilderAdminRemoveCommand(manager));
		addSubCommand(new BuilderAdminCreateCommand(manager));
		addSubCommand(new BuilderAdminWhoCommand(manager));
		addSubCommand(new BuilderAdminNBICommand(manager));
		addSubCommand(new BuilderAdminEnableDisableCommand(manager));
		addSubCommand(new BuilderAdminDownloadCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.help(label + " list", "List areas"));
		sender.sendMessage(M.help(label + " remove", "Remove area"));
		sender.sendMessage(M.help(label + " create", "Create area"));
		sender.sendMessage(M.help(label + " who ", "List players in builder mode"));
		sender.sendMessage(M.help(label + " nbi", "Manage non-builder interaction"));
		sender.sendMessage(M.help(label + " enable", "Enable builder mode"));
		sender.sendMessage(M.help(label + " disable", "Disable builder mode"));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("list", "remove", "create", "who", "nbi", "enable", "disable") :
				Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("list", "remove", "create", "who", "nbi", "enable", "disable") :
				Collections.emptyList();
	}
	
}
