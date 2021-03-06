package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandClipboard;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandCopyCommand extends AbstractArmorStandCommand {
	
	public ArmorStandCopyCommand(ArmorStandHandler handler){
		super(handler, Rank.MEMBER, "copy");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(manager.isLockoutMode()){
			profile.sendMessage(M.error("You can't edit armor stands while lockout mode is enabled."));
			return;
		}
		ArmorStand as = handler.getStand(profile);
		if(as == null){
			return;
		}
		handler.setClipboard(profile, ArmorStandClipboard.from(as));
		profile.sendMessage(M.msg + "Copied this armor stand to your clipboard.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
