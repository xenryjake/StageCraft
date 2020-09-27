package com.xenry.stagecraft.commands;
import com.xenry.stagecraft.profile.Profile;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public interface Access {
	
	boolean has(@NotNull Profile profile);
	
	boolean has(@NotNull CommandSender sender);
	
}
