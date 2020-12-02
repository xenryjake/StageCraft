package com.xenry.stagecraft.command;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.StageCraftPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class PlayerCommand<P extends StageCraftPlugin,T extends Manager<P>> extends Command<P,T> {
	
	protected PlayerCommand(T manager, Access access, String... labels) {
		super(manager, access, labels);
	}
	
	@Override
	protected final void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected final @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
