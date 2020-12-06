package com.xenry.stagecraft.ui;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.ui.item.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class Menu<P extends StageCraftPlugin,T extends Manager<P>> {
	
	protected final T manager;
	
	public final UIManager uiManager;
	public final String id;
	public final InventoryType type;
	public int rows, cols;
	public final Menu<P,T> parent;
	
	private String title;
	private boolean closable;
	
	public Menu(T manager, UIManager uiManager, String id, String title, InventoryType type, int rows, int cols, Menu<P,T> parent, boolean closable) {
		this.manager = manager;
		this.uiManager = uiManager;
		this.id = id;
		this.title = title;
		this.type = type;
		this.rows = rows;
		this.cols = cols;
		this.parent = parent;
		this.closable = closable;
	}
	
	public Menu(T manager, UIManager uiManager, String id, String title, InventoryType type, int rows, int cols, Menu<P,T> parent) {
		this(manager, uiManager, id, title, type, rows, cols, parent, true);
	}
	
	public Menu(T manager, String id, String title, int rows) {
		this(manager, manager.getCore().getUIManager(), id, title, InventoryType.CHEST, rows, 9, null, true);
	}
	
	public Menu(T manager, String id, String title, int rows, Menu<P,T> parent) {
		this(manager, manager.getCore().getUIManager(), id, title, InventoryType.CHEST, rows, 9, parent, true);
	}
	
	public boolean isClosable() {
		return closable;
	}
	
	public void setClosable(boolean closable) {
		this.closable = closable;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Inventory open(Player player){
		return open(player, 0);
	}
	
	public Inventory open(Player player, int page){
		Menu<?,?> oldMenu = uiManager.getOpenMenu(player);
		if(oldMenu != null){
			oldMenu.close(player);
		}
		MenuContents contents = new MenuContents(this, player);
		contents.pagination().page(page);
		uiManager.setOpenContents(player, contents);
		init(player, contents);
		Opener opener = uiManager.getOpener(type);
		if(opener == null){
			throw new IllegalStateException("No opener for type " + type.name());
		}
		Inventory inv = opener.open(this, player);
		uiManager.setOpenMenu(player, this);
		return inv;
	}
	
	public void close(Player player){
		uiManager.setOpenMenu(player, null);
		player.closeInventory();
		uiManager.setOpenContents(player, null);
	}
	
	protected abstract void init(Player player, MenuContents contents);
	
	protected void tick(Player player, MenuContents contents){}
	
	protected void onClick(InventoryClickEvent event){}
	
	protected void onDrag(InventoryDragEvent event){}
	
	protected void onOpen(InventoryOpenEvent event){}
	
	protected void onClose(InventoryCloseEvent event){}
	
	protected void onQuit(PlayerQuitEvent event){}
	
	protected Item getBackgroundItem(){
		ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§r");
		is.setItemMeta(im);
		return new Item(is);
	}
	
	protected Item getInvisibleItem(){
		ItemStack is = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§r");
		is.setItemMeta(im);
		return new Item(is);
	}
	
}
