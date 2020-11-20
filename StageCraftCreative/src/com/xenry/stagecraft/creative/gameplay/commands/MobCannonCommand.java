package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class MobCannonCommand extends Command<Creative,GameplayManager> {
	
	public MobCannonCommand(GameplayManager manager) {
		super(manager, Rank.PREMIUM, "beezooka", "beecannon", "kittycannon");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Class<? extends Entity> clazz;
		switch(label){
			case "beezooka":
			case "beecannon":
				clazz = Bee.class;
				break;
			case "kittycannon":
				clazz = Cat.class;
				break;
			default:
				profile.sendMessage(M.error("Invalid mob."));
				return;
		}
		Player player = profile.getPlayer();
		Entity mob = player.getWorld().spawn(player.getEyeLocation(), clazz);
		if(mob instanceof Cat){
			int i = manager.getCore().getRandom().nextInt(Cat.Type.values().length);
			((Cat)mob).setCatType(Cat.Type.values()[i]);
			((Cat)mob).setTamed(true);
			((Cat)mob).setBaby();
		}
		mob.setVelocity(player.getEyeLocation().getDirection().multiply(2));
		manager.plugin.getServer().getScheduler().scheduleSyncDelayedTask(manager.plugin, () -> {
			final Location location = mob.getLocation();
			mob.remove();
			location.createExplosion(0);
		}, 20);
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
