package com.xenry.stagecraft.survival.gameplay.enchantment.telekinesis;
import com.sun.istack.internal.NotNull;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.util.event.FakeBlockBreakEvent;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.ItemUtil;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/20/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TelekinesisHandler extends Handler<Survival,GameplayManager> {
	
	public TelekinesisHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockTelekinesis(BlockBreakEvent event){
		if(event instanceof FakeBlockBreakEvent){
			return;
		}
		Player player = event.getPlayer();
		if(player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE){
			return;
		}
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if(!hasTelekinesisEnchantment(itemInHand)){
			return;
		}
		Collection<ItemStack> drops = event.getBlock().getDrops(itemInHand);
		if(drops.size() < 1){
			return;
		}
		for(ItemStack item : drops){
			if(item == null){
				continue;
			}
			if(PlayerUtil.hasSpaceForItemStack(player, item)){
				player.getInventory().addItem(item);
			}else{
				Location location = player.getLocation();
				if(location.getWorld() == null){
					continue;
				}
				Item i = location.getWorld().dropItem(location.clone().add(0, 1.2, 0), item);
				i.setVelocity(new Vector());
			}
		}
		event.setCancelled(true);
		player.giveExp(event.getExpToDrop());
		event.getBlock().setType(Material.AIR);
		ItemUtil.damageItem(itemInHand, 1);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTelekinesis(EntityDeathEvent event){
		Player killer = event.getEntity().getKiller();
		if(killer == null){
			return;
		}
		ItemStack itemInHand = killer.getInventory().getItemInMainHand();
		if(!hasTelekinesisEnchantment(itemInHand)){
			return;
		}
		List<ItemStack> drops = new ArrayList<>(event.getDrops());
		if(drops.size() < 1){
			return;
		}
		for(ItemStack item : drops){
			if(item == null){
				continue;
			}
			if(PlayerUtil.hasSpaceForItemStack(killer, item)){
				killer.getInventory().addItem(item);
			}else{
				Location location = killer.getLocation();
				if(location.getWorld() == null){
					continue;
				}
				Item i = location.getWorld().dropItem(location.clone().add(0, 1.2, 0), item);
				i.setVelocity(new Vector());
			}
		}
		killer.giveExp(event.getDroppedExp());
		event.setDroppedExp(0);
		event.getDrops().clear();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onShearTelekinesis(PlayerShearEntityEvent event){
		Player player = event.getPlayer();
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if(!hasTelekinesisEnchantment(itemInHand)){
			return;
		}
		Entity entity = event.getEntity();
		if(!(entity instanceof Sheep)){
			return;
		}
		Sheep sheep = (Sheep) entity;
		Material wool = getWoolType(sheep);
		if(wool == null){
			return;
		}
		
		ItemStack drop = new ItemStack(wool, getCore().getRandom().nextInt(3) + 1);
		if(PlayerUtil.hasSpaceForItemStack(player, drop)){
			player.getInventory().addItem(drop);
		}else{
			Location location = player.getLocation();
			if(location.getWorld() == null){
				return;
			}
			Item i = location.getWorld().dropItem(location.clone().add(0, 1.2, 0), drop);
			i.setVelocity(new Vector());
		}
		event.setCancelled(true);
		sheep.setSheared(true);
		ItemUtil.damageItem(itemInHand, 1);
	}
	
	public Material getWoolType(@NotNull Sheep sheep){
		if(sheep.getColor() == null){
			return null;
		}
		switch(sheep.getColor()){
			case WHITE:
				return Material.WHITE_WOOL;
			case ORANGE:
				return Material.ORANGE_WOOL;
			case MAGENTA:
				return Material.MAGENTA_WOOL;
			case LIGHT_BLUE:
				return Material.LIGHT_BLUE_WOOL;
			case YELLOW:
				return Material.YELLOW_WOOL;
			case LIME:
				return Material.LIME_WOOL;
			case PINK:
				return Material.PINK_WOOL;
			case GRAY:
				return Material.GRAY_WOOL;
			case LIGHT_GRAY:
				return Material.LIGHT_GRAY_WOOL;
			case CYAN:
				return Material.CYAN_WOOL;
			case PURPLE:
				return Material.PURPLE_WOOL;
			case BLUE:
				return Material.BLUE_WOOL;
			case BROWN:
				return Material.BROWN_WOOL;
			case GREEN:
				return Material.GREEN_WOOL;
			case RED:
				return Material.RED_WOOL;
			case BLACK:
				return Material.BLACK_WOOL;
			default:
				return null;
		}
	}
	
	public boolean hasTelekinesisEnchantment(ItemStack item){
		if(item == null || item.getItemMeta() == null || item.getType() == Material.AIR){
			return false;
		}
		return item.getEnchantmentLevel(CustomEnchantment.TELEKINESIS) > 0;
	}
	
}
