package com.xenry.stagecraft.survival.gameplay.enchantment.autosmelt;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.FakeBlockBreakEvent;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.util.ItemUtil;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment.ORE_SMELTING;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/4/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class OreSmeltingHandler extends Handler<Survival,GameplayManager> {
	
	public OreSmeltingHandler(GameplayManager manager) {
		super(manager);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event){
		if(event instanceof FakeBlockBreakEvent){
			return;
		}
		Player player = event.getPlayer();
		if(player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE){
			return;
		}
		
		ItemStack drop = getDrops(event.getBlock());
		if(drop == null){
			return;
		}
		
		ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
		if(!hasAutoSmeltingEnchantment(itemInHand)){
			return;
		}
		
		if(itemInHand.getEnchantmentLevel(CustomEnchantment.TELEKINESIS) > 0 && PlayerUtil.hasSpaceForItemStack(player, drop)){
			player.getInventory().addItem(drop);
		}else{
			Location location = event.getBlock().getLocation();
			if(location.getWorld() == null){
				return;
			}
			location.getWorld().dropItemNaturally(location.clone().add(0.5, 1.2, 0.5), drop);
		}
		event.setCancelled(true);
		player.giveExp(getExp(drop));
		event.getBlock().setType(Material.AIR);
		ItemUtil.damageItem(itemInHand, 1);
	}
	
	public ItemStack getDrops(Block block){
		switch(block.getType()){
			case IRON_ORE:
				return new ItemStack(Material.IRON_INGOT, 1);
			case GOLD_ORE:
				return new ItemStack(Material.GOLD_INGOT, 1);
			case ANCIENT_DEBRIS:
				return new ItemStack(Material.NETHERITE_SCRAP, 1);
			default:
				return null;
		}
	}
	
	public int getExp(ItemStack item){
		switch(item.getType()){
			case IRON_INGOT:
				return getCore().getRandom().nextFloat() > 0.7 ? 0 : 1;
			case GOLD_INGOT:
				return 1;
			case NETHERITE_SCRAP:
				return 2;
			default:
				return 0;
		}
	}
	
	public boolean hasAutoSmeltingEnchantment(ItemStack item){
		if(item == null || item.getItemMeta() == null){
			return false;
		}
		return item.getItemMeta().getEnchantLevel(ORE_SMELTING) > 0;
	}
	
}
