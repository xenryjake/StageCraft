package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.creative.gameplay.armorstand.Pose;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandSavePoseCommand extends AbstractArmorStandCommand {
	
	public static final int MAX_POSES = 1000;
	
	public static final List<String> ILLEGAL_NAMES = Arrays.asList("list","l","clear","reset","save","head","body",
			"leftleg","rightleg","leftarm","rightarm");
	
	public ArmorStandSavePoseCommand(ArmorStandHandler handler){
		super(handler, ArmorStandHandler.CAN_MAKE_POSES, "savepose", "save", "posesave");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/armorstand " + label + " <name>"));
			return;
		}
		String name = args[0];
		if(!name.matches("^[A-Za-z0-9_]+$")){
			profile.sendMessage(M.error("Pose names can only contain alphanumeric characters and underscores."));
			return;
		}
		if(name.replaceAll("[0-9_]", "").isEmpty()){
			profile.sendMessage(M.error("Pose names must contain at least one letter."));
			return;
		}
		if(name.length() > 32 || name.length() < 2){
			profile.sendMessage(M.error("Pose names must be between 2 and 32 characters long."));
			return;
		}
		if(ILLEGAL_NAMES.contains(name.toLowerCase())){
			profile.sendMessage(M.error("You can't use " + M.elm + name + M.err + " as a pose name."));
			return;
		}
		if(handler.getPose(name) != null){
			profile.sendMessage(M.error("A pose with that name already exists."));
			return;
		}
		if(handler.getPoses().size() >= MAX_POSES){
			profile.sendMessage(M.error("The server has reached its maximum number of poses."));
			return;
		}
		ArmorStand as = handler.getStand(profile);
		if(as == null){
			return;
		}
		handler.addPose(new Pose(name, as));
		profile.sendMessage(M.msg + "You saved a new pose: " + M.elm + name + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
