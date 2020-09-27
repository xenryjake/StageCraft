package com.xenry.stagecraft.ui.item;
/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class UIItem {
	
	protected UIItemStack item;
	
	public UIItem(UIItemStack item){
		this.item = item;
	}
	
	public UIItemStack getItemStack(){
		return item;
	}
	
	public void setItemStack(UIItemStack item){
		this.item=item;
	}
	
}
