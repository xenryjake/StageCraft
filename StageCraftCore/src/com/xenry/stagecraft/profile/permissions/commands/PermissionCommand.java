package com.xenry.stagecraft.profile.permissions.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PermissionCommand extends Command<Core,ProfileManager> {
	
	public PermissionCommand(ProfileManager manager){
		super(manager, Rank.ADMIN, "permission", "permissions", "perm", "perms");
		setCanBeDisabled(true);
		addSubCommand(new PermissionSetCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "§lPermission Commands:");
		sender.sendMessage(M.help("perm set list", "List permission sets"));
		sender.sendMessage(M.help("perm set view <name>", "View a permission set"));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Collections.singletonList("set"), args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? filter(Collections.singletonList("set"), args[0]) : Collections.emptyList();
	}
	
}
