package com.xenry.stagecraft.survival.economy;
/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("SameParameterValue")
public enum Currency {
	
	;
	
	private final String name;
	private final String display;
	private final boolean canBeNegative;
	private final int startValue;
	
	Currency(String name, String display, boolean canBeNegative, int startValue){
		this.name = name;
		this.display = display;
		this.canBeNegative = canBeNegative;
		this.startValue = startValue;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDisplayString() {
		return display;
	}
	
	public String getDisplay(double amount){
		return display.replace("%n%", String.format("%.2f", Math.floor(amount * 100) / 100)).replace("(s)", amount == 1 ? "" : "s");
	}
	
	public boolean canBeNegative() {
		return canBeNegative;
	}
	
	public int getStartValue() {
		return startValue;
	}
	
}
