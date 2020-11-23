package com.xenry.stagecraft.util.event;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class FakeEntityDamageByEntityEvent extends EntityDamageByEntityEvent implements FakeEvent {
	
	public FakeEntityDamageByEntityEvent(@NotNull Entity damager, @NotNull Entity damagee, @NotNull DamageCause cause,
										 double damage) {
		super(damager, damagee, cause, damage);
	}
	
}
