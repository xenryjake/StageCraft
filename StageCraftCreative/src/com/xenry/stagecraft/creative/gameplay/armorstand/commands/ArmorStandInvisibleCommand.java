package com.xenry.stagecraft.creative.gameplay.armorstand.commands;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler.INTERACT_INVISIBLE;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandInvisibleCommand extends AbstractArmorStandCommand {
	
	public ArmorStandInvisibleCommand(ArmorStandHandler handler){
		super(handler, INTERACT_INVISIBLE, "invisible", "invis", "visible", "vis");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		ArmorStand as = handler.getStand(profile);
		if(as == null || !handler.checkPerms(player, as)){
			return;
		}
		boolean invisible = !as.isInvisible();
		as.setInvisible(invisible);
		player.sendMessage(M.msg + "This armor stand is now " + (invisible ? "§cinvisible" : "§avisible") + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
