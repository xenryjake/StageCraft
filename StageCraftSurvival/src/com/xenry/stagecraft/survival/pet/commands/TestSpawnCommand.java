package com.xenry.stagecraft.survival.pet.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.pet.PetManager;
import com.xenry.stagecraft.survival.pet.entities.BeePetEntity;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.command.CommandSender;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TestSpawnCommand extends Command<Survival,PetManager> {
	
	public TestSpawnCommand(PetManager manager){
		super(manager, Rank.ADMIN, "testspawnpet");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		manager.spawn(new BeePetEntity(profile.getPlayer().getLocation()), profile.getPlayer().getLocation());
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
}
