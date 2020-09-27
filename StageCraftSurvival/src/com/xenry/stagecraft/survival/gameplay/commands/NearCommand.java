package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.MapUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class NearCommand extends Command<Survival,GameplayManager> {
	
	public NearCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "near", "nearby");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		int radius = 200;
		if(args.length > 0){
			try{
				radius = Integer.parseInt(args[0]);
			}catch(Exception exception){
				profile.sendMessage(M.error("You specified an invalid integer."));
				return;
			}
		}
		Player player = profile.getPlayer();
		Map<String,Long> nearbyPlayers = new HashMap<>();
		for(Player nearby : player.getWorld().getPlayers()){
			if(nearby == player){
				continue;
			}
			long distance = Math.round(player.getLocation().distance(nearby.getLocation()));
			if(distance <= radius){
				nearbyPlayers.put(nearby.getName(), distance);
			}
		}
		
		nearbyPlayers = MapUtil.sortByValue(nearbyPlayers);
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String,Long> entry : nearbyPlayers.entrySet()){
			sb.append(M.WHITE).append(entry.getKey()).append("(").append(entry.getValue()).append("m)").append(M.gry).append(", ");
		}
		String nearbyString = sb.toString().trim();
		if(nearbyString.isEmpty()){
			nearbyString = M.WHITE + "none";
		}else{
			nearbyString = nearbyString.substring(0, nearbyString.length() - 1);
		}
		profile.sendMessage(M.msg + "Nearby players: " + nearbyString);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
