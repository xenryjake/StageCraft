package com.xenry.stagecraft.creative.teleportation.commands.home;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Home;
import com.xenry.stagecraft.creative.teleportation.Teleportation;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.creative.teleportation.commands.TPCommand;
import com.xenry.stagecraft.creative.teleportation.commands.warp.WarpCommand;
import com.xenry.stagecraft.profile.Profile;
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
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HomeCommand extends Command<Creative,TeleportationManager> {
	
	public HomeCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "home", "h", "homeo", "ho", "homes");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(label.equalsIgnoreCase("homes") || args.length == 0 || args[0].matches("[0-9]+")){
			listHomes(profile, args);
			return;
		}
		boolean safe = true;
		if(label.endsWith("o")){
			if(!TPCommand.SELF_RANK.has(profile)){
				noPermission(profile);
				return;
			}
			safe = false;
		}
		Home home = manager.getHomeHandler().getHome(profile, args[0]);
		if(home == null){
			profile.sendMessage(M.error("You don't have a home named " + M.elm + args[0] + M.err + "."));
			return;
		}
		manager.createAndExecuteTeleportation(profile.getPlayer(), profile.getPlayer(), profile.getPlayer().getLocation(), home.getLocation(), safe ? Teleportation.Type.HOME : Teleportation.Type.ADMIN, safe);
	}
	
	private void listHomes(Profile profile, String[] args){
		final int HOMES_PER_PAGE = WarpCommand.WARPS_PER_PAGE;
		List<String> homes = manager.getHomeHandler().getHomeNameList(profile);
		if(homes.isEmpty()){
			profile.sendMessage(M.error("You have no homes set."));
			return;
		}
		int page = 1;
		if(args.length > 0){
			try{
				page = Integer.parseInt(args[0]);
			}catch(Exception ex){
				profile.sendMessage(M.error("Please enter a valid integer."));
				return;
			}
		}
		
		int maxPages = (int) Math.ceil(homes.size() / (double)HOMES_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstHome = (page-1) * HOMES_PER_PAGE;
		int homesOnThisPage = Math.min(homes.size() - firstHome, HOMES_PER_PAGE);
		List<String> homesToDisplay = homes.subList(firstHome, firstHome + homesOnThisPage);
		
		ComponentBuilder cb = new ComponentBuilder(" » ").color(ChatColor.DARK_GRAY).bold(true);
		Iterator<String> it = homesToDisplay.iterator();
		while(it.hasNext()){
			String name = it.next();
			ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + name);
			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/home " + name));
			cb.append(name).color(M.WHITE).bold(false).event(ce).event(he);
			if(it.hasNext()){
				cb.append(", ").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null);
			}
		}
		
		if(homes.size() > HOMES_PER_PAGE){
			profile.sendMessage(M.msg + "Showing homes, page " + M.elm + page + M.msg + " of " + M.elm + maxPages
					+ M.msg + " (" + M.elm + (firstHome+1) + M.msg + "-" + M.elm + (firstHome + homesOnThisPage)
					+ M.msg + " of " + M.elm + homes.size() + M.msg + " homes):");
		}else{
			profile.sendMessage(M.msg + "Showing all homes:");
		}
		profile.sendMessage(cb.create());
		profile.sendMessage(M.msg + "Use " + M.elm + "/home <name>" + M.msg + " to teleport to a home.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getHomeHandler().getHomeNameList(profile) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}