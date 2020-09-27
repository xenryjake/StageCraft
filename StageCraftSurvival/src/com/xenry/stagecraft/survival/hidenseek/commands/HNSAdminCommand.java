package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class HNSAdminCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSAdminCommand(HideNSeekManager manager){
		super(manager, Rank.ADMIN, "hidenseekadmin", "ha", "hnsa");
		addSubCommand(new HNSCreateCommand(manager));
		addSubCommand(new HNSStartCommand(manager));
		addSubCommand(new HNSKickCommand(manager));
		addSubCommand(new HNSEndCommand(manager));
		addSubCommand(new HNSSettingsCommand(manager));
		addSubCommand(new HNSMapCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(msg + "§lHide'N'Seek Admin Command List:");
		sender.sendMessage(hlt + " /" + label + " create" + gry + " - Create a new game");
		sender.sendMessage(hlt + " /" + label + " start" + gry + " - Start the game");
		sender.sendMessage(hlt + " /" + label + " kick" + gry + " - Kick a player from the game");
		sender.sendMessage(hlt + " /" + label + " end" + gry + " - End the game");
		sender.sendMessage(hlt + " /" + label + " settings" + gry + " - Change game settings");
		sender.sendMessage(hlt + " /" + label + " map" + gry + " - Change the map");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Arrays.asList("create", "start", "kick", "end", "settings", "map");
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Arrays.asList("create", "start", "kick", "end", "settings", "map");
	}
	
}
