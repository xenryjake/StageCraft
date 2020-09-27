package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerMode;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.command.CommandSender;

import java.util.List;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HNSWhoCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSWhoCommand(HideNSeekManager manager){
		super(manager, Rank.MEMBER, "who", "list");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		StringBuilder hiderSB = new StringBuilder().append(PlayerMode.HIDER.getColoredName()).append("s").append(gry).append(": ");
		List<String> hiders = manager.getPlayerHandler().getPlayerNamesByMode(PlayerMode.HIDER);
		if(hiders.size() < 1){
			hiderSB.append(gry).append("none");
		}else{
			for(String name : hiders){
				hiderSB.append("§f ").append(name).append(",");
			}
		}
		
		StringBuilder seekerSB = new StringBuilder().append(PlayerMode.SEEKER.getColoredName()).append("s").append(gry).append(": ");
		List<String> seekers = manager.getPlayerHandler().getPlayerNamesByMode(PlayerMode.SEEKER);
		if(seekers.size() < 1){
			seekerSB.append(gry).append("none");
		}else{
			for(String name : seekers){
				seekerSB.append("§f ").append(name).append(",");
			}
		}
		
		StringBuilder spectatorSB = new StringBuilder().append(PlayerMode.SPECTATOR.getColoredName()).append("s").append(gry).append(": ");
		List<String> spectators = manager.getPlayerHandler().getPlayerNamesByMode(PlayerMode.SPECTATOR);
		if(spectators.size() < 1){
			spectatorSB.append(gry).append("none");
		}else{
			for(String name : spectators){
				spectatorSB.append("§f ").append(name).append(",");
			}
		}
		
		sender.sendMessage(hiderSB.toString());
		sender.sendMessage(seekerSB.toString());
		sender.sendMessage(spectatorSB.toString());
	}
	
}
