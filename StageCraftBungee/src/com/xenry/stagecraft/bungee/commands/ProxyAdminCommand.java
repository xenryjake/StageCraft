package com.xenry.stagecraft.bungee.commands;
import com.xenry.stagecraft.bungee.Manager;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class ProxyAdminCommand<T extends Manager> extends ProxyCommand<T> {
	
	public ProxyAdminCommand(T manager, String label, String... aliases) {
		super(manager, label, "stagecraft.admin", aliases);
	}
	
}
