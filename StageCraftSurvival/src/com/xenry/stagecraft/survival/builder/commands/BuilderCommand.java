package com.xenry.stagecraft.survival.builder.commands;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.builder.BuilderManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BuilderCommand extends Command<Survival,BuilderManager> {
	
	public BuilderCommand(BuilderManager manager){
		super(manager, Rank.MEMBER, "builder", "build", "bd");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(!manager.isBuilderModeEnabled()){
			profile.sendMessage(M.error("Builder mode is not available right now."));
			return;
		}
		Player target = profile.getPlayer();
		final boolean other = args.length >= 1 && profile.check(Rank.ADMIN);
		if(other){
			target = Bukkit.getPlayer(args[0]);
			if(target == null){
				profile.sendMessage(M.error("Cannot find player."));
				return;
			}
		}
		if(manager.plugin.getHideNSeekManager().getPlayerHandler().isInGame(target)){
			profile.sendMessage(M.error("Cannot enter build mode. " + (other ? target.getName() + "is" : "You are")
					+ " in a Hide'N'Seek game."));
			return;
		}
		if(manager.isBuilder(target)){
			manager.removeBuilder(target);
			target.sendMessage(M.msg + "Builder mode §cdisabled" + M.msg + ".");
			if(other){
				profile.sendMessage(M.msg + "Builder mode §cdisabled" + M.msg + " for " + M.elm + target.getName()
						+ M.msg + ".");
			}
		}else{
			if(PlayerUtil.hasItemsInInventory(target)){
				profile.sendMessage(M.error("Cannot enter build mode. " + (other ? target.getName() + "'s" : "Your")
						+ " inventory is not empty."));
				return;
			}
			if(!manager.isInBuildArea(target)){
				profile.sendMessage(M.error("Cannot enter build mode. " + (other ? target.getName() + " is" : "You are")
						+ " not in a build area."));
				return;
			}
			manager.addBuilder(target);
			target.sendMessage(M.msg + "Builder mode §aenabled" + M.msg + ".");
			if(other){
				profile.sendMessage(M.msg + "Builder mode §aenabled" + M.msg + " for " + M.elm + target.getName()
						+ M.msg + ".");
			}
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(!manager.isBuilderModeEnabled()){
			sender.sendMessage(M.error("Builder mode is not enabled."));
			return;
		}
		if(args.length < 1){
			sender.sendMessage("/" + label + " <player>");
			return;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(M.error("/Cannot find player."));
			return;
		}
		if(manager.isBuilder(target)){
			manager.removeBuilder(target);
			sender.sendMessage(M.msg + "Builder mode §cdisabled" + M.msg + " for " + M.elm + target.getName()
					+ M.msg + ".");
		}else{
			if(PlayerUtil.hasItemsInInventory(target)){
				sender.sendMessage(M.error("Cannot enter build mode. " + target.getName()
						+ "'s inventory is not empty."));
				return;
			}
			if(!manager.isInBuildArea(target)){
				sender.sendMessage(M.error("Cannot enter build mode. " + target.getName()
						+ " is not in a build area."));
				return;
			}
			manager.addBuilder(target);
			target.sendMessage(M.msg + "Builder mode §aenabled" + M.msg + ".");
			sender.sendMessage(M.msg + "Builder mode §aenabled" + M.msg + " for " + M.elm + target.getName()
					+ M.msg + ".");
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
}
