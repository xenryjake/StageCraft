package com.xenry.stagecraft.survival.jail.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.survival.jail.Sentence;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.survival.teleportation.Warp;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SentenceRemoveCommand extends Command<Survival,JailManager> {
	
	public SentenceRemoveCommand(JailManager manager){
		super(manager, Rank.MOD, "remove", "undo");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/punishments " + label + " <player>"));
			return;
		}
		
		Profile target;
		if(args[0].length() <= 17) {
			if(Bukkit.getPlayer(args[0]) != null) {
				target = getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
			} else {
				target = getCore().getProfileManager().getProfileByLatestUsername(args[0]);
			}
		} else {
			target = getCore().getProfileManager().getProfileByUUID(args[0]);
		}
		if(target == null) {
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		
		List<Sentence> list = manager.getActiveSentences(target);
		if(list.isEmpty()){
			sender.sendMessage(M.error(M.elm + target.getLatestUsername() + M.err + " doesn't have any active jail sentences on their record."));
			return;
		}
		
		int i = 0;
		for(Sentence sentence : list){
			i++;
			sentence.setRemoved(true);
			manager.save(sentence);
		}
		if(target.isOnline()){
			target.getPlayer().setGameMode(GameMode.SURVIVAL);
			Warp spawn = manager.plugin.getTeleportationManager().getWarpHandler().getSpawn();
			if(spawn != null){
				manager.plugin.getTeleportationManager().createAndExecuteTeleportation(target.getPlayer(), sender, target.getPlayer().getLocation(), spawn.getLocation(), Teleportation.Type.ADMIN, false);
			}
		}
		
		sender.sendMessage(M.msg + "Successfully removed " + M.elm + i + M.msg + " active jail sentences from " + M.elm + target.getLatestUsername() + M.msg + "'s profile.");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return serverTabComplete(profile.getPlayer(), args, label);
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
}
