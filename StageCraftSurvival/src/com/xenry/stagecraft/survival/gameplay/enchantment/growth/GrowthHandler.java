package com.xenry.stagecraft.survival.gameplay.enchantment.growth;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GrowthHandler extends Handler<Survival,GameplayManager> {
	
	public static final String modifierName = "growth";
	public static final int maxBoost = 40;
	
	public GrowthHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		//clearAllModifiers(event.getPlayer());
		updateGrowth(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		clearGrowthModifiers(event.getPlayer());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getItem() == null){
			return;
		}
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.GROWTH) > 0){
			Bukkit.getScheduler().runTask(manager.plugin, () -> updateGrowth(event.getPlayer()));
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		Bukkit.getScheduler().runTask(manager.plugin, () -> updateGrowth(event.getWhoClicked()));
	}
	
	@EventHandler
	public void onDamage(PlayerItemDamageEvent event){
		if(event.getItem().getEnchantmentLevel(CustomEnchantment.GROWTH) > 0){
			updateGrowth(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onBreak(PlayerItemBreakEvent event){
		if(event.getBrokenItem().getEnchantmentLevel(CustomEnchantment.GROWTH) > 0){
			updateGrowth(event.getPlayer());
		}
	}
	
	public void updateGrowth(HumanEntity player){
		AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if(attr == null){
			Log.severe("Could not access health attribute for player " + player.getName());
			return;
		}
		
		double totalBoost = 0;
		for(ItemStack item : player.getInventory().getArmorContents()){
			if(item == null || item.getAmount() < 1){
				continue;
			}
			totalBoost += getHealthBoost(item);
		}
		if(totalBoost < 0){
			totalBoost = 0;
		}
		if(totalBoost > maxBoost){
			totalBoost = maxBoost;
		}
		if(totalBoost == getModifierAmount(attr)) {
			return;
		}
		boolean fullHealth = player.getHealth() == attr.getValue();
		
		clearGrowthModifiers(attr);
		if(totalBoost > 0){
			attr.addModifier(new AttributeModifier(modifierName, totalBoost, AttributeModifier.Operation.ADD_NUMBER));
		}
		
		if(fullHealth){
			player.setHealth(attr.getValue());
		}
		
	}
	
	public void clearGrowthModifiers(HumanEntity player){
		AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if(attr == null){
			Log.severe("Could not access health attribute for player " + player.getName());
			return;
		}
		clearGrowthModifiers(attr);
	}
	
	public void clearGrowthModifiers(AttributeInstance attr){
		ArrayList<AttributeModifier> modifiersToRemove = new ArrayList<>();
		for(AttributeModifier mod : attr.getModifiers()){
			if(mod.getName().equals(modifierName)){
				modifiersToRemove.add(mod);
			}
		}
		for(AttributeModifier mod : modifiersToRemove){
			attr.removeModifier(mod);
		}
	}
	
	public void clearAllModifiers(HumanEntity player){
		AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if(attr == null){
			Log.severe("Could not access health attribute for player " + player.getName());
			return;
		}
		clearAllModifiers(attr);
	}
	
	public void clearAllModifiers(AttributeInstance attr){
		for(AttributeModifier mod : new ArrayList<>(attr.getModifiers())){
			attr.removeModifier(mod);
		}
	}
	
	public double getModifierAmount(HumanEntity player){
		AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if(attr == null){
			Log.severe("Could not access health attribute for player " + player.getName());
			return 0;
		}
		return getModifierAmount(attr);
	}
	
	public double getModifierAmount(AttributeInstance attr){
		AttributeModifier mod = null;
		for(AttributeModifier m : attr.getModifiers()){
			if(m.getName().equals(modifierName)){
				mod = m;
				break;
			}
		}
		if(mod == null){
			return 0;
		}
		return mod.getAmount();
	}
	
	public double getHealthBoost(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return 0;
		}
		return getHealthBoost(item.getEnchantmentLevel(CustomEnchantment.GROWTH));
	}
	
	public double getHealthBoost(int level){
		return level < 1 ? 0 : level;
	}
	
}
