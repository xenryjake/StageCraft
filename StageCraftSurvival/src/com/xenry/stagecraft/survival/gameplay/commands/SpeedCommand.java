package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.ArrayUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SpeedCommand extends Command<Survival,GameplayManager> {
	
	public SpeedCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "speed");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		ArrayUtil.toLowerCase(args);
		if(args.length < 2) {
			sender.sendMessage(M.usage("/" + label + " <fly|walk> <speed> [player]"));
			return;
		}
		
		boolean fly;
		if(args[0].startsWith("w")){
			fly = false;
		}else if(args[0].startsWith("f")){
			fly = true;
		}else{
			sender.sendMessage(M.usage("/" + label + " <fly|walk> <speed> <player>"));
			return;
		}
		
		float speed;
		try{
			speed = Float.parseFloat(args[1]);
		}catch(Exception ex){
			sender.sendMessage(M.error("Invalid number: " + args[1]));
			return;
		}
		if(speed < 0 || speed > 10){
			sender.sendMessage(M.error("Speed must be between 0 and 10."));
			return;
		}
		
		Player target = sender.getPlayer();
		if(args.length > 2){
			target = Bukkit.getPlayer(args[2]);
			if(target == null){
				sender.sendMessage(M.playerNotFound(args[2]));
				return;
			}
		}
		
		if(fly){
			target.setFlySpeed(getRealSpeed(speed, true));
		}else{
			target.setWalkSpeed(getRealSpeed(speed, false));
		}
		sender.sendMessage(M.msg + "You set " + M.elm + target.getName() + M.msg + "'s " + M.elm
				+ (fly ? "fly" : "walk") + M.msg + " speed to " + M.elm + speed + M.msg + ".");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		ArrayUtil.toLowerCase(args);
		if(args.length < 3) {
			sender.sendMessage(M.usage("/" + label + " <fly|walk> <speed> <player>"));
			return;
		}
		
		boolean fly;
		if(args[0].startsWith("w")){
			fly = false;
		}else if(args[0].startsWith("f")){
			fly = true;
		}else{
			sender.sendMessage(M.usage("/" + label + " <fly|walk> <speed> <player>"));
			return;
		}
		
		float speed;
		try{
			speed = Float.parseFloat(args[1]);
		}catch(Exception ex){
			sender.sendMessage(M.error("Invalid number: " + args[1]));
			return;
		}
		if(speed < 0 || speed > 10){
			sender.sendMessage(M.error("Speed must be between 0 and 10."));
			return;
		}
		
		Player target = Bukkit.getPlayer(args[2]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[2]));
			return;
		}
		
		if(fly){
			target.setFlySpeed(getRealSpeed(speed, true));
		}else{
			target.setWalkSpeed(getRealSpeed(speed, false));
		}
		sender.sendMessage(M.msg + "You set " + M.elm + target.getName() + M.msg + "'s " + M.elm
				+ (fly ? "fly" : "walk") + M.msg + " speed to " + M.elm + speed + M.msg + ".");
	}
	
	private float getRealSpeed(float speed, boolean fly) {
		float defaultSpeed = fly ? 0.1f : 0.2f;
		float maxSpeed = 1f;
		
		if (speed < 1f) {
			return defaultSpeed * speed;
		} else {
			float ratio = ((speed - 1) / 9) * (maxSpeed - defaultSpeed);
			return ratio + defaultSpeed;
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return serverTabComplete(profile.getPlayer(), args, label);
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 0:
			case 1:
				return Arrays.asList("fly", "walk");
			case 3:
				return null;
			default:
				return Collections.emptyList();
		}
	}
	
}
