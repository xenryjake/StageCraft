package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.creative.gameplay.armorstand.Pose;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler.CAN_DELETE_POSES;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandDeletePoseCommand extends Command<Creative,GameplayManager> {
	
	public ArmorStandDeletePoseCommand(GameplayManager manager){
		super(manager, CAN_DELETE_POSES, "deletepose", "delpose", "removepose");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <name>"));
			return;
		}
		if(ArmorStandSavePoseCommand.ILLEGAL_NAMES.contains(args[0].toLowerCase())){
			sender.sendMessage(M.error("You can't use " + M.elm + args[0] + M.err + " as a pose name."));
			return;
		}
		Pose pose = manager.getArmorStandHandler().getPose(args[0]);
		if(pose == null){
			sender.sendMessage(M.error("That pose does not exist: " + args[0]));
			return;
		}
		manager.getArmorStandHandler().deletePose(pose);
		sender.sendMessage(M.msg + "Deleted pose " + M.elm + pose.getName() + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getArmorStandHandler().getPoseNameList(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getArmorStandHandler().getPoseNameList(args[0]) : Collections.emptyList();
	}
	
}
