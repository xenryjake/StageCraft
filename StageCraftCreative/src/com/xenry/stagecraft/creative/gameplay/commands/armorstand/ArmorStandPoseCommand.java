package com.xenry.stagecraft.creative.gameplay.commands.armorstand;
import com.xenry.stagecraft.commands.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.NumberUtil;
import com.xenry.stagecraft.util.event.FakeEntityDamageByEntityEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandPoseCommand extends PlayerCommand<Creative,GameplayManager> {
	
	public ArmorStandPoseCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "pose");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length < 3){
			player.sendMessage(M.usage("/armorstand pose <part> <x|y|z> <angle>"));
			return;
		}
		Entity entity = player.getTargetEntity(5);
		if(!(entity instanceof ArmorStand)){
			player.sendMessage(M.error("You aren't looking at an armor stand."));
			return;
		}
		ArmorStand as = (ArmorStand)entity;
		if(as.isMarker() || (as.isInvisible() && !ArmorStandCommand.INTERACT_INVISIBLE.has(profile))){
			player.sendMessage(M.error("You can't interact with that armor stand."));
			return;
		}
		
		FakeEntityDamageByEntityEvent fedbee = new FakeEntityDamageByEntityEvent(player, as,
				EntityDamageEvent.DamageCause.CUSTOM, 1);
		manager.plugin.getServer().getPluginManager().callEvent(fedbee);
		if(fedbee.isCancelled()){
			return;
		}
		
		Part part;
		EulerAngle angle;
		switch(args[0].toLowerCase()){
			case "head":
				part = Part.HEAD;
				angle = as.getHeadPose();
				break;
			case "body":
				part = Part.BODY;
				angle = as.getBodyPose();
				break;
			case "leftleg":
			case "left_leg":
				part = Part.LEFT_LEG;
				angle = as.getLeftLegPose();
				break;
			case "rightleg":
			case "right_leg":
				part = Part.RIGHT_LEG;
				angle = as.getRightLegPose();
				break;
			case "leftarm":
			case "left_arm":
				part = Part.LEFT_ARM;
				angle = as.getLeftArmPose();
				break;
			case "rightarm":
			case "right_arm":
				part = Part.RIGHT_ARM;
				angle = as.getRightArmPose();
				break;
			default:
				player.sendMessage(M.error("Invalid part: " + args[0]));
				return;
		}
		if(!as.hasArms() && (part == Part.LEFT_ARM || part == Part.RIGHT_ARM)){
			profile.sendMessage(M.error("This armor stand doesn't have arms."));
			return;
		}
		Dimension dimension;
		try{
			dimension = Dimension.valueOf(args[1].toUpperCase());
		}catch(Exception ex){
			player.sendMessage(M.error("Invalid dimension: " + args[1]));
			return;
		}
		double value;
		try{
			value = Double.parseDouble(args[2]);
		}catch(Exception ex){
			player.sendMessage(M.error("Invalid angle: " + args[2]));
			return;
		}
		value = value % 360;
		double rad = Math.toRadians(value);
		if(dimension == Dimension.X){
			angle = angle.setX(rad);
		}else if(dimension == Dimension.Y){
			angle = angle.setY(rad);
		}else if(dimension == Dimension.Z){
			angle = angle.setZ(rad);
		}
		if(part == Part.HEAD){
			as.setHeadPose(angle);
		}else if(part == Part.BODY){
			as.setBodyPose(angle);
		}else if(part == Part.LEFT_LEG){
			as.setLeftLegPose(angle);
		}else if(part == Part.RIGHT_LEG){
			as.setRightLegPose(angle);
		}else if(part == Part.LEFT_ARM){
			as.setLeftArmPose(angle);
		}else{ // always will be RIGHT_ARM
			as.setRightArmPose(angle);
		}
		player.sendMessage(M.msg + "Set " + M.elm + part + " " + dimension + M.msg + " on armor stand to " + M.elm
				+ NumberUtil.displayAsHundredths(value) + "°" + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				return Arrays.asList("head", "body", "leftleg", "rightleg", "leftarm", "rightarm");
			case 2:
				return Arrays.asList("x", "y", "z");
			default:
				return Collections.emptyList();
		}
	}
	
	private enum Part {
		
		HEAD, BODY, LEFT_LEG, RIGHT_LEG, LEFT_ARM, RIGHT_ARM;
		
		public final String name;
		
		Part() {
			this.name = name().toLowerCase().replace("_", "");
		}
		
		@Override
		public String toString() {
			return name;
		}
		
	}
	
	private enum Dimension {
		
		X, Y, Z;
		
		@Override
		public String toString() {
			return name().toLowerCase();
		}
		
	}
	
}
