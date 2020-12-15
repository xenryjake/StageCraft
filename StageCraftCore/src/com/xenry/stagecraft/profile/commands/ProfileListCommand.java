package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProfileListCommand extends Command<Core,ProfileManager> {
	
	//todo fix this or remove it idk
	
	public static final Access ACCESS = Rank.HEAD_MOD;
	public static final int ITEMS_PER_PAGE = 20;
	
	public ProfileListCommand(ProfileManager manager){
		super(manager, ACCESS, "profilelist");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		List<String> profiles = manager.getAllProfileLatestUsernames();
		if(profiles.isEmpty()){
			sender.sendMessage(M.error("There are no profiles."));
			return;
		}
		int page = 1;
		if(args.length > 0){
			try{
				page = Integer.parseInt(args[0]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Please enter a valid integer."));
				return;
			}
		}
		
		int maxPages = (int) Math.ceil(profiles.size() / (double)ITEMS_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstItem = (page-1) * ITEMS_PER_PAGE;
		int itemsOnThisPage = Math.min(profiles.size() - firstItem, ITEMS_PER_PAGE);
		List<String> profilesToDisplay = profiles.subList(firstItem, firstItem + itemsOnThisPage);
		
		ComponentBuilder cb = new ComponentBuilder(" » ").color(ChatColor.DARK_GRAY).bold(true);
		Iterator<String> it = profilesToDisplay.iterator();
		while(it.hasNext()){
			String name = it.next();
			ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/lookup " + name);
			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/lookup " + name));
			cb.append(name).color(M.WHITE).bold(false).event(ce).event(he);
			if(it.hasNext()){
				cb.append(", ").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null);
			}
		}
		
		if(profiles.size() > ITEMS_PER_PAGE){
			sender.sendMessage(M.msg + "Showing network profiles, page " + M.elm + page + M.msg + " of " + M.elm + maxPages
					+ M.msg + " (" + M.elm + (firstItem+1) + M.msg + "-" + M.elm + (firstItem + itemsOnThisPage) + M.msg
					+ " of " + M.elm + profiles.size() + M.msg + " profiles):");
		}else{
			sender.sendMessage(M.msg + "Showing all network profiles:");
		}
		sender.spigot().sendMessage(cb.create());
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
