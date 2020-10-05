package com.xenry.stagecraft.survival.economy;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.PlayerUtil;
import com.xenry.stagecraft.survival.Survival;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EconomyManager extends Manager<Survival> {
	
	public EconomyManager(Survival plugin){
		super("Economy", plugin);
	}
	
	@Override
	protected void onEnable() {
		registerCommand(new DiamondsCommand(this));
	}
	
	public int getAvailableDiamonds(GenericProfile profile){
		return getAvailableDiamonds(profile.getPlayer());
	}
	
	public int getAvailableDiamonds(Player player){
		int i = 0;
		for(ItemStack is : player.getInventory()){
			if(is == null){
				continue;
			}
			if(is.getType() == Material.DIAMOND){
				i += is.getAmount();
			}
		}
		for(ItemStack is : player.getEnderChest()){
			if(is == null){
				continue;
			}
			if(is.getType() == Material.DIAMOND){
				i += is.getAmount();
			}
		}
		return i;
	}
	
	public boolean removeDiamondsFromInventory(Player player, int amount){
		if(amount > getAvailableDiamonds(player) || amount < 1){
			return false;
		}
		int remaining = amount;
		for(ItemStack is : player.getInventory()){
			if(is == null){
				continue;
			}
			if(remaining <= 0){
				break;
			}
			if(is.getType() != Material.DIAMOND){
				continue;
			}
			int thisStackSize = is.getAmount();
			if(thisStackSize <= remaining){
				remaining -= thisStackSize;
				is.setAmount(0);
			}else{
				is.setAmount(thisStackSize - remaining);
				remaining = 0;
				break;
			}
		}
		for(ItemStack is : player.getEnderChest()){
			if(is == null){
				continue;
			}
			if(remaining <= 0){
				break;
			}
			if(is.getType() != Material.DIAMOND){
				continue;
			}
			int thisStackSize = is.getAmount();
			if(thisStackSize <= remaining){
				remaining -= thisStackSize;
				is.setAmount(0);
			}else{
				is.setAmount(thisStackSize - remaining);
				remaining = 0;
				break;
			}
		}
		if(remaining > 0){
			Log.severe("Failed to remove " + remaining + " diamonds from " + player.getName() + "'s inventory."
					+ "Player is lacking " + (amount - remaining) + " diamonds.");
			return false;
		}
		return true;
	}
	
	public int addDiamondsToInventory(Player player, int amount){
		if(amount < 0){
			throw new IllegalArgumentException("amount must not be negative");
		}
		int stacks = (amount+63)/64;
		int lastStackSize = amount % 64;
		if(lastStackSize == 0){
			lastStackSize = 64;
		}
		int remaining = amount;
		for(int i = 0; i < stacks; i++){
			int n = i == stacks - 1 ? lastStackSize : 64;
			if(!PlayerUtil.hasSpaceForItemStack(player, new ItemStack(Material.DIAMOND, n))){
				return remaining;
			}
			player.getInventory().addItem(new ItemStack(Material.DIAMOND, n));
			remaining -= n;
		}
		return remaining;
	}
	
}
