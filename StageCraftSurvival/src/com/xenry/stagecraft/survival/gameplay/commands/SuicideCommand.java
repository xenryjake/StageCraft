package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class SuicideCommand extends Command<Survival,GameplayManager> {
	
	public SuicideCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "suicide", "kms", "die", "kermit");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		EntityDamageEvent event = new EntityDamageEvent(profile.getPlayer(), EntityDamageEvent.DamageCause.SUICIDE,
				Short.MAX_VALUE);
		manager.plugin.getServer().getPluginManager().callEvent(event);
		if(profile.getPlayer().getHealth() > 0){
			profile.getPlayer().setHealth(0);
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
