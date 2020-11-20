package com.xenry.stagecraft.creative.gameplay.commands.armorstand;
import com.xenry.stagecraft.commands.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.event.FakeEntityDamageByEntityEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandCenterCommand extends PlayerCommand<Creative,GameplayManager> {
	
	public ArmorStandCenterCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "center");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		Entity entity = player.getTargetEntity(5);
		if(!(entity instanceof ArmorStand)){
			player.sendMessage(M.error("You aren't looking at an armor stand."));
			return;
		}
		ArmorStand as = (ArmorStand)entity;
		if(as.isMarker() || (as.isInvisible() && !ArmorStandCommand.INTERACT_INVISIBLE.has(profile))){
			player.sendMessage(M.error("You can't interact with that armor stand."));
			return;
		}
		
		FakeEntityDamageByEntityEvent fedbee = new FakeEntityDamageByEntityEvent(player, as,
				EntityDamageEvent.DamageCause.CUSTOM, 1);
		manager.plugin.getServer().getPluginManager().callEvent(fedbee);
		if(fedbee.isCancelled()){
			return;
		}
		
		as.teleport(LocationUtil.center(as.getLocation()));
		
		player.sendMessage(M.msg + "This armor stand has been centered. ");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
