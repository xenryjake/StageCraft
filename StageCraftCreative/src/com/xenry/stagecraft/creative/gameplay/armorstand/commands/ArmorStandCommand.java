package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandCommand extends Command<Creative,GameplayManager> {
	
	public ArmorStandCommand(ArmorStandHandler handler){
		super(handler.getManager(), Rank.MEMBER, "armorstand", "as");
		setCanBeDisabled(true);
		addSubCommand(new ArmorStandInfoCommand(handler));
		addSubCommand(new ArmorStandArmsCommand(handler));
		addSubCommand(new ArmorStandBaseCommand(handler));
		addSubCommand(new ArmorStandSmallCommand(handler));
		addSubCommand(new ArmorStandPoseCommand(getManager()));
		addSubCommand(new ArmorStandAdjustCommand(handler));
		addSubCommand(new ArmorStandCenterCommand(handler));
		addSubCommand(new ArmorStandCopyCommand(handler));
		addSubCommand(new ArmorStandPasteCommand(handler));
		addSubCommand(new ArmorStandSavePoseCommand(handler));
		addSubCommand(new ArmorStandDeletePoseCommand(getManager()));
		addSubCommand(new ArmorStandInvisibleCommand(handler));
		addSubCommand(new ArmorStandGravityCommand(handler));
		addSubCommand(new ArmorStandYawPitchCommand(handler));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Armor Stand commands:");
		sender.sendMessage(M.help(label + " info", "View info about an armor stand."));
		sender.sendMessage(M.help(label + " arms", "Toggle arms on an armor stand."));
		sender.sendMessage(M.help(label + " base", "Toggle the base plate on an armor stand."));
		sender.sendMessage(M.help(label + " small", "Toggle the size of an armor stand."));
		sender.sendMessage(M.help(label + " pose", "Change the pose of the armor stand."));
		sender.sendMessage(M.help(label + " <part>", "Adjust a part of the armor stand."));
		sender.sendMessage(M.help(label + " center", "Center an armor stand in the current block."));
		sender.sendMessage(M.help(label + " copy", "Copy an armor stand to your clipboard."));
		sender.sendMessage(M.help(label + " paste", "Paste an armor stand from your clipboard."));
		sender.sendMessage(M.help(label + " gravity", "Toggle gravity on an armor stand."));
		sender.sendMessage(M.help(label + " savepose", "Save a pose to the database."));
		sender.sendMessage(M.help(label + " delpose", "Delete a pose from the database."));
		sender.sendMessage(M.help(label + " invisible", "Toggle armor stand visibility"));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.sendMessage(M.msg + "Armor Stand commands:");
		profile.sendMessage(M.help(label + " info", "View info about an armor stand."));
		profile.sendMessage(M.help(label + " arms", "Toggle arms on an armor stand."));
		profile.sendMessage(M.help(label + " base", "Toggle the base plate on an armor stand."));
		profile.sendMessage(M.help(label + " small", "Toggle the size of an armor stand."));
		profile.sendMessage(M.help(label + " pose", "Change the pose of the armor stand."));
		profile.sendMessage(M.help(label + " <part>", "Adjust a part of the armor stand."));
		profile.sendMessage(M.help(label + " center", "Center an armor stand in the current block."));
		profile.sendMessage(M.help(label + " copy", "Copy an armor stand to your clipboard."));
		profile.sendMessage(M.help(label + " paste", "Paste an armor stand from your clipboard."));
		if(CAN_APPLY_GRAVITY.has(profile)){
			profile.sendMessage(M.help(label + " gravity", "Toggle gravity on an armor stand."));
		}
		if(CAN_MAKE_POSES.has(profile)){
			profile.sendMessage(M.help(label + " savepose", "Save a pose to the database."));
		}
		if(CAN_DELETE_POSES.has(profile)){
			profile.sendMessage(M.help(label + " delpose", "Delete a pose from the database."));
		}
		if(INTERACT_INVISIBLE.has(profile)){
			profile.sendMessage(M.help(label + " invisible", "Toggle armor stand visibility"));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			ArrayList<String> list = new ArrayList<>(Arrays.asList("info", "arms", "base", "small", "pose", "center",
					"head", "body", "leftArm", "rightArm", "leftLeg", "rightLeg", "copy", "paste", "gravity", "yaw", "pitch"));
			if(CAN_MAKE_POSES.has(profile)){
				list.add("savepose");
			}
			if(CAN_DELETE_POSES.has(profile)){
				list.add("delpose");
			}
			if(INTERACT_INVISIBLE.has(profile)){
				list.add("invisible");
			}
			return filter(list, args[0]);
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
