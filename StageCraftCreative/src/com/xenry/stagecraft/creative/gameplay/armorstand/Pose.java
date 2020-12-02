package com.xenry.stagecraft.creative.gameplay.armorstand;
import com.mongodb.BasicDBObject;
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
public class Pose extends BasicDBObject {
	
	@Deprecated
	public Pose(){
		//required for Mongo instantiation
	}
	
	public Pose(String name, @NotNull ArmorStand as){
		this(name, as.getHeadPose(), as.getBodyPose(), as.getLeftLegPose(), as.getRightLegPose(), as.getLeftArmPose(),
				as.getRightArmPose());
	}
	
	public Pose(String name, EulerAngle head, EulerAngle body, EulerAngle leftLeg, EulerAngle rightLeg,
				EulerAngle leftArm, EulerAngle rightArm){
		put("name", name);
		
		put("headX", head.getX());
		put("headY", head.getY());
		put("headZ", head.getZ());
		
		put("bodyX", body.getX());
		put("bodyY", body.getY());
		put("bodyZ", body.getZ());
		
		put("leftLegX", leftLeg.getX());
		put("leftLegY", leftLeg.getY());
		put("leftLegZ", leftLeg.getZ());
		
		put("rightLegX", rightLeg.getX());
		put("rightLegY", rightLeg.getY());
		put("rightLegZ", rightLeg.getZ());
		
		put("leftArmX", leftArm.getX());
		put("leftArmY", leftArm.getY());
		put("leftArmZ", leftArm.getZ());
		
		put("rightArmX", rightArm.getX());
		put("rightArmY", rightArm.getY());
		put("rightArmZ", rightArm.getZ());
	}
	
	public String getName(){
		return getString("name");
	}
	
	public EulerAngle getHead(){
		return new EulerAngle(getDouble("headX"), getDouble("headY"), getDouble("headZ"));
	}
	
	public EulerAngle getBody(){
		return new EulerAngle(getDouble("bodyX"), getDouble("bodyY"), getDouble("bodyZ"));
	}
	
	public EulerAngle getLeftLeg(){
		return new EulerAngle(getDouble("leftLegX"), getDouble("leftLegY"), getDouble("leftLegZ"));
	}
	
	public EulerAngle getRightLeg(){
		return new EulerAngle(getDouble("rightLegX"), getDouble("rightLegY"), getDouble("rightLegZ"));
	}
	
	public EulerAngle getLeftArm(){
		return new EulerAngle(getDouble("leftArmX"), getDouble("leftArmY"), getDouble("leftArmZ"));
	}
	
	public EulerAngle getRightArm(){
		return new EulerAngle(getDouble("rightArmX"), getDouble("rightArmY"), getDouble("rightArmZ"));
	}
	
	public void apply(@NotNull ArmorStand as){
		as.setHeadPose(getHead());
		as.setBodyPose(getBody());
		as.setLeftLegPose(getLeftLeg());
		as.setRightLegPose(getRightLeg());
		as.setLeftArmPose(getLeftArm());
		as.setRightArmPose(getRightArm());
	}
	
}
