package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandYawPitchCommand extends AbstractArmorStandCommand {
	
	public ArmorStandYawPitchCommand(ArmorStandHandler handler){
		super(handler, Rank.MEMBER, "yaw", "pitch");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length < 1){
			player.sendMessage(M.usage("/as " + label + " <angle>"));
			return;
		}
		if(manager.isLockoutMode()){
			player.sendMessage(M.error("You can't edit armor stands while lockout mode is enabled."));
			return;
		}
		float angle;
		try{
			angle = Float.parseFloat(args[0]);
		}catch(Exception ex){
			player.sendMessage(M.error("Invalid angle: " + args[0]));
			return;
		}
		ArmorStand as = handler.getStand(profile);
		if(as == null || !handler.checkPerms(player, as)){
			return;
		}
		boolean yaw = label.equalsIgnoreCase("yaw");
		Location location = as.getLocation();
		if(yaw){
			location.setYaw(angle % 360);
		}else{
			location.setPitch(angle % 180);
		}
		as.teleport(location);
		player.sendMessage(M.msg + "Set this armor stand's " + (yaw ? "yaw" : "pitch") + " to " + M.elm + angle + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
