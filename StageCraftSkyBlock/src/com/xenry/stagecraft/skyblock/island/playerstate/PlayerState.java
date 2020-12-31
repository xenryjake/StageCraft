package com.xenry.stagecraft.skyblock.island.playerstate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerState {
	
	private final double health;
	private final int foodLevel;
	private final float saturation;
	private final float exhaustion;
	private final int level;
	private final float exp;
	private final Collection<PotionEffect> effects;
	private final ItemStack[] inventoryContents;
	private final ItemStack[] inventoryArmor;
	private final ItemStack[] enderChest;
	
	public PlayerState(Player player) {
		health = player.getHealth();
		foodLevel = player.getFoodLevel();
		saturation = player.getSaturation();
		exhaustion = player.getExhaustion();
		level = player.getLevel();
		exp = player.getExp();
		effects = new ArrayList<>(player.getActivePotionEffects());
		inventoryContents = player.getInventory().getContents();
		inventoryArmor = player.getInventory().getArmorContents();
		enderChest = player.getEnderChest().getContents();
	}
	
	public PlayerState(double health, int foodLevel, float saturation, float exhaustion, int level, float exp,
					   Collection<PotionEffect> effects, ItemStack[] inventoryContents, ItemStack[] inventoryArmor,
					   ItemStack[] enderChest) {
		this.health = health;
		this.foodLevel = foodLevel;
		this.saturation = saturation;
		this.exhaustion = exhaustion;
		this.level = level;
		this.exp = exp;
		this.effects = effects;
		this.inventoryContents = inventoryContents;
		this.inventoryArmor = inventoryArmor;
		this.enderChest = enderChest;
	}
	
	//apply potion effects and set inventory before setting numerical values
	// eg. saved health might be greater than 20, but is given from a potion effect
	public void apply(Player player){
		for(PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		for(PotionEffect effect : effects){
			player.addPotionEffect(effect);
		}
		player.getInventory().setContents(inventoryContents);
		player.getInventory().setArmorContents(inventoryArmor);
		player.getEnderChest().setContents(enderChest);
		player.setHealth(health);
		player.setFoodLevel(foodLevel);
		player.setSaturation(saturation);
		player.setExhaustion(exhaustion);
		player.setLevel(level);
		player.setExp(exp);
	}
	
	public static PlayerState fromFile(PlayerStateFile file){
		return file.toPlayerState();
	}
	
	public double getHealth() {
		return health;
	}
	
	public int getFoodLevel() {
		return foodLevel;
	}
	
	public float getSaturation() {
		return saturation;
	}
	
	public float getExhaustion() {
		return exhaustion;
	}
	
	public int getLevel() {
		return level;
	}
	
	public float getExp() {
		return exp;
	}
	
	public Collection<PotionEffect> getEffects() {
		return effects;
	}
	
	public ItemStack[] getInventoryContents() {
		return inventoryContents;
	}
	
	public ItemStack[] getInventoryArmor() {
		return inventoryArmor;
	}
	
	public ItemStack[] getEnderChest() {
		return enderChest;
	}
	
}
