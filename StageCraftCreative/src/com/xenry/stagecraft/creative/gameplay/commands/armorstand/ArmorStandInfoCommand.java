package com.xenry.stagecraft.creative.gameplay.commands.armorstand;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.NumberUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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
public final class ArmorStandInfoCommand extends Command<Creative,GameplayManager> {
	
	public ArmorStandInfoCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "info");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
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
		player.sendMessage(M.arrow("Body pose: " + M.elm + display(body.getX()) + M.msg + "°, "
				+ M.elm + display(body.getY()) + M.msg + "°, " + M.elm + display(body.getZ()) + M.msg + "°"));
		player.sendMessage(M.arrow("Head pose: " + M.elm + display(head.getX()) + M.msg + "°, "
				+ M.elm + display(head.getY()) + M.msg + "°, " + M.elm + display(head.getZ()) + M.msg + "°"));
		player.sendMessage(M.arrow("Left leg pose: " + M.elm + display(leftLeg.getX()) + M.msg + "°, "
				+ M.elm + display(leftLeg.getY()) + M.msg + "°, " + M.elm + display(leftLeg.getZ()) + M.msg + "°"));
		player.sendMessage(M.arrow("Right leg pose: " + M.elm + display(rightLeg.getX()) + M.msg + "°, "
				+ M.elm + display(rightLeg.getY()) + M.msg + "°, " + M.elm + display(rightLeg.getZ()) + M.msg + "°"));
		if(arms){
			player.sendMessage(M.arrow("Left arm pose: " + M.elm + display(leftArm.getX()) + M.msg + "°, "
					+ M.elm + display(leftArm.getY()) + M.msg + "°, " + M.elm + display(leftArm.getZ()) + M.msg + "°"));
			player.sendMessage(M.arrow("Right arm pose: " + M.elm + display(rightArm.getX()) + M.msg + "°, "
					+ M.elm + display(rightArm.getY()) + M.msg + "°, " + M.elm + display(rightArm.getZ()) + M.msg + "°"));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
	private static String display(double rad){
		return NumberUtil.displayAsHundredths(Math.toDegrees(rad));
	}
	
}
