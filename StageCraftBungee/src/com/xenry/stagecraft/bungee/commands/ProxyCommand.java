package com.xenry.stagecraft.bungee.commands;
import com.xenry.stagecraft.bungee.Manager;
import net.md_5.bungee.api.plugin.Command;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class ProxyCommand<M extends Manager> extends Command {
	
	protected final M manager;
	
	public ProxyCommand(M manager, String label, String permission, String...aliases){
		super(label, permission, aliases);
		this.manager = manager;
	}
	
}
