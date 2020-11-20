package com.xenry.stagecraft.util.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class CancellableEvent extends Event implements Cancellable {

	protected boolean cancelled;
	
	public CancellableEvent(boolean isAsync){
		super(isAsync);
	}

	public CancellableEvent(){
		this(false);
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
}
