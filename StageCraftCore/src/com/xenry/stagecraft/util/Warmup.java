package com.xenry.stagecraft.util;
import com.xenry.stagecraft.Core;
import org.bukkit.Bukkit;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Warmup {
	
	private final long warmupTicks;
	private final Runnable runnable;
	private Integer taskID = null;
	
	public Warmup(long warmupTime, Runnable runnable){
		this.warmupTicks = warmupTime;
		this.runnable = runnable;
	}
	
	public void use(){
		taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), runnable, warmupTicks);
	}
	
	public void cancel(){
		if(taskID == null){
			return;
		}
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
}
