package com.xenry.stagecraft.ui.item.button;
import com.xenry.stagecraft.ui.item.UIItemStack;
import com.xenry.stagecraft.ui.page.UIPage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class OpenPageButton extends UIButton {
	
	private final UIPage page;
	
	public OpenPageButton(UIItemStack stack, UIPage page){
		super(stack);
		this.page = page;
	}
	
	public void click(Player p, ClickType type){
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
		page.open(p);
	}
	
	public UIPage getPage(){
		return page;
	}

}
