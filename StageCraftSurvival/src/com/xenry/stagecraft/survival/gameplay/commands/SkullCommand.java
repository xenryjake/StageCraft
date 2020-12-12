package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SkullCommand extends PlayerCommand<Survival,GameplayManager> {
	
	public static final Access SPAWN_SKULL = Rank.ADMIN;
	
	public SkullCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "skull", "head", "pskull", "phead", "playerskull", "playerhead");
		setCanBeDisabled(true);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		String owner = player.getName();
		if(args.length > 0){
			owner = args[0];
			if(!owner.matches("^[A-Za-z0-9_]{1,16}$")){
				player.sendMessage(M.error("Invalid username: " + args[0]));
				return;
			}
		}
		
		ItemStack is = player.getInventory().getItemInMainHand();
		if(is != null && is.getType() == Material.PLAYER_HEAD){
			ItemMeta im = is.getItemMeta();
			if(im instanceof SkullMeta){
				SkullMeta skull = (SkullMeta)im;
				//noinspection deprecation
				skull.setOwner(owner);
				skull.setDisplayName("§e" + owner + "'s Head");
				is.setItemMeta(skull);
				player.sendMessage(M.msg + "Set skull owner to " + M.elm + owner + M.msg + ".");
				return;
			}
		}
		if(!SPAWN_SKULL.has(profile)){
			player.sendMessage(M.error("You aren't holding a skull."));
			return;
		}
		is = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta im = is.getItemMeta();
		if(!(im instanceof SkullMeta)){
			player.sendMessage(M.error("Could not spawn a skull."));
			return;
		}
		SkullMeta skull = (SkullMeta)im;
		//noinspection deprecation
		skull.setOwner(owner);
		skull.setDisplayName("§e" + owner + "'s Head");
		is.setItemMeta(skull);
		if(!PlayerUtil.hasSpaceForItemStack(player, is)){
			player.sendMessage(M.error("You don't have space in your inventory."));
			return;
		}
		player.getInventory().addItem(is);
		player.sendMessage(M.msg + "Spawned a skull owned by " + M.elm + owner + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0]) : Collections.emptyList();
	}
	
}
