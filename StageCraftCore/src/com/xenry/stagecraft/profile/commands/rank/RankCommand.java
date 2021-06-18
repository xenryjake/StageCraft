package com.xenry.stagecraft.profile.commands.rank;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * MineTogether created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RankCommand extends Command<Core,ProfileManager> {
	
	public RankCommand(ProfileManager manager){
		super(manager, Rank.MOD, "rank", "ranks");
		addSubCommand(new RankListCommand(manager));
		addSubCommand(new RankLookupCommand(manager));
		addSubCommand(new RankPermanentCommand(manager));
		addSubCommand(new RankTemporaryCommand(manager));
		addSubCommand(new RankRemoveCommand(manager));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
		List<Rank> ranks = profile.getRanks();
		if(ranks.isEmpty()){
			profile.sendMessage(M.elm + profile.getLatestUsername() + M.msg + " does not have any ranks.");
		}else{
			ComponentBuilder cb = new ComponentBuilder("You have the following ranks: ").color(M.msg);
			Iterator<Rank> it = ranks.iterator();
			while(it.hasNext()){
				Rank rank = it.next();
				cb.append(rank.name).color(rank.color);
				if(it.hasNext()){
					cb.append(", ").color(M.DGRAY);
				}
			}
			profile.sendMessage(cb.create());
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sender.sendMessage(M.msg + "Rank Commands:");
		sender.sendMessage(M.help(label + " list", "See all available ranks."));
		sender.sendMessage(M.help(label + " lookup", "See a player's ranks."));
		sender.sendMessage(M.help(label + " perm", "Give a player a rank permanently."));
		sender.sendMessage(M.help(label + " temp", "Give a player a rank temporarily."));
		sender.sendMessage(M.help(label + " remove", "Remove a rank from a player."));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("list", "lookup", "perm", "temp", "remove"), args[0])
				: Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("list", "lookup", "perm", "temp", "remove"), args[0])
				: Collections.emptyList();
	}
	
}
