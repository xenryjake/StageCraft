package com.xenry.stagecraft.survival.pet.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.pet.PetManager;
import com.xenry.stagecraft.survival.pet.entities.BeePetEntity;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TestSpawnCommand extends PlayerCommand<Survival,PetManager> {
	
	public TestSpawnCommand(PetManager manager){
		super(manager, Rank.ADMIN, "testspawnpet");
		setDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		manager.spawn(new BeePetEntity(profile.getPlayer().getLocation()), profile.getPlayer().getLocation());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
