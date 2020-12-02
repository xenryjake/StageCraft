package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/20/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class AbstractArmorStandCommand extends PlayerCommand<Creative,GameplayManager> {
	
	protected final ArmorStandHandler handler;
	
	protected AbstractArmorStandCommand(ArmorStandHandler handler, Access access, String...labels){
		super(handler.getManager(), access, labels);
		this.handler = handler;
	}
	
}
