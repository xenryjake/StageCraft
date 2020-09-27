package com.xenry.stagecraft.survival.hidenseek.game;
/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class GameTimer {
	
	public enum Mode {
		OFF, COUNT_UP, COUNT_DOWN
	}
	
	private long lastUpdate = 0;
	private long extraMS = 0;
	
	private int seconds = 0;
	private Mode mode = Mode.OFF;
	
	public GameTimer(){}
	
	public String getClock(){
		if(seconds < 0){
			seconds = 0;
		}
		
		String mins = String.valueOf((int)Math.floor(seconds/60d));
		String secs = String.valueOf(seconds % 60);
		
		if((seconds % 60) < 10){
			secs = "0" + secs;
		}
		return mins + ":" + secs;
	}
	
	public void update(){
		if(mode == Mode.OFF){
			seconds = 0;
			return;
		}
		
		long msPast = System.currentTimeMillis() - lastUpdate;
		int secPast = (int)Math.floor(msPast/1000d);
		
		extraMS += msPast % 1000;
		
		if(extraMS >= 1000){
			secPast += (int)Math.floor(extraMS/1000d);
			extraMS = extraMS % 1000;
		}
		
		if(mode == Mode.COUNT_DOWN){
			seconds -= secPast;
			if(seconds <= 0){
				seconds = 0;
			}
		}
		if(mode == Mode.COUNT_UP){
			seconds += secPast;
		}
		
		lastUpdate = System.currentTimeMillis();
	}
	
	public void reset(){
		seconds = 0;
		lastUpdate = System.currentTimeMillis();
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
		lastUpdate = System.currentTimeMillis();
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
		lastUpdate = System.currentTimeMillis();
	}
	
}
