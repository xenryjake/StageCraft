package com.xenry.stagecraft.ui;
import com.google.common.collect.Lists;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.ui.item.button.UIButton;
import com.xenry.stagecraft.ui.page.UIPage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class UIClickHandler extends Handler<Core,UIManager> {
	
	public UIClickHandler(UIManager manager){
		super(manager);
	}
	
	@EventHandler
	public void on(InventoryClickEvent event){
		for(UIPage p : manager.getAllPages()){
			if(event.getView().getTitle().equalsIgnoreCase(p.getName())) {
				event.setCancelled(true);
				if(event.getCurrentItem() == null) return;
				if(event.getCurrentItem().getItemMeta() == null) return;
				for(UIButton button : Lists.reverse(p.getButtons())) {
					if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(button.getItemStack().getName())) {
						button.click((Player)event.getWhoClicked(), event.getClick());
						break;
					}
				}
				break;
			}
		}
	}
	
}
