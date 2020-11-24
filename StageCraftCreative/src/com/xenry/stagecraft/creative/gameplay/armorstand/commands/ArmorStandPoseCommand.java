package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandPoseCommand extends AbstractArmorStandCommand {
	
	public ArmorStandPoseCommand(ArmorStandHandler handler){
		super(handler, Rank.MEMBER, "pose");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(manager.isLockoutMode()){
			player.sendMessage(M.error("You can't edit armor stands while lockout mode is enabled."));
			return;
		}
		if(args.length < 1){
			player.sendMessage(M.usage("/armorstand pose <pose>"));
			return;
		}
		Pose pose;
		try{
			pose = Pose.valueOf(args[0].toUpperCase());
		}catch(Exception ex){
			player.sendMessage(M.error("Invalid pose: " + args[0]));
			return;
		}
		ArmorStand as = handler.getStand(profile);
		if(as == null || !handler.checkPerms(player, as)){
			return;
		}
		as.setHeadPose(pose.head);
		as.setBodyPose(pose.body);
		as.setRightLegPose(pose.rightLeg);
		as.setLeftLegPose(pose.leftLeg);
		as.setRightArmPose(pose.rightArm);
		as.setLeftArmPose(pose.leftArm);
		player.sendMessage(M.msg + "Set pose to " + M.elm + pose.name + M.msg + " on this armor stand.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? Pose.names() : Collections.emptyList();
	}
	
	private enum Pose {
		
		RESET("Reset", new EulerAngle(0,0,0), new EulerAngle(0,0,0),
				new EulerAngle(0,0,0), new EulerAngle(0,0,0),
				new EulerAngle(0,0,0), new EulerAngle(0,0,0)),
		CLEAR(RESET),
		T_POSE("T Pose", new EulerAngle(0,0,0), new EulerAngle(0,0,0),
				new EulerAngle(0, 0,0), new EulerAngle(0,0,0),
				new EulerAngle(0, 0, Math.toRadians(-90)), new EulerAngle(0, 0, Math.toRadians(90))),
		SITTING("Sitting", new EulerAngle(0,0,0), new EulerAngle(0,0,0),
				new EulerAngle(Math.toRadians(-90), 0,0), new EulerAngle(Math.toRadians(-90),0,0),
				new EulerAngle(0, 0, 0), new EulerAngle(0, 0, 0)),
		WALKING("Walking", new EulerAngle(0,0,0), new EulerAngle(0,0,0),
				new EulerAngle(Math.toRadians(-30), 0,0), new EulerAngle(Math.toRadians(30),0,0),
				new EulerAngle(Math.toRadians(30), 0, 0), new EulerAngle(Math.toRadians(-30), 0, 0));
		
		private static List<String> names = null;
		
		public final String name;
		public final EulerAngle head, body, leftLeg, rightLeg, leftArm, rightArm;
		
		Pose(String name, EulerAngle head, EulerAngle body, EulerAngle leftLeg, EulerAngle rightLeg, EulerAngle leftArm,
			 EulerAngle rightArm) {
			this.name = name;
			this.head = head;
			this.body = body;
			this.leftLeg = leftLeg;
			this.rightLeg = rightLeg;
			this.leftArm = leftArm;
			this.rightArm = rightArm;
		}
		
		Pose(Pose pose){
			this(pose.name, pose.head, pose.body, pose.leftLeg, pose.rightLeg, pose.leftArm, pose.rightArm);
		}
		
		public static List<String> names(){
			if(names != null){
				return names;
			}
			names = new ArrayList<>();
			for(Pose pose : values()){
				names.add(pose.name().toLowerCase());
			}
			return names;
		}
		
	}
	
}
