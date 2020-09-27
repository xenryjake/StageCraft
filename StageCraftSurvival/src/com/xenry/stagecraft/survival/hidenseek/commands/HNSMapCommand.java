package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameStatus;
import com.xenry.stagecraft.survival.hidenseek.map.Map;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.command.CommandSender;

import java.util.List;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HNSMapCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSMapCommand(HideNSeekManager manager){
		super(manager, Rank.ADMIN, "map");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			StringBuilder sb = new StringBuilder().append(msg).append("Maps: ");
			for(Map map : manager.getAllMaps()){
				sb.append(gry).append(map.getName()).append(msg).append(", ");
			}
			sender.sendMessage(sb.toString());
			sender.sendMessage(msg + "Current map: " + manager.getMap().getName());
			return;
		}
		
		if(manager.getStatus() != GameStatus.NONE){
			sender.sendMessage(err + "You must set the map before you create a game.");
			return;
		}
		
		Map newMap = null;
		for(Map map : manager.getAllMaps()){
			if(map.getName().equalsIgnoreCase(args[0])){
				newMap = map;
				break;
			}
		}
		if(newMap == null){
			sender.sendMessage(err + "Map not found.");
			return;
		}
		sender.sendMessage(msg + "New map: " + newMap.getName());
		manager.setMap(newMap);
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return manager.getAllMapNames();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return manager.getAllMapNames();
	}
	
}
