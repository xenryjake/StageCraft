package com.xenry.stagecraft.ui.page;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.ui.item.UIItem;
import com.xenry.stagecraft.ui.item.UIItemStack;
import com.xenry.stagecraft.ui.item.button.UIButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class UIPage {
	
	protected final String name;
	protected final HashMap<Integer,UIItem> items;
	protected final int rows;
	private final boolean setupOnOpen;
	
	public UIPage(String name, int rows, boolean setupOnOpen){
		this(name, rows, setupOnOpen, new HashMap<>());
	}
	
	public UIPage(String name, int rows, boolean setupOnOpen, HashMap<Integer,UIItem> items){
		this.name = name;
		this.rows = rows > 6 ? 6 : Math.max(rows, 1);
		this.setupOnOpen = setupOnOpen;
		this.items = items;
		
		if(!setupOnOpen) setup();
	}
	
	public String getName(){
		return name;
	}
	
	public HashMap<Integer,UIItem> getItems() {
		return items;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void addItem(int slot, UIItemStack is){
		addItem(slot, new UIItem(is));
	}
	
	public void addItem(int slot, UIItem i){
		items.put(slot, i);
	}
	
	public void removeItem(int slot){
		items.remove(slot);
	}
	
	public void removeItem(UIItem item){
		items.entrySet().removeIf(entry -> item == entry.getValue());
	}
	
	public void clearItems(){
		items.clear();
	}
	
	public Inventory construct(){
		Inventory inv = Bukkit.createInventory(null, rows*9, name);
		for(Map.Entry<Integer,UIItem> entry : items.entrySet())
			inv.setItem(entry.getKey(), entry.getValue().getItemStack());
		return inv;
	}
	
	public void open(Player p){
		if(setupOnOpen){
			clearItems();
			setup();
		}
		p.openInventory(construct());
	}
	
	public void open(Profile p){
		open(p.getPlayer());
	}
	
	public void setup(){}
	
	public List<UIButton> getButtons() {
		List<UIButton> buttons = new ArrayList<>();
		for(UIItem i : items.values()) {
			if(i instanceof UIButton) {
				buttons.add((UIButton)i);
			}
		}
		return buttons;
	}
	
}
