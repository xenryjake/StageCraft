package com.xenry.stagecraft.survival.gameplay.shulkerbox;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 8/3/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ShulkerBoxHandler extends Handler<Survival,GameplayManager> {
	
	public static final Access CAN_OPEN_WITHOUT_PLACING = Rank.MEMBER;
	
	private final HashMap<String, Integer> swap;
	private final Cooldown cooldown;
	private final ShulkerBoxListener listener;
	
	public ShulkerBoxHandler(GameplayManager manager) {
		super(manager);
		swap = new HashMap<>();
		cooldown = new Cooldown(1000, M.err + "Please wait before doing that again.");
		listener = new ShulkerBoxListener(this);
	}
	
	public boolean isShulkerBox(ItemStack item){
		if(!(item.getItemMeta() instanceof BlockStateMeta)) {
			return false;
		}
		BlockStateMeta im = (BlockStateMeta)item.getItemMeta();
		if(!(im.getBlockState() instanceof ShulkerBox)) {
			return false;
		}
		return item.toString().contains("SHULKER_BOX");
	}
	
	public boolean doesOpenInventoryMatchBoxInHand(Player player){
		InventoryView inventory = player.getOpenInventory();
		if(inventory.getType() != InventoryType.SHULKER_BOX) {
			return false;
		}
		ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
		if(meta == null){
			return false;
		}
		return !meta.hasDisplayName() || inventory.getTitle().equals(meta.getDisplayName());
	}
	
	public void shulkerSwap(Player player, int slot) {
		ItemStack shulker = player.getInventory().getItem(slot);
		ItemStack originalmh = player.getInventory().getItemInMainHand();
		swap.put(player.getName(), slot);
		player.getInventory().setItem(slot, originalmh);
		player.getInventory().setItem(player.getInventory().getHeldItemSlot(), shulker);
	}
	
	public void shulkerUnswap(Player player) {
		if (!swap.containsKey(player.getName())) {
			return;
		}
		ItemStack shulker = player.getInventory().getItemInMainHand();
		ItemStack tomain = player.getInventory().getItem(swap.get(player.getName()));
		player.getInventory().setItem(player.getInventory().getHeldItemSlot(), tomain);
		player.getInventory().setItem(swap.get(player.getName()), shulker);
		swap.remove(player.getName());
	}
	
	public void closeShulkerBox(Player player, ItemStack shulker, Inventory inventory) {
		ItemStack itemStack = shulker.clone();
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		if(meta == null){
			return;
		}
		ShulkerBox box = (ShulkerBox) meta.getBlockState();
		box.getInventory().setContents(inventory.getContents());
		meta.setBlockState(box);
		box.update();
		itemStack.setItemMeta(meta);
		player.getInventory().setItem(player.getInventory().getHeldItemSlot(), itemStack);
		
		player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1.0F, 1.0F);
	}
	
	public void openShulkerBox(Player player, ItemStack shulker, int slot) {
		BlockStateMeta meta = (BlockStateMeta) shulker.getItemMeta();
		if(meta == null){
			return;
		}
		ShulkerBox box = (ShulkerBox) meta.getBlockState();
		Inventory inventory = Bukkit.createInventory(player, InventoryType.SHULKER_BOX,
				meta.hasDisplayName() ? meta.getDisplayName() : InventoryType.SHULKER_BOX.getDefaultTitle());
		inventory.setContents(box.getInventory().getContents());
		player.openInventory(inventory);
		player.getInventory().setHeldItemSlot(slot);
		
		player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F);
	}
	
	public HashMap<String,Integer> getSwap() {
		return swap;
	}
	
	public Cooldown getCooldown() {
		return cooldown;
	}
	
	public ShulkerBoxListener getListener() {
		return listener;
	}
	
}
