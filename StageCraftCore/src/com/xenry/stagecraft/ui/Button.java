package com.xenry.stagecraft.ui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class Button extends Item {
	
	public Button(ItemStack itemStack) {
		super(itemStack);
	}
	
	public abstract void onClick(InventoryClickEvent event);
	
}
