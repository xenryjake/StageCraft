package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameType;
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
public final class HNSCreateCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSCreateCommand(HideNSeekManager manager){
		super(manager, Rank.ADMIN, "create", "new");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(err + "/ha " + label + " <type>");
			profile.sendMessage(err + "Please enter a game type. Valid types are: " + hlt + "Normal" + err + ", " + hlt + "Sardines" + err + ".");
			return;
		}
		GameType type;
		if(args[0].toLowerCase().startsWith("n") || args[0].toLowerCase().startsWith("h")){
			type = GameType.HIDE_N_SEEK;
		}else if(args[0].toLowerCase().startsWith("s")){
			type = GameType.SARDINES;
		}else{
			profile.sendMessage(err + "Invalid game type. Valid types are: " + hlt + "Normal" + err + ", " + hlt + "Sardines" + err + ".");
			return;
		}
		if(PlayerUtil.hasItemsInInventory(profile.getPlayer())){
			profile.sendMessage(err + "Cannot create the game. Your inventory is not empty!");
			return;
		}
		if(!manager.createGame(type)){
			profile.sendMessage(err + "You can't create a game right now.");
			return;
		}
		profile.sendMessage(msg + "A game has been created (map: " + manager.getMap().getName() + ")");
		manager.getPlayerHandler().addPlayer(profile);
	}
	
}
