package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameStatus;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.command.CommandSender;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class HNSJoinCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSJoinCommand(HideNSeekManager manager){
		super(manager, Rank.MEMBER, "join", "j");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(manager.plugin.getBuilderManager().isBuilder(profile)){
			profile.sendMessage(err + "You can't join the game when in builder mode.");
			return;
		}
		if(manager.getStatus() == GameStatus.NONE){
			profile.sendMessage(err + "There is no game to join.");
			return;
		}
		if(PlayerUtil.hasItemsInInventory(profile.getPlayer())){
			profile.sendMessage(err + "Cannot join the game. Your inventory is not empty!");
			return;
		}
		if(manager.getPlayerHandler().getPlayers().containsKey(profile.getPlayer().getName())){
			profile.sendMessage(err + "You're already in the game.");
			return;
		}
		if(!manager.getPlayerHandler().addPlayer(profile)) {
			profile.sendMessage(err + "You can't join the game right now.");
		}
	}
	
}
