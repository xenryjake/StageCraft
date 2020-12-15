package com.xenry.stagecraft.survival.gameplay.grapplinghook;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GrapplingHookCommand extends Command<Survival,GameplayManager> {

	public GrapplingHookCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "graphook");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		boolean status = GrapplingHookHandler.enabled;
		if(args.length < 1){
			sender.sendMessage(M.msg + "Grappling Hooks are currently " + (status ? "§aenabled" : "§cdisabled") + M.msg + ".");
			return;
		}
		args[0] = args[0].toLowerCase();
		args[0] = args[0].toLowerCase();
		boolean enable;
		if(args[0].startsWith("e") || args[0].startsWith("on")){
			enable = true;
		}else if(args[0].startsWith("d") || args[0].startsWith("off")){
			enable = false;
		}else if(args[0].startsWith("t")){
			enable = !status;
		}else{
			sender.sendMessage(M.error("Invalid argument: " + args[0]));
			return;
		}
		if(status == enable){
			sender.sendMessage(M.error("Grappling Hooks are already " + (status ? "enabled" : "disabled") + "."));
			return;
		}
		GrapplingHookHandler.enabled = enable;
		sender.sendMessage(M.msg + "Grappling hooks " + (enable ? "§aenabled" : "§cdisabled") + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("enable", "disable"), args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("enable", "disable"), args[0]) : Collections.emptyList();
	}
	
}
