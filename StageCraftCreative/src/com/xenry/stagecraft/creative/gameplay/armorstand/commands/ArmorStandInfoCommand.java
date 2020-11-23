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

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandInfoCommand extends AbstractArmorStandCommand {
	
	public ArmorStandInfoCommand(ArmorStandHandler handler){
		super(handler, Rank.MEMBER, "info");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		ArmorStand as = handler.getStand(profile);
		if(as == null){
			return;
		}
		String name = as.getCustomName();
		boolean small = as.isSmall();
		boolean arms = as.hasArms();
		boolean basePlate = as.hasBasePlate();
		EulerAngle body = as.getBodyPose();
		EulerAngle head = as.getHeadPose();
		EulerAngle leftLeg = as.getLeftLegPose();
		EulerAngle rightLeg = as.getRightLegPose();
		EulerAngle leftArm = as.getLeftArmPose();
		EulerAngle rightArm = as.getRightArmPose();
		
		player.sendMessage(M.msg + "Info about ArmorStand:");
		if(name != null && !name.isEmpty()){
			player.sendMessage(M.arrow("Custom name: " + M.WHITE + name));
		}
		player.sendMessage(M.arrow("Is small: " + (small ? "§atrue" : "§cfalse")));
		player.sendMessage(M.arrow("Has arms: " + (arms ? "§atrue" : "§cfalse")));
		player.sendMessage(M.arrow("Has base plate: " + (basePlate ? "§atrue" : "§cfalse")));
		player.sendMessage(M.arrow("Body pose: " + M.elm + display(body.getX()) + M.msg + ", "
				+ M.elm + display(body.getY()) + M.msg + ", " + M.elm + display(body.getZ())));
		player.sendMessage(M.arrow("Head pose: " + M.elm + display(head.getX()) + M.msg + ", "
				+ M.elm + display(head.getY()) + M.msg + ", " + M.elm + display(head.getZ())));
		player.sendMessage(M.arrow("Left leg pose: " + M.elm + display(leftLeg.getX()) + M.msg + ", "
				+ M.elm + display(leftLeg.getY()) + M.msg + ", " + M.elm + display(leftLeg.getZ())));
		player.sendMessage(M.arrow("Right leg pose: " + M.elm + display(rightLeg.getX()) + M.msg + ", "
				+ M.elm + display(rightLeg.getY()) + M.msg + ", " + M.elm + display(rightLeg.getZ())));
		if(arms){
			player.sendMessage(M.arrow("Left arm pose: " + M.elm + display(leftArm.getX()) + M.msg + ", "
					+ M.elm + display(leftArm.getY()) + M.msg + ", " + M.elm + display(leftArm.getZ())));
			player.sendMessage(M.arrow("Right arm pose: " + M.elm + display(rightArm.getX()) + M.msg + ", "
					+ M.elm + display(rightArm.getY()) + M.msg + ", " + M.elm + display(rightArm.getZ())));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	private static String display(double rad){
		return NumberUtil.displayAsHundredths(Math.toDegrees(rad)) + "°";
	}
	
}
