package com.xenry.stagecraft.ui;
import java.util.Arrays;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Pagination {
	
	private int current;
	private int itemsPerPage = 5;
	private Item[] items = new Item[0];
	
	public Item[] getItems(){
		return Arrays.copyOfRange(items, current * itemsPerPage, (current + 1) * itemsPerPage);
	}
	
	public int current() {
		return current;
	}
	
	public Pagination page(int page){
		if(page < 0){
			throw new IllegalArgumentException("page must not be negative");
		}
		current = page;
		return this;
	}
	
	public boolean isFirst(){
		return current <= 0;
	}
	
	public boolean isLast(){
		return current >= (int)Math.ceil((double)items.length / itemsPerPage) - 1;
	}
	
	public Pagination first(){
		current = 0;
		return this;
	}
	
	public Pagination next(){
		if(!isLast()){
			current++;
		}
		return this;
	}
	
	public Pagination prev(){
		if(!isFirst()){
			current--;
		}
		return this;
	}
	
	public Pagination last(){
		current = items.length / itemsPerPage;
		return this;
	}
	
	public Pagination addToIterator(SlotIterator iterator){
		for(Item item : getItems()){
			iterator.next().set(item);
			if(iterator.ended()){
				break;
			}
		}
		return this;
	}
	
	public Pagination setItems(Item...items){
		this.items = items;
		return this;
	}
	
	public Pagination setItemsPerPage(int itemsPerPage){
		this.itemsPerPage = itemsPerPage;
		return this;
	}
	
}
