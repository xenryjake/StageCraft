package com.xenry.stagecraft.creative.gameplay.armorstand;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.event.FakeEntityDamageByEntityEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandHandler extends Handler<Creative,GameplayManager> {
	
	public static final Access INTERACT_INVISIBLE = Rank.ADMIN;
	
	private final HashMap<String,ArmorStandClipboard> clipboards;
	
	public ArmorStandHandler(GameplayManager manager){
		super(manager);
		clipboards = new HashMap<>();
	}
	
	public boolean checkPerms(Player player, ArmorStand as){
		FakeEntityDamageByEntityEvent fedbee = new FakeEntityDamageByEntityEvent(player, as,
				EntityDamageEvent.DamageCause.CUSTOM, 1);
		manager.plugin.getServer().getPluginManager().callEvent(fedbee);
		return !fedbee.isCancelled();
	}
	
	public ArmorStand getStand(Profile profile){
		Player player = profile.getPlayer();
		Entity entity = player.getTargetEntity(5);
		if(!(entity instanceof ArmorStand)){
			player.sendMessage(M.error("You aren't looking at an armor stand."));
			return null;
		}
		ArmorStand as = (ArmorStand)entity;
		if(as.isMarker() || (as.isInvisible() && !INTERACT_INVISIBLE.has(profile))){
			player.sendMessage(M.error("You can't interact with that armor stand."));
			return null;
		}
		return as;
	}
	
	@Nullable
	public ArmorStandClipboard getClipboard(String uuid){
		return clipboards.getOrDefault(uuid, null);
	}
	
	@Nullable
	public ArmorStandClipboard getClipboard(Player player){
		return getClipboard(player.getUniqueId().toString());
	}
	
	@Nullable
	public ArmorStandClipboard getClipboard(Profile profile){
		return getClipboard(profile.getUUID());
	}
	
	public void setClipboard(String uuid, ArmorStandClipboard clipboard){
		clipboards.put(uuid, clipboard);
	}
	
	public void setClipboard(Player player, ArmorStandClipboard clipboard){
		setClipboard(player.getUniqueId().toString(), clipboard);
	}
	
	public void setClipboard(Profile profile, ArmorStandClipboard clipboard){
		setClipboard(profile.getUUID(), clipboard);
	}
	
}
