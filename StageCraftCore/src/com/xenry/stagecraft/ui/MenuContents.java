package com.xenry.stagecraft.ui;
import com.xenry.stagecraft.ui.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class MenuContents {
	
	private final Menu<?,?> menu;
	private final Player player;
	private final Item[][] contents;
	private final Pagination pagination;
	private final Map<String,SlotIterator> iterators;
	private final Map<String,Object> properties;
	
	public MenuContents(@NotNull Menu<?,?> menu, @NotNull Player player) {
		this.menu = menu;
		this.player = player;
		contents = new Item[menu.rows][menu.cols];
		pagination = new Pagination();
		iterators = new HashMap<>();
		properties = new HashMap<>();
	}
	
	@NotNull
	public Menu<?,?> menu(){
		return menu;
	}
	
	@NotNull
	public Pagination pagination(){
		return pagination;
	}
	
	@Nullable
	public SlotIterator iterator(String id){
		return iterators.getOrDefault(id, null);
	}
	
	@NotNull
	public SlotIterator addIterator(String id, @NotNull SlotIterator iterator){
		iterators.put(id, iterator);
		return iterator;
	}
	
	public Item[][] all(){
		return contents;
	}
	
	@Nullable
	public Slot firstEmpty(){
		for(int row = 0; row < contents.length; row++){
			for(int col = 0; col < contents[row].length; col++){
				if(get(row, col) == null){
					return new Slot(row, col);
				}
			}
		}
		return null;
	}
	
	@Nullable
	public Item get(int row, int col){
		if(row < 0 || col < 0){
			throw new IllegalArgumentException("row & col must not be negative");
		}
		if(row >= contents.length || col >= contents[row].length){
			return null;
		}
		return contents[row][col];
	}
	
	@Nullable
	public Item get(Slot slot){
		return get(slot.row, slot.col);
	}
	
	@NotNull
	public MenuContents set(int row, int col, @Nullable Item item){
		if(row < 0 || col < 0){
			throw new IllegalArgumentException("row & col must not be negative");
		}
		if(row < contents.length && col < contents[row].length){
			contents[row][col] = item;
			update(row, col, item != null ? item.getItemStack() : null);
		}
		return this;
	}
	
	@NotNull
	public MenuContents set(Slot slot, @Nullable Item item){
		return set(slot.row, slot.col, item);
	}
	
	@NotNull
	public MenuContents add(Item item){
		Slot slot = firstEmpty();
		if(slot != null){
			set(slot.row, slot.col, item);
		}
		return this;
	}
	
	@NotNull
	public MenuContents clear(){
		return fill(null);
	}
	
	@NotNull
	public MenuContents fill(@Nullable Item item){
		for(int row = 0; row < contents.length; row++){
			for(int col = 0; col < contents[row].length; col++){
				set(row, col, item);
			}
		}
		return this;
	}
	
	@NotNull
	public MenuContents fillRow(int row, @Nullable Item item){
		if(row < 0){
			throw new IllegalArgumentException("row must not be negative");
		}
		if(row < contents.length){
			for(int col = 0; col < contents[row].length; col++){
				set(row, col, item);
			}
		}
		return this;
	}
	
	@NotNull
	public MenuContents fillCol(int col, @Nullable Item item){
		if(col < 0){
			throw new IllegalArgumentException("col must not be negative");
		}
		for(int row = 0; row < contents.length; col++){
			set(row, col, item);
		}
		return this;
	}
	
	@NotNull
	public MenuContents fillRect(int fromRow, int fromCol, int toRow, int toCol, @Nullable Item item){
		if(fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0){
			throw new IllegalArgumentException("row & col must not be negative");
		}
		if(toRow < fromRow){
			int i = toRow;
			toRow = fromRow;
			fromRow = i;
		}
		if(toCol < fromCol){
			int i = toCol;
			toCol = fromCol;
			fromCol = i;
		}
		for(int row = fromRow; row <= toRow; row++){
			for(int col = fromCol; col <= toCol; col++){
				set(row, col, item);
			}
		}
		return this;
	}
	
	@NotNull
	public MenuContents fillRectHollow(int fromRow, int fromCol, int toRow, int toCol, @Nullable Item item){
		if(fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0){
			throw new IllegalArgumentException("row & col must not be negative");
		}
		if(toRow < fromRow){
			int i = toRow;
			toRow = fromRow;
			fromRow = i;
		}
		if(toCol < fromCol){
			int i = toCol;
			toCol = fromCol;
			fromCol = i;
		}
		for(int row = fromRow; row <= toRow; row++){
			for(int col = fromCol; col <= toCol; col++){
				if(row == fromRow || row == toRow || col == fromCol || col == toCol){
					set(row, col, item);
				}
			}
		}
		return this;
	}
	
	@NotNull
	public MenuContents fillBorders(@Nullable Item item){
		return fillRectHollow(0, 0, menu.rows - 1, menu.cols - 1, item);
	}
	
	@NotNull
	public MenuContents fillEmptySlots(@Nullable Item item){
		Slot slot = firstEmpty();
		while(slot != null){
			set(slot, item);
			slot = firstEmpty();
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public <T> T getProperty(String key){
		return (T) properties.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key, T defaultValue){
		return properties.containsKey(key) ? (T) properties.get(key) : defaultValue;
	}
	
	@NotNull
	public MenuContents setProperty(String key, Object value){
		properties.put(key, value);
		return this;
	}
	
	private void update(int row, int col, @Nullable ItemStack stack){
		if(menu.uiManager.getOpenedPlayers(menu).contains(player)){
			Inventory inv = player.getOpenInventory().getTopInventory();
			inv.setItem(menu.cols * row + col, stack);
		}
	}
	
}
