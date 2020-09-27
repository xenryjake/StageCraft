package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class HNSLeaveCommand extends Command<Survival,HideNSeekManager> {
	
	private final ArrayList<String> confirms;
	
	public HNSLeaveCommand(HideNSeekManager manager){
		super(manager, Rank.MEMBER, "leave", "quit");
		confirms = new ArrayList<>();
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(final Profile profile, String[] args, String label) {
		if(manager.getPlayerHandler().getPlayerMode(profile) == null){
			profile.sendMessage(err + "You aren't in a game.");
			return;
		}
		
		if(!confirms.contains(profile.getPlayer().getName())){
			profile.sendMessage(err + "Are you sure you want to " + label + "? Type " + hlt + "/hns " + label + err + " again to " + label + ".");
			confirms.add(profile.getPlayer().getName());
			Bukkit.getScheduler().runTaskLater(manager.plugin, () -> confirms.remove(profile.getPlayer().getName()), 200L);
			return;
		}
		
		if(!manager.getPlayerHandler().removePlayer(profile)){
			profile.sendMessage(err + "Failed to leave the game.");
			return;
		}
		profile.sendMessage(msg + "You have left the game.");
		confirms.remove(profile.getPlayer().getName());
	}
	
}
