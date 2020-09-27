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
public final class HNSCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSCommand(HideNSeekManager manager){
		super(manager, Rank.MEMBER, "hidenseek", "hns", "sardines");
		addSubCommand(new HNSJoinCommand(manager));
		addSubCommand(new HNSLeaveCommand(manager));
		addSubCommand(new HNSModeCommand(manager));
		addSubCommand(new HNSWhoCommand(manager));
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(msg + "§lHide'N'Seek Command List:");
		sender.sendMessage(hlt + " /" + label + " join" + gry + " - Join the game");
		sender.sendMessage(hlt + " /" + label + " leave" + gry + " - Leave the game");
		sender.sendMessage(hlt + " /" + label + " hide" + gry + " - Join the hiders team");
		sender.sendMessage(hlt + " /" + label + " seek" + gry + " - Join the seekers team");
		sender.sendMessage(hlt + " /" + label + " spectate" + gry + " - Spectate the game");
		sender.sendMessage(hlt + " /" + label + " who" + gry + " - See who is in the game");
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Arrays.asList("join", "leave", "hide", "seek", "spectate", "who");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Arrays.asList("join", "leave", "hide", "seek", "spectate", "who");
	}
	
}
