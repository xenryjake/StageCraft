package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.creative.gameplay.armorstand.Pose;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandPoseCommand extends Command<Creative,GameplayManager> {
	
	public static final int POSES_PER_PAGE = 20;
	
	public ArmorStandPoseCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "pose", "poses", "poselist");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length == 0 || args[0].matches("[0-9]+") || label.equalsIgnoreCase("poses")
				|| label.equalsIgnoreCase("poselist")){
			listPoses(player, args);
			return;
		}
		if(manager.isLockoutMode()){
			player.sendMessage(M.error("You can't edit armor stands while lockout mode is enabled."));
			return;
		}
		Pose pose = manager.getArmorStandHandler().getPose(args[0]);
		if(pose == null){
			player.sendMessage(M.error("There is no posed named " + args[0] + "."));
			return;
		}
		ArmorStand as = manager.getArmorStandHandler().getStand(profile);
		if(as == null || !manager.getArmorStandHandler().checkPerms(player, as)){
			return;
		}
		pose.apply(as);
		player.sendMessage(M.msg + "Set pose to " + M.elm + pose.getName() + M.msg + " on this armor stand.");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		listPoses(sender, args);
	}
	
	private void listPoses(CommandSender sender, String[] args){
		List<String> poses = manager.getArmorStandHandler().getPoseNameList();
		if(poses.isEmpty()){
			sender.sendMessage(M.error("There are no poses saved."));
			return;
		}
		int page = 1;
		if(args.length > 0){
			try{
				page = Integer.parseInt(args[0]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Please enter a valid integer."));
				return;
			}
		}
		
		int maxPages = (int) Math.ceil(poses.size() / (double)POSES_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstPose = (page-1) * POSES_PER_PAGE;
		int posesOnThisPage = Math.min(poses.size() - firstPose, POSES_PER_PAGE);
		List<String> posesToDisplay = poses.subList(firstPose, firstPose + posesOnThisPage);
		
		ComponentBuilder cb = new ComponentBuilder(" » ").color(ChatColor.DARK_GRAY).bold(true);
		Iterator<String> it = posesToDisplay.iterator();
		while(it.hasNext()){
			String name = it.next();
			cb.append(name).color(M.WHITE).bold(false);
			if(it.hasNext()){
				cb.append(", ").color(ChatColor.GRAY);
			}
		}
		
		if(poses.size() > POSES_PER_PAGE){
			sender.sendMessage(M.msg + "Showing poses, page " + M.elm + page + M.msg + " of " + M.elm + maxPages + M.msg + " (" + M.elm + (firstPose+1) + M.msg + "-" + M.elm + (firstPose + posesOnThisPage) + M.msg + " of " + M.elm + poses.size() + M.msg + " pose):");
		}else{
			sender.sendMessage(M.msg + "Showing all poses:");
		}
		sender.spigot().sendMessage(cb.create());
		sender.sendMessage(M.msg + "Use " + M.elm + "/as pose <name>" + M.msg + " to apply a pose.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getArmorStandHandler().getPoseNameList() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
