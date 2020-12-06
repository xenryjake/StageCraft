package com.xenry.stagecraft.ui;
import com.xenry.stagecraft.ui.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public interface Opener {
	
	Inventory open(Menu<?,?> menu, Player player);
	
	boolean supports(InventoryType type);
	
	default void fill(Inventory inventory, MenuContents contents){
		Item[][] items = contents.all();
		
		for(int row = 0; row < items.length; row++){
			for(int col = 0; col < items[row].length; col++){
				if(items[row][col] != null){
					inventory.setItem(9 * row + col, items[row][col].getItemStack());
				}
			}
		}
	}
	
}
