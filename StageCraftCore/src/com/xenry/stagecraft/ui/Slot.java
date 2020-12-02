package com.xenry.stagecraft.ui;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Slot {
	
	public final int row, col;
	
	public Slot(int row, int col) {
		if(row < 0 || col < 0){
			throw new IllegalArgumentException("row & col must not be negative");
		}
		this.row = row;
		this.col = col;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o){
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		Slot slot = (Slot)o;
		return row == slot.row && col == slot.col;
	}
	
}
