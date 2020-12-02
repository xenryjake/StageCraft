package com.xenry.stagecraft.survival.gameplay.damageindicator;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/8/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DamageIndicatorCommand extends Command<Survival,GameplayManager> {
	
	public DamageIndicatorCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "damageindicator", "damageindicators", "di");
		addSubCommand(new DamageIndicatorDeSpawnCommand(manager));
		addSubCommand(new DamageIndicatorForceDeSpawnCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + M.BOLD + "Damage Indicator Commands:");
		sender.sendMessage(M.help(label + " despawn", "Despawn damage indicators from a world."));
		sender.sendMessage(M.help(label + " forcedespawn", "Forcefully despawn damage indicators from a world."));
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
