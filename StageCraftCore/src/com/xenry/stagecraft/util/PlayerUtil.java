package com.xenry.stagecraft.util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/17/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerUtil {
	
	private PlayerUtil(){}
	
	public static List<String> getOnlinePlayerNames(){
		List<String> names = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers()){
			names.add(player.getName());
		}
		return names;
	}
	
	@Nullable
	public static Player getAnyPlayer(){
		ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		if(players.size() < 1){
			return null;
		}
		return players.get(0);
	}
	
	public static boolean arePlayersOnline(){
		return Bukkit.getOnlinePlayers().size() > 0;
	}
	
	@SuppressWarnings("ConstantConditions")
	public static boolean hasItemsInInventory(HumanEntity player){
		if(player == null){
			return false;
		}
		for(ItemStack item : player.getInventory().getContents()){
			if(item == null || item.getType() != Material.AIR){
				return true;
			}
		}
		return false;
	}
	
	public static void clearPotionEffects(HumanEntity player){
		if(player == null){
			return;
		}
		for(PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
	}
	
	public static boolean hasSpaceForItemStack(Player player, ItemStack itemStack){
		if(player == null){
			return false;
		}
		return hasSpaceForItemStack(player.getInventory(), itemStack);
	}
	
	public static boolean hasSpaceForItemStack(Inventory inventory, ItemStack itemStack){
		if(itemStack == null || inventory == null){
			return false;
		}
		if(inventory.firstEmpty() != -1){
			return true;
		}
		int stacks = 0;
		int items = 0;
		for(ItemStack item : inventory.getContents()){
			if(!itemStack.isSimilar(item)){ //handles item==null condition
				continue;
			}
			stacks++;
			items += item.getAmount();
		}
		return stacks * itemStack.getMaxStackSize() >= items + itemStack.getAmount();
	}
	
}
