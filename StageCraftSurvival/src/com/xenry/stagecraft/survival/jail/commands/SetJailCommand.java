package com.xenry.stagecraft.survival.jail.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.Jail;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.util.M;
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
public final class SetJailCommand extends PlayerCommand<Survival,JailManager> {
	
	public static final int MAX_JAILS = 100;
	
	public SetJailCommand(JailManager manager){
		super(manager, Rank.MOD, "setjail", "addjail", "jailset", "jailadd");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <name>"));
			return;
		}
		String name = args[0];
		if(!name.matches("^[A-Za-z0-9_]+$")){
			profile.sendMessage(M.error("Jail names can only contain alphanumeric characters and underscores."));
			return;
		}
		if(name.length() > 32 || name.length() < 1){
			profile.sendMessage(M.error("Jail names must be between 1 and 32 characters long."));
			return;
		}
		if(manager.getJailHandler().getJail(name) != null){
			profile.sendMessage(M.error("A jail with that name already exists."));
			return;
		}
		if(manager.getJailHandler().getJails().size() >= MAX_JAILS){
			profile.sendMessage(M.error("The server has reached its maximum number of jails."));
			return;
		}
		manager.getJailHandler().addJail(new Jail(name, profile.getPlayer().getLocation()));
		profile.sendMessage(M.msg + "You created a new jail: " + M.elm + name + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
