package com.xenry.stagecraft.skyblock.island.playerstate;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerStateFile {
	
	public final String islandID;
	public final String uuid;
	private final File file;
	private final FileConfiguration config;
	
	public PlayerStateFile(IslandManager manager, String islandID, String uuid) throws Exception {
		this.islandID = islandID;
		this.uuid = uuid;
		String relativePath = islandID + File.separator + uuid + ".yml";
		file = new File(manager.plugin.getDataFolder(), relativePath);
		if(!file.exists()){
			file.getParentFile().mkdirs();
			manager.plugin.saveResource(relativePath, false);
		}
		config = new YamlConfiguration();
		config.load(file);
	}
	
	public boolean save(){
		try{
			config.save(file);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	public double getHealth(){
		return config.getDouble("health");
	}
	
	public void setHealth(double health){
		config.set("health", health);
	}
	
	public int getFoodLevel(){
		return config.getInt("food-level");
	}
	
	public void setFoodLevel(int foodLevel){
		config.set("food-level", foodLevel);
	}
	
	public float getSaturation(){
		return (float)config.getDouble("saturation");
	}
	
	public void setSaturation(float saturation){
		config.set("saturation", (double)saturation);
	}
	
	public float getExhaustion(){
		return (float)config.getDouble("exhaustion");
	}
	
	public void setExhaustion(float exhaustion){
		config.set("exhaustion", (double)exhaustion);
	}
	
	public int getLevel(){
		return config.getInt("level");
	}
	
	public void setLevel(int level){
		config.set("level", level);
	}
	
	public float getExp(){
		return (float)config.getDouble("exp");
	}
	
	public void setExp(float exp){
		config.set("exp", (double)exp);
	}
	
	@SuppressWarnings("unchecked")
	public List<PotionEffect> getPotionEffects(){
		return (List<PotionEffect>)config.getList("potion-effects");
	}
	
	public void setPotionEffects(Collection<PotionEffect> effects){
		config.set("potion-effects", new ArrayList<>(effects));
	}
	
	@SuppressWarnings("unchecked")
	public ItemStack[] getInventoryContents(){
		return ((List<ItemStack>)config.get("inventory-contents")).toArray(new ItemStack[]{});
	}
	
	public void setInventoryContents(ItemStack[] contents){
		config.set("inventory-contents", contents);
	}
	
	@SuppressWarnings("unchecked")
	public ItemStack[] getArmor(){
		return ((List<ItemStack>)config.get("armor")).toArray(new ItemStack[]{});
	}
	
	public void setArmor(ItemStack[] armor){
		config.set("armor", armor);
	}
	
	@SuppressWarnings("unchecked")
	public ItemStack[] getEnderChest(){
		return ((List<ItemStack>)config.get("ender-chest")).toArray(new ItemStack[]{});
	}
	
	public void setEnderChest(ItemStack[] enderChest){
		config.set("ender-chest", enderChest);
	}
	
	public PlayerState toPlayerState(){
		return new PlayerState(getHealth(), getFoodLevel(), getSaturation(), getExhaustion(), getLevel(), getExp(),
				getPotionEffects(), getInventoryContents(), getArmor(), getEnderChest());
	}
	
	public void set(PlayerState state){
		setHealth(state.getHealth());
		setFoodLevel(state.getFoodLevel());
		setSaturation(state.getSaturation());
		setExhaustion(state.getExhaustion());
		setLevel(state.getLevel());
		setExp(state.getExp());
		setPotionEffects(state.getEffects());
		setInventoryContents(state.getInventoryContents());
		setArmor(state.getInventoryArmor());
		setEnderChest(state.getEnderChest());
	}
	
}
