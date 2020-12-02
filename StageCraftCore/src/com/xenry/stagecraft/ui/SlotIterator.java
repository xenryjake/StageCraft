package com.xenry.stagecraft.ui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SlotIterator {
	
	private final Menu menu;
	private final MenuContents contents;
	private final Type type;
	
	private boolean started = false;
	private boolean allowOverride = true;
	private int row, col;
	
	private Set<Slot> blacklist;
	
	public SlotIterator(Menu menu, MenuContents contents, Type type, int row, int col) {
		if(row < 0 || col < 0){
			throw new IllegalArgumentException("row & col must not be negative");
		}
		this.menu = menu;
		this.contents = contents;
		this.type = type;
		this.row = row;
		this.col = col;
	}
	
	public SlotIterator(Menu menu, MenuContents contents, Type type) {
		this(menu, contents, type, 0, 0);
	}
	
	@Nullable
	public Item get(){
		return contents.get(row, col);
	}
	
	@NotNull
	public SlotIterator set(@Nullable Item item){
		if(canPlace()){
			contents.set(row, col, item);
		}
		return this;
	}
	
	@NotNull
	public SlotIterator next(){
		if(ended()){
			this.started = true;
			return this;
		}
		do{
			if(!this.started){
				this.started = true;
			}else{
				switch(type){
					case HORIZONTAL:
						col = ++col % menu.cols;
						if(col == 0){
							row++;
						}
						break;
					case VERTICAL:
						row = ++row % menu.rows;
						if(row == 0){
							col++;
						}
						break;
				}
			}
		}while(!canPlace() && !ended());
		return this;
	}
	
	@NotNull
	public SlotIterator previous(){
		if(row <= 0 && col <= 0){
			this.started = true;
			return this;
		}
		do {
			if(!this.started) {
				this.started = true;
			}else{
				switch(type){
					case HORIZONTAL:
						col--;
						if(col <= 0){
							col = menu.cols - 1;
							row--;
						}
						break;
					case VERTICAL:
						row--;
						if(row <= 0){
							row = menu.rows - 1;
							col--;
						}
						break;
				}
			}
		}while(!canPlace() && (row > 0 || col > 0));
		return this;
	}
	
	@NotNull
	public SlotIterator blacklist(int row, int col){
		if(row < 0 || col < 0){
			throw new IllegalArgumentException("row & col must not be negative");
		}
		return blacklist(new Slot(row, col));
	}
	
	@NotNull
	public SlotIterator blacklist(Slot slot){
		blacklist.add(slot);
		return this;
	}
	
	public int row(){
		return row;
	}
	
	@NotNull
	public SlotIterator row(int row){
		if(row < 0){
			throw new IllegalArgumentException("row must not be negative");
		}
		this.row = row;
		return this;
	}
	
	public int col(){
		return col;
	}
	
	@NotNull
	public SlotIterator col(int col){
		if(col < 0){
			throw new IllegalArgumentException("col must not be negative");
		}
		this.col = col;
		return this;
	}
	
	public boolean started(){
		return started;
	}
	
	public boolean ended(){
		return row == menu.rows - 1 && col == menu.cols - 1;
	}
	
	public boolean allowsOverride(){
		return allowOverride;
	}
	
	@NotNull
	public SlotIterator allowOverride(boolean override){
		this.allowOverride = override;
		return this;
	}
	
	private boolean canPlace(){
		return !blacklist.contains(new Slot(row, col)) && (allowOverride || get() == null);
	}
	
	public enum Type {
		VERTICAL, HORIZONTAL
	}
	
}
