package com.xenry.stagecraft.ui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ChestOpener implements Opener {
	
	public final UIManager manager;
	
	public ChestOpener(UIManager manager) {
		this.manager = manager;
	}
	
	@Override
	public Inventory open(Menu menu, Player player) {
		if(!supports(menu.type)){
			throw new IllegalArgumentException("illegal menu type: " + menu.type.name());
		}
		if(menu.cols != 9){
			throw new IllegalArgumentException("cols must be 9");
		}
		if(menu.rows < 1 || menu.rows > 6){
			throw new IllegalArgumentException("rows must be 1-6");
		}
		
		Inventory inv = Bukkit.createInventory(player, menu.rows * menu.cols, menu.title);
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
		return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
	}
	
}
