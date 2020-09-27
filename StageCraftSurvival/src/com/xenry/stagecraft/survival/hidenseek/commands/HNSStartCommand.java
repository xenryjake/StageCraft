package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameStatus;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerMode;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class HNSStartCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSStartCommand(HideNSeekManager manager){
		super(manager, Rank.ADMIN, "start", "begin");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(manager.getStatus() == GameStatus.NONE){
			sender.sendMessage(err + "There is no game to start.");
			return;
		}
		if(sender instanceof Player && !manager.getPlayerHandler().isInGame((Player)sender)){
			sender.sendMessage(err + "You aren't in the game.");
			return;
		}
		if(manager.getStatus() != GameStatus.PRE_GAME){
			sender.sendMessage(err + "You can't start the game right now.");
			return;
		}
		if(manager.getPlayerHandler().getNumberOfActivePlayers() < manager.getSettings().getMinimumPlayers()){
			sender.sendMessage(err + "There are not enough players. There must be at least " + manager.getSettings().getMinimumPlayers() + " players.");
			return;
		}
		if(!manager.getPlayerHandler().getPlayers().containsValue(PlayerMode.HIDER) || !manager.getPlayerHandler().getPlayers().containsValue(PlayerMode.SEEKER)){
			sender.sendMessage(err + "There must be at least 1 hider and 1 seeker to start the game.");
			return;
		}
		if(!manager.startHiding(false)){
			sender.sendMessage(err + "Failed to start the game.");
		}
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
}
