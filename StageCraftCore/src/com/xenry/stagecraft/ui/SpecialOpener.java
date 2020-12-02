package com.xenry.stagecraft.ui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SpecialOpener implements Opener {
	
	private static final List<InventoryType> types = Arrays.asList(InventoryType.FURNACE, InventoryType.WORKBENCH,
			InventoryType.DISPENSER, InventoryType.DROPPER, InventoryType.ENCHANTING, InventoryType.BREWING,
			InventoryType.ANVIL, InventoryType.BEACON, InventoryType.HOPPER);
	
	public final UIManager manager;
	
	public SpecialOpener(UIManager manager) {
		this.manager = manager;
	}
	
	@Override
	public Inventory open(Menu menu, Player player) {
		if(!supports(menu.type)){
			throw new IllegalArgumentException("illegal menu type: " + menu.type.name());
		}
		Inventory inv = Bukkit.createInventory(player, menu.type, menu.title);
		MenuContents contents = manager.getOpenContents(player);
		if(contents == null){
			throw new IllegalStateException("there are no contents?");
		}
		fill(inv, contents);
		player.openInventory(inv);
		return inv;
	}
	
	@Override
	public boolean supports(InventoryType type) {
		return types.contains(type);
	}
	
}
