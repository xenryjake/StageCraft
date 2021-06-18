package com.xenry.stagecraft.profile.commands.rank;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * MineTogether created by Henry Blasingame (Xenry) on 3/17/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class RankLookupCommand extends Command<Core,ProfileManager> {
	
	public RankLookupCommand(ProfileManager manager) {
		super(manager, Rank.MOD, "lookup", "check", "get", "view");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/rank lookup <username|uuid>"));
			return;
		}
		Profile profile;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				profile = manager.getProfile(Bukkit.getPlayer(args[0]));
			}else{
				profile = manager.getProfileByLatestUsername(args[0]);
			}
		}else{
			profile = manager.getProfileByUUID(args[0]);
		}
		if(profile == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		List<Rank> ranks = profile.getRanks();
		if(ranks.isEmpty()){
			sender.sendMessage(M.elm + profile.getLatestUsername() + M.msg + " does not have any ranks.");
			return;
		}
		ComponentBuilder cb = new ComponentBuilder(profile.getLatestUsername()).color(M.elm)
				.append(" has the following ranks: ").color(M.msg);
		Iterator<Rank> it = ranks.iterator();
		while(it.hasNext()){
			Rank rank = it.next();
			cb.append(rank.name).color(rank.color);
			if(it.hasNext()){
				cb.append(", ").color(M.DGRAY);
			}
		}
		sender.sendMessage(cb.create());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
}
