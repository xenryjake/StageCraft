package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.NumberUtil;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
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
public final class ArmorStandAdjustCommand extends AbstractArmorStandCommand {
	
	public ArmorStandAdjustCommand(ArmorStandHandler handler){
		super(handler, Rank.MEMBER, "head", "body", "leftarm", "rightarm", "leftleg", "rightleg", "left_arm",
				"right_arm", "left_leg", "right_leg");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length < 1){
			player.sendMessage(M.usage("/armorstand <part> <clear|x|y|z> <angle>"));
			return;
		}
		Part part = Part.getPart(label);
		if(part == null){
			player.sendMessage(M.error("Invalid part: " + label));
			return;
		}
		if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("reset")){
			resetPart(profile, part);
		}else{
			precisePart(profile, args, part);
		}
	}
	
	private void resetPart(Profile profile, Part part){
		Player player = profile.getPlayer();
		ArmorStand as = handler.getStand(profile);
		if(as == null){
			return;
		}
		if(!as.hasArms() && (part == Part.LEFT_ARM || part == Part.RIGHT_ARM)){
			profile.sendMessage(M.error("This armor stand doesn't have arms."));
			return;
		}
		if(!handler.checkPerms(player, as)){
			return;
		}
		setPart(as, part, new EulerAngle(0, 0, 0));
		player.sendMessage(M.msg + "Reset " + M.elm + part + M.msg + " pose on this armor stand.");
	}
	
	private void precisePart(Profile profile, String[] args, Part part){
		Player player = profile.getPlayer();
		if(args.length < 2){
			player.sendMessage(M.usage("/armorstand <part> <clear|x|y|z> <angle>"));
			return;
		}
		ArmorStand as = handler.getStand(profile);
		if(as == null){
			return;
		}
		
		EulerAngle angle;
		if(part == Part.HEAD){
			angle = as.getHeadPose();
		}else if(part == Part.BODY){
			angle = as.getBodyPose();
		}else if(part == Part.LEFT_LEG){
			angle = as.getLeftLegPose();
		}else if(part == Part.RIGHT_LEG){
			angle = as.getRightLegPose();
		}else if(part == Part.LEFT_ARM){
			angle = as.getLeftArmPose();
		}else if(part == Part.RIGHT_ARM){
			angle = as.getRightArmPose();
		}else{
			player.sendMessage(M.error("Invalid part."));
			return;
		}
		
		Dimension dimension;
		try{
			dimension = Dimension.valueOf(args[0].toUpperCase());
		}catch(Exception ex){
			player.sendMessage(M.error("Invalid dimension: " + args[0]));
			return;
		}
		double value;
		try{
			value = Double.parseDouble(args[1]);
		}catch(Exception ex){
			player.sendMessage(M.error("Invalid angle: " + args[1]));
			return;
		}
		if(!handler.checkPerms(player, as)){
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
		
		setPart(as, part, angle);
		player.sendMessage(M.msg + "Set " + M.elm + part + " " + dimension + M.msg + " on armor stand to " + M.elm
				+ NumberUtil.displayAsHundredths(value) + "°" + M.msg + ".");
	}
	
	private void setPart(ArmorStand as, @NotNull Part part, EulerAngle angle){
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
		}else if(part == Part.RIGHT_ARM){
			as.setRightArmPose(angle);
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("x", "y", "z", "clear") : Collections.emptyList();
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
		
		public static Part getPart(String string){
			switch(string.toLowerCase()){
				case "head":
					return Part.HEAD;
				case "body":
					return Part.BODY;
				case "leftleg":
				case "left_leg":
					return Part.LEFT_LEG;
				case "rightleg":
				case "right_leg":
					return Part.RIGHT_LEG;
				case "leftarm":
				case "left_arm":
					return Part.LEFT_ARM;
				case "rightarm":
				case "right_arm":
					return Part.RIGHT_ARM;
				default:
					return null;
			}
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
