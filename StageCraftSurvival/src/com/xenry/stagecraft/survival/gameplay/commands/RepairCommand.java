package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class RepairCommand extends Command<Survival,GameplayManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	
	public RepairCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "repair", "fix");
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		Player target = sender.getPlayer();
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <all|hand|armor|offhand> [player]"));
			return;
		}
		if(args.length > 1 && OTHERS.has(sender)){
			target = Bukkit.getPlayer(args[1]);
			if(target == null){
				sender.sendMessage(M.playerNotFound(args[1]));
				return;
			}
		}
		
		Slots slots;
		if(args[0].toLowerCase().startsWith("h")){
			slots = Slots.HAND;
		}else if(args[0].toLowerCase().startsWith("o")){
			slots = Slots.OFFHAND;
		}else if(args[0].toLowerCase().startsWith("ar")){
			slots = Slots.ARMOR;
		}else if(args[0].toLowerCase().startsWith("al")){
			slots = Slots.ALL;
		}else{
			sender.sendMessage(M.error("Invalid slot to repair."));
			return;
		}
		
		doRepair(target, sender.getPlayer(), slots);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <all|hand|armor|offhand> <player>"));
			return;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if(target == null){
			sender.sendMessage(M.playerNotFound(args[1]));
			return;
		}
		
		Slots slots;
		if(args[0].toLowerCase().startsWith("h")){
			slots = Slots.HAND;
		}else if(args[0].toLowerCase().startsWith("o")){
			slots = Slots.OFFHAND;
		}else if(args[0].toLowerCase().startsWith("ar")){
			slots = Slots.ARMOR;
		}else if(args[0].toLowerCase().startsWith("al")){
			slots = Slots.ALL;
		}else{
			sender.sendMessage(M.error("Invalid slot to repair."));
			return;
		}
		
		doRepair(target, sender, slots);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				return filter(Arrays.asList("all", "hand", "armor", "offhand"), args[0]);
			case 2:
				return localPlayers(args[0]);
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 1:
				return filter(Arrays.asList("all", "hand", "armor", "offhand"), args[0]);
			case 2:
				return localPlayers(args[0]);
			default:
				return Collections.emptyList();
		}
	}
	
	private void doRepair(Player target, CommandSender sender, Slots slots){
		boolean different = target != sender;
		switch(slots){
			case HAND:{
				ItemStack item = target.getInventory().getItemInMainHand();
				ItemMeta meta = item.getItemMeta();
				if(meta instanceof Damageable){
					((Damageable)meta).setDamage(0);
					item.setItemMeta(meta);
					sender.sendMessage(M.msg + "The item in " + (different ? M.elm + target.getName() + M.msg + "'s" : "your") + " hand has been repaired");
				}else{
					sender.sendMessage(M.error("That item cannot be repaired."));
				}
				break;
			}
			case OFFHAND: {
				ItemStack item = target.getInventory().getItemInOffHand();
				ItemMeta meta = item.getItemMeta();
				if(meta instanceof Damageable){
					((Damageable)meta).setDamage(0);
					item.setItemMeta(meta);
					sender.sendMessage(M.msg + "The item in " + (different ? M.elm + target.getName() + M.msg + "'s" : "your") + " offhand has been repaired");
				}else{
					sender.sendMessage(M.error("That item cannot be repaired."));
				}
				break;
			}
			case ARMOR: {
				ItemStack[] contents = target.getInventory().getArmorContents();
				boolean repaired = false;
				for(ItemStack content : contents) {
					if(content == null){
						continue;
					}
					ItemMeta meta = content.getItemMeta();
					if(meta instanceof Damageable) {
						((Damageable)meta).setDamage(0);
						content.setItemMeta(meta);
						repaired = true;
					}
				}
				if(repaired){
					sender.sendMessage((different ? M.elm + target.getName() + M.msg + "'s" : "Your") +  " armor has been repaired.");
				}else{
					sender.sendMessage(M.error("No items in " + (different ? M.elm + target.getName() + M.err + "'s" : "your") + " armor slots could be repaired."));
				}
				break;
			}
			case ALL: {
				ItemStack[] contents = target.getInventory().getContents();
				boolean repaired = false;
				for(ItemStack content : contents) {
					if(content == null){
						continue;
					}
					ItemMeta meta = content.getItemMeta();
					if(meta instanceof Damageable) {
						((Damageable)meta).setDamage(0);
						content.setItemMeta(meta);
						repaired = true;
					}
				}
				if(repaired){
					sender.sendMessage(M.msg + "All items in " + (different ? M.elm + target.getName() + M.msg + "'s" : "your") + " inventory have been repaired.");
				}else{
					sender.sendMessage(M.error("No items in " + (different ? M.elm + target.getName() + M.err + "'s" : "your") + " inventory could be repaired."));
				}
				break;
			}
		}
	}
	
	private enum Slots {
		ALL, HAND, ARMOR, OFFHAND
	}
	
}
