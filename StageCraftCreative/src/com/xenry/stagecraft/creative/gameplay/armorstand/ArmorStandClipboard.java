package com.xenry.stagecraft.creative.gameplay.armorstand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandClipboard {
	
	public final boolean base;
	public final boolean arms;
	public final boolean small;
	public final EulerAngle head, body, leftLeg, rightLeg, leftArm, rightArm;
	
	public ArmorStandClipboard(boolean base, boolean arms, boolean small, EulerAngle head, EulerAngle body,
							   EulerAngle leftLeg, EulerAngle rightLeg, EulerAngle leftArm, EulerAngle rightArm) {
		this.base = base;
		this.arms = arms;
		this.small = small;
		this.head = head;
		this.body = body;
		this.leftLeg = leftLeg;
		this.rightLeg = rightLeg;
		this.leftArm = leftArm;
		this.rightArm = rightArm;
	}
	
	public static ArmorStandClipboard from(@NotNull ArmorStand as){
		return new ArmorStandClipboard(as.hasBasePlate(), as.hasArms(), as.isSmall(), as.getHeadPose(),
				as.getBodyPose(), as.getLeftLegPose(), as.getRightLegPose(), as.getLeftArmPose(), as.getRightArmPose());
	}
	
	public void apply(@NotNull ArmorStand as){
		as.setBasePlate(base);
		as.setArms(arms);
		as.setSmall(small);
		as.setHeadPose(head);
		as.setBodyPose(body);
		as.setLeftLegPose(leftLeg);
		as.setRightLegPose(rightLeg);
		as.setLeftArmPose(leftArm);
		as.setRightArmPose(rightArm);
	}
	
}
