package com.xenry.stagecraft.ui.item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Button extends Item {
	
	private final Click click;
	
	public Button(ItemStack itemStack, Click click) {
		super(itemStack);
		this.click = click;
	}
	
	public void onClick(InventoryClickEvent event){
		click.click(event);
	}
	
	public static Button runCommandButton(ItemStack itemStack, String command){
		return runCommandButton(itemStack, command, true);
	}
	
	public static Button runCommandButton(ItemStack itemStack, String command, boolean closeInventory){
		return new Button(itemStack, (event) -> {
			if(closeInventory){
				event.getWhoClicked().closeInventory();
			}
			((Player)event.getWhoClicked()).performCommand(command);
		});
	}
	
	public static Button closeInventoryButton(ItemStack itemStack){
		return new Button(itemStack, (event) -> event.getWhoClicked().closeInventory());
	}
	
	public interface Click {
		
		void click(InventoryClickEvent event);
		
	}
	
}
