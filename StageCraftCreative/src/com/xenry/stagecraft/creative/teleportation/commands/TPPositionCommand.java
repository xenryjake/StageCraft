package com.xenry.stagecraft.creative.teleportation.commands;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TPPositionCommand extends PlayerCommand<Creative,TeleportationManager> {
	
	public TPPositionCommand(TeleportationManager manager){
		super(manager, TPCommand.SELF_RANK, "teleportposition", "teleportpos", "tpposition", "tppos");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 3){
			profile.sendMessage(M.usage("/" + label + " <x> <y> <z> [yaw] [pitch]"));
			return;
		}
		if(args.length > 5){
			profile.sendMessage(M.error("You supplied too many arguments. This command does not support teleporting other players"));
			return;
		}
		
		double[] coordinates = new double[3];
		boolean[] relative = new boolean[3];
		
		for(int i = 0; i < 3; i++){
			if(args[i].equals("~")){
				args[i] = "~0";
			}
			relative[i] = args[i].contains("~");
			if(relative[i]){
				args[i] = args[i].replaceAll("~", "");
			}
			try{
				coordinates[i] = Double.parseDouble(args[i]);
			}catch(Exception ex){
				profile.sendMessage(M.error("Invalid number: " + args[i]));
				return;
			}
		}
		
		Player player = profile.getPlayer();
		World world = player.getWorld();
		Location pl = player.getLocation();
		
		Location location = new Location(world,
				coordinates[0] + (relative[0] ? pl.getX() : 0),
				coordinates[1] + (relative[1] ? pl.getY() : 0),
				coordinates[2] + (relative[2] ? pl.getZ() : 0),
				pl.getYaw(),
				pl.getPitch()
		);
		
		if(args.length > 3){
			try{
				location.setYaw(Location.normalizeYaw(Float.parseFloat(args[3])));
			}catch(Exception ex){
				profile.sendMessage(M.error("Invalid number: " + args[3]));
				return;
			}
			if(args.length > 4){
				try{
					location.setPitch(Location.normalizePitch(Float.parseFloat(args[4])));
				}catch(Exception ex){
					profile.sendMessage(M.error("Invalid number: " + args[4]));
					return;
				}
			}
		}
		
		manager.createAndExecuteTeleportation(player, player, player.getLocation(), location, Teleportation.Type.ADMIN, false);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		switch(args.length){
			case 1:
				return new ArrayList<>(Collections.singleton(String.valueOf(player.getLocation().getBlockX())));
			case 2:
				return new ArrayList<>(Collections.singleton(String.valueOf(player.getLocation().getBlockY())));
			case 3:
				return new ArrayList<>(Collections.singleton(String.valueOf(player.getLocation().getBlockZ())));
			case 4:
				return new ArrayList<>(Collections.singleton(String.valueOf(player.getLocation().getYaw())));
			case 5:
				return new ArrayList<>(Collections.singleton(String.valueOf(player.getLocation().getPitch())));
			default:
				return Collections.emptyList();
		}
	}
	
}
