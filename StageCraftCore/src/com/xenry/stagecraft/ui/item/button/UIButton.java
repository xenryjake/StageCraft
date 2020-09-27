package com.xenry.stagecraft.ui.item.button;
import com.xenry.stagecraft.ui.item.UIItem;
import com.xenry.stagecraft.ui.item.UIItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class UIButton extends UIItem {
	
	public UIButton(UIItemStack item){
		super(item);
	}
	
	public void setItem(UIItemStack stack){
		this.item = stack;
	}
	
	public abstract void click(Player p, ClickType type);
	
}
