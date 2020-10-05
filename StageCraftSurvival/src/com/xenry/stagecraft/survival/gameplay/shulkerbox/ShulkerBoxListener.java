package com.xenry.stagecraft.survival.gameplay.shulkerbox;

import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 8/3/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ShulkerBoxListener extends Handler<Survival,GameplayManager> {
	
	private final ShulkerBoxHandler handler;
	
	public ShulkerBoxListener(ShulkerBoxHandler handler){
		super(handler.getManager());
		this.handler = handler;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if(event.getAction() != Action.RIGHT_CLICK_AIR
				|| !ShulkerBoxHandler.CAN_OPEN_WITHOUT_PLACING.has(player)
				|| player.getInventory().getItemInOffHand().equals(event.getItem())
				|| !(itemInHand.getItemMeta() instanceof BlockStateMeta)){
			return;
		}
		BlockStateMeta meta = (BlockStateMeta)event.getItem().getItemMeta();
		if(meta == null || !(meta.getBlockState() instanceof ShulkerBox)){
			return;
		}
		
		if ((event.getAction().equals(Action.RIGHT_CLICK_AIR)) && handler.isShulkerBox(event.getItem())) {
			event.setCancelled(true);
			if(!handler.getCooldown().use(player)){
				return;
			}
			int slot = player.getInventory().getHeldItemSlot();
			if (player.getOpenInventory().getTopInventory().getType() == InventoryType.SHULKER_BOX) {
				player.closeInventory();
				return;
			}
			handler.openShulkerBox(player, itemInHand, slot);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void rightClickInventory(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		ItemStack currentItem = event.getCurrentItem();
		if(!event.getClick().equals(ClickType.RIGHT)
				|| event.getClickedInventory() == null
				|| currentItem == null
				|| !handler.isShulkerBox(currentItem)
				|| !ShulkerBoxHandler.CAN_OPEN_WITHOUT_PLACING.has(player)) {
			return;
		}
		
		boolean isInventoryShulker = handler.doesOpenInventoryMatchBoxInHand(player);
		boolean isPerformingSwitch = isInventoryShulker && handler.isShulkerBox(currentItem);
		InventoryType type = event.getClickedInventory().getType();
		if(!(type == InventoryType.PLAYER || type == InventoryType.CRAFTING || isInventoryShulker
				|| type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST)) {
			return;
		}
		
		int slot = event.getSlot();
		if(type.equals(InventoryType.CHEST) || type.equals(InventoryType.ENDER_CHEST)){
			ItemStack it = event.getCurrentItem();
			ArrayList<Integer> freeSlots = new ArrayList<>();
			for(int i = 0; i < 36; i++) {
				if (player.getInventory().getItem(i) == null) {
					freeSlots.add(i);
				}
			}
			if(freeSlots.size() == 0) {
				event.setCancelled(true);
				return;
			}
			//e.getInventory().remove(it);
			int selectedSlot = -1;
			for(int i = 0; i < event.getInventory().getSize(); i++) {
				ItemStack slot_stack = event.getInventory().getItem(i);
				if (slot_stack != null && slot_stack.equals(it)) {
					selectedSlot = i;
					break;
				}
			}
			if(selectedSlot == -1) {
				return;
			}
			event.getInventory().setItem(selectedSlot, new ItemStack(Material.AIR));
			player.getInventory().setItem(freeSlots.get(0), it);
			slot = freeSlots.get(0);
			//TODO
			
		}
		event.setCancelled(true);
		if(!handler.getCooldown().use(player)){
			return;
		}
		
		if (isPerformingSwitch) {
			//Close inventory without performing unswap
			handler.getSwap().remove(player.getName());
			handler.closeShulkerBox(player, player.getInventory().getItemInMainHand(), event.getInventory());
			
		}
		handler.shulkerSwap(player, slot);
		handler.openShulkerBox(player, player.getInventory().getItemInMainHand(),
				player.getInventory().getHeldItemSlot());
	}
	
	/*@EventHandler
	public void onInvClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		
		// This prevents duping when a player closes a (block) shulker box while holding one with the same
		// color and name.
		if (event.getInventory().getHolder() != null && !event.getInventory().getHolder().equals(player)) {
			return;
		}
		
		String nowinvname = event.getView().getTitle();
		if (handler.isShulkerBox(player.getInventory().getItemInMainHand())) {
			String holdingitemname = "";
			ItemStack iteminmainhand = event.getPlayer().getInventory().getItemInMainHand();
			if (iteminmainhand.getItemMeta() != null && iteminmainhand.getItemMeta().hasDisplayName()) {
				holdingitemname = iteminmainhand.getItemMeta().getDisplayName();
			}
			
			String checkname = cfgi.invname;
			if (holdingitemname.isEmpty()) {
				checkname = cfgi.invname.replace("%itemname%", holdingitemname);
				if (checkname.isEmpty()) {
					checkname = InventoryType.SHULKER_BOX.getDefaultTitle();
				}
			} else {
				checkname = checkname.replace("%itemname%", holdingitemname);
			}
			
			if (nowinvname.equalsIgnoreCase(checkname)) {
				shlkm.closeShulker(player, event.getPlayer().getInventory().getItemInMainHand(), event.getInventory());
				Material mat = Material.AIR;
				try {
					mat = player.getInventory().getItem(shlkm.swap.get(player.getName())).getType();
				} catch (NullPointerException npe) {
					//Do nothing about it
				}
				if (!mat.toString().contains("SHULKER_BOX")) {
					shlkm.shulkerUnswap(player);
				}
				
			}
		}
	}*/
	
}
