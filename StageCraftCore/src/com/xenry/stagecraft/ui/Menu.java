package com.xenry.stagecraft.ui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class Menu {
	
	public final UIManager manager;
	public final String id, title;
	public final InventoryType type;
	public final int rows, cols;
	public final Menu parent;
	
	private boolean closable;
	
	public Menu(UIManager manager, String id, String title, InventoryType type, int rows, int cols, Menu parent) {
		this.manager = manager;
		this.id = id;
		this.title = title;
		this.type = type;
		this.rows = rows;
		this.cols = cols;
		this.parent = parent;
	}
	
	public Menu(UIManager manager, String id, String title, InventoryType type, int rows, int cols, Menu parent,
				boolean closable) {
		this(manager, id, title, type, rows, cols, parent);
		this.closable = closable;
	}
	
	public boolean isClosable() {
		return closable;
	}
	
	public void setClosable(boolean closable) {
		this.closable = closable;
	}
	
	public Inventory open(Player player){
		return open(player, 0);
	}
	
	public Inventory open(Player player, int page){
		Menu oldMenu = manager.getOpenMenu(player);
		if(oldMenu != null){
			oldMenu.close(player);
		}
		MenuContents contents = new MenuContents(this, player);
		contents.pagination().page(page);
		manager.setOpenContents(player, contents);
		init(player, contents);
		Opener opener = manager.getOpener(type);
		if(opener == null){
			throw new IllegalStateException("No opener for type " + type.name());
		}
		Inventory inv = opener.open(this, player);
		manager.setOpenMenu(player, this);
		return inv;
	}
	
	public void close(Player player){
		manager.setOpenMenu(player, null);
		player.closeInventory();
		manager.setOpenContents(player, null);
	}
	
	public abstract void init(Player player, MenuContents contents);
	
	public void tick(Player player, MenuContents contents){}
	
	public void onClick(InventoryClickEvent event){}
	
	public void onDrag(InventoryDragEvent event){}
	
	public void onOpen(InventoryOpenEvent event){}
	
	public void onClose(InventoryCloseEvent event){}
	
	public void onQuit(PlayerQuitEvent event){}
	
}
