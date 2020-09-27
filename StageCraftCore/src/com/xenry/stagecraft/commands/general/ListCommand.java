package com.xenry.stagecraft.survival.gameplay.commands;
import com.earth2me.essentials.Essentials;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ListCommand extends Command<Survival,GameplayManager> {
	
	public static final Access SEE_VANISHED = Rank.ADMIN;
	
	public ListCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "list", "who", "online", "ls");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		sendList(profile.getPlayer(), SEE_VANISHED.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sendList(sender, true);
	}
	
	private void sendList(CommandSender sender, boolean seeVanished){
		StringBuilder sb = new StringBuilder();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		Essentials ess = getCore().getIntegrationManager().getEssentials();
		int amount = 0;
		for(Player player : players){
			boolean vanished = ess != null && ess.getUser(player).isVanished();
			boolean afk = ess != null && ess.getUser(player).isAfk();
			if(vanished && !seeVanished){
				continue;
			}
			sb.append(M.WHITE).append(player.getDisplayName());
			if(afk){
				sb.append(M.gry).append(" [AFK]");
			}
			if(vanished){
				sb.append(M.gry).append(" [VANISHED]");
			}
			sb.append(M.DGRAY).append(", ");
			amount++;
		}
		String playersString = sb.toString().trim();
		if(playersString.endsWith(",")){
			playersString = playersString.substring(0, playersString.length() - 1);
		}
		sender.sendMessage(M.msg + M.BOLD + "Online Players (" + M.elm + M.BOLD + amount + M.msg + M.BOLD + "):");
		sender.sendMessage(M.arrow(playersString));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
