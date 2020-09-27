package com.xenry.stagecraft.ui.item.button;
import com.xenry.stagecraft.ui.item.UIItemStack;
import com.xenry.stagecraft.util.M;
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
public class ErrorButton extends UIButton {
	
	private String message;
	
	public ErrorButton(UIItemStack itemStack, String message){
		super(itemStack);
		this.message = message;
	}
	
	@Override
	public void click(Player p, ClickType type) {
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
		p.sendMessage(M.err + message);
		p.closeInventory();
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
