package com.xenry.stagecraft.profile.permissions.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
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
 * StageCraft created by Henry Blasingame (Xenry) on 11/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PermissionSetListCommand extends Command<Core,ProfileManager> {
	
	public static final int ITEMS_PER_PAGE = 40;
	
	public PermissionSetListCommand(ProfileManager manager){
		super(manager, Rank.ADMIN, "list");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		List<String> names = manager.getPermissionsHandler().getPermissionSetNames();
		if(names.isEmpty()){
			sender.sendMessage(M.error("There are no permission sets."));
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
		
		int maxPages = (int) Math.ceil(names.size() / (double)ITEMS_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstItem = (page-1) * ITEMS_PER_PAGE;
		int itemsOnThisPage = Math.min(names.size() - firstItem, ITEMS_PER_PAGE);
		List<String> setsToDisplay = names.subList(firstItem, firstItem + itemsOnThisPage);
		
		ComponentBuilder cb = new ComponentBuilder(" » ").color(ChatColor.DARK_GRAY).bold(true);
		Iterator<String> it = setsToDisplay.iterator();
		while(it.hasNext()){
			String name = it.next();
			ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/permission set view " + name);
			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/permission set view " + name));
			cb.append(name).color(M.WHITE).bold(false).event(ce).event(he);
			if(it.hasNext()){
				cb.append(", ").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null);
			}
		}
		
		if(names.size() > ITEMS_PER_PAGE){
			sender.sendMessage(M.msg + "Showing permission sets, page " + M.elm + page + M.msg + " of " + M.elm + maxPages
					+ M.msg + " (" + M.elm + (firstItem+1) + M.msg + "-" + M.elm + (firstItem + itemsOnThisPage) + M.msg
					+ " of " + M.elm + names.size() + M.msg + " sets):");
		}else{
			sender.sendMessage(M.msg + "Showing all permission sets:");
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
