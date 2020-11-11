package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class EnderChestCommand extends Command<Creative,GameplayManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	
	public EnderChestCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "enderchest", "echest", "ec");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player target;
		if(args.length < 1 || !OTHERS.has(profile)){
			target = profile.getPlayer();
		}else{
			target = Bukkit.getPlayer(args[0]);
			if(target == null){
				profile.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}
		profile.getPlayer().closeInventory();
		profile.getPlayer().openInventory(target.getEnderChest());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return OTHERS.has(profile) && args.length == 1 ? allLocalPlayers() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
