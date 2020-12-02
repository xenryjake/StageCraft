package com.xenry.stagecraft.creative.gameplay.armorstand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
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
	
	public final boolean base, arms, small;
	public final EulerAngle head, body, leftLeg, rightLeg, leftArm, rightArm;
	public final ItemStack helmet, chestplate, leggings, boots, hand;
	
	public ArmorStandClipboard(boolean base, boolean arms, boolean small, EulerAngle head, EulerAngle body,
							   EulerAngle leftLeg, EulerAngle rightLeg, EulerAngle leftArm, EulerAngle rightArm,
							   ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
							   ItemStack hand) {
		this.base = base;
		this.arms = arms;
		this.small = small;
		this.head = head;
		this.body = body;
		this.leftLeg = leftLeg;
		this.rightLeg = rightLeg;
		this.leftArm = leftArm;
		this.rightArm = rightArm;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		this.hand = hand;
	}
	
	public static ArmorStandClipboard from(@NotNull ArmorStand as){
		ItemStack helmet = null;
		ItemStack chestplate = null;
		ItemStack leggings = null;
		ItemStack boots = null;
		ItemStack hand = null;
		EntityEquipment equip = as.getEquipment();
		if(equip != null){
			helmet = equip.getHelmet();
			if(helmet != null){
				helmet = helmet.clone();
			}
			chestplate = equip.getChestplate();
			if(chestplate != null){
				chestplate = chestplate.clone();
			}
			leggings = equip.getLeggings();
			if(leggings != null){
				leggings = leggings.clone();
			}
			boots = equip.getBoots();
			if(boots != null){
				boots = boots.clone();
			}
			hand = equip.getItemInMainHand().clone();
		}
		return new ArmorStandClipboard(as.hasBasePlate(), as.hasArms(), as.isSmall(), as.getHeadPose(),
				as.getBodyPose(), as.getLeftLegPose(), as.getRightLegPose(), as.getLeftArmPose(), as.getRightArmPose(),
				helmet, chestplate, leggings, boots, hand);
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
		EntityEquipment equip = as.getEquipment();
		if(equip != null){
			equip.setHelmet(helmet);
			equip.setChestplate(chestplate);
			equip.setLeggings(leggings);
			equip.setBoots(boots);
			equip.setItemInMainHand(hand);
		}
	}
	
}
