package com.xenry.stagecraft.creative.gameplay.commands.armorstand;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandCommand extends PlayerCommand<Creative,GameplayManager> {
	
	public static final Access INTERACT_INVISIBLE = Rank.ADMIN;
	
	public ArmorStandCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "armorstand", "as");
		setCanBeDisabled(true);
		addSubCommand(new ArmorStandInfoCommand(manager));
		addSubCommand(new ArmorStandArmsCommand(manager));
		addSubCommand(new ArmorStandBaseCommand(manager));
		addSubCommand(new ArmorStandSmallCommand(manager));
		addSubCommand(new ArmorStandPoseCommand(manager));
		addSubCommand(new ArmorStandCenterCommand(manager));
		addSubCommand(new ArmorStandInvisibleCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		profile.sendMessage(M.msg + "Armor Stand commands:");
		profile.sendMessage(M.help(label + " info", "View info about an armor stand."));
		profile.sendMessage(M.help(label + " arms", "Toggle arms on an armor stand."));
		profile.sendMessage(M.help(label + " base", "Toggle the base plate on an armor stand."));
		profile.sendMessage(M.help(label + " small", "Toggle the size of an armor stand."));
		profile.sendMessage(M.help(label + " pose", "Change the pose of the armor stand."));
		profile.sendMessage(M.help(label + " center", "Center an armor stand in the current block."));
		if(INTERACT_INVISIBLE.has(profile)){
			profile.sendMessage(M.help(label + " invisible", "Toggle armor stand visibility"));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			ArrayList<String> list = new ArrayList<>(Arrays.asList("info", "arms", "base", "small", "pose", "center"));
			if(INTERACT_INVISIBLE.has(profile)){
				list.add("invisible");
			}
			return list;
		}else{
			return Collections.emptyList();
		}
	}
	
}
