package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameStatus;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HNSEndCommand extends Command<Survival,HideNSeekManager> {
	
	private final ArrayList<String> confirms;
	
	public HNSEndCommand(HideNSeekManager manager){
		super(manager, Rank.ADMIN, "end", "stop");
		confirms = new ArrayList<>();
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(manager.getStatus() == GameStatus.NONE) {
			sender.sendMessage(err + "There is no game.");
			return;
		}
		
		if(!confirms.contains(sender.getName())){
			sender.sendMessage(err + "Are you sure you want to " + label + " the game? Type " + hlt + "/ha " + label + err + " again to " + label + ".");
			confirms.add(sender.getName());
			Bukkit.getScheduler().runTaskLater(manager.plugin, () -> confirms.remove(sender.getName()), 200L);
			return;
		}
		
		sender.sendMessage(msg + "Ending the game...");
		manager.endGame();
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
}
