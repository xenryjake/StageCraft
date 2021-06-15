package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class OverrideListCommand extends Command<Creative,GameplayManager> {
	
	public OverrideListCommand(GameplayManager manager){
		super(manager, Rank.MOD, "list");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		StringBuilder sb = new StringBuilder();
		sb.append(M.msg).append("Players in override mode: ");
		String message;
		if(manager.getPlayerOverrides().size() == 0){
			sb.append(M.err).append("none");
			message = sb.toString();
		}else{
			for(String name : manager.getPlayerOverrides()){
				sb.append(M.elm).append(name).append(M.gry).append(", ");
			}
			message = sb.toString();
			message = message.substring(0, message.length() - 2);
		}
		sender.sendMessage(message);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
