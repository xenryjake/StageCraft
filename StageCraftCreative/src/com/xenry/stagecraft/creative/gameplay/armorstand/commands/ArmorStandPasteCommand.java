package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandClipboard;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
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
public final class ArmorStandPasteCommand extends AbstractArmorStandCommand {
	
	public ArmorStandPasteCommand(ArmorStandHandler handler){
		super(handler, Rank.MEMBER, "paste");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(manager.isLockoutMode()){
			player.sendMessage(M.error("You can't edit armor stands while lockout mode is enabled."));
			return;
		}
		ArmorStandClipboard clipboard = handler.getClipboard(profile);
		if(clipboard == null){
			player.sendMessage(M.error("Your clipboard is empty."));
			return;
		}
		
		ArmorStand as = handler.getStand(profile);
		if(as == null || !handler.checkPerms(player, as)){
			return;
		}
		clipboard.apply(as);
		profile.sendMessage(M.msg + "Pasted this armor stand from your clipboard.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
