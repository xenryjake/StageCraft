package com.xenry.stagecraft.creative.teleportation.commands.home;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.teleportation.Home;
import com.xenry.stagecraft.creative.teleportation.TeleportationManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.NumberUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ViewHomesCommand extends Command<Creative,TeleportationManager> {
	
	public ViewHomesCommand(TeleportationManager manager){
		super(manager, Rank.MOD, "viewhomes", "seehomes", "viewhome", "seehome", "listhomes", "listhome");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Profile profile;
		if(Bukkit.getPlayer(args[0]) != null){
			profile = getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
		}else{
			profile = getCore().getProfileManager().getProfileByLatestUsername(args[0]);
		}
		if(profile == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		listHomes(sender, profile, args);
	}
	
	private void listHomes(CommandSender sender, Profile profile, String[] args){
		List<Home> homes = manager.getHomeHandler().getHomes(profile);
		if(homes.isEmpty()){
			sender.sendMessage(M.error(M.elm + profile.getLatestUsername() + M.err + " has no homes set."));
			return;
		}
		int page = 1;
		if(args.length > 1){
			try{
				page = Integer.parseInt(args[1]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Please enter a valid integer."));
				return;
			}
		}
		
		final int HOMES_PER_PAGE = 10;
		int maxPages = (int) Math.ceil(homes.size() / (double)HOMES_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstHome = (page-1) * HOMES_PER_PAGE;
		int homesOnThisPage = Math.min(homes.size() - firstHome, HOMES_PER_PAGE);
		List<Home> homesToDisplay = homes.subList(firstHome, firstHome + homesOnThisPage);
		
		if(homes.size() > HOMES_PER_PAGE){
			sender.sendMessage(M.msg + "Showing " + M.elm + profile.getLatestUsername() + M.msg + "'s homes, page " + M.elm + page + M.msg + " of " + M.elm + maxPages + M.msg + " (" + M.elm + firstHome + M.msg + "-" + M.elm + (firstHome + homesOnThisPage) + M.msg + " of " + M.elm + homes.size() + M.msg + " homes):");
		}else{
			sender.sendMessage(M.msg + M.BOLD + "Showing all of " + M.elm + profile.getLatestUsername() + "'s homes:");
		}
		for(Home home : homesToDisplay){
			String name = home.getName();
			ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gotohome " + profile.getLatestUsername() + " " + name);
			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/gotohome " + profile.getLatestUsername() + " " + name));
			ComponentBuilder cb = new ComponentBuilder(" » ").color(ChatColor.DARK_GRAY).bold(true);
			cb.append(name).color(ChatColor.WHITE).event(he).event(ce).bold(false);
			cb.append(" (" + NumberUtil.displayAsHundredths(home.getX()) + ","
					+ NumberUtil.displayAsHundredths(home.getY()) + ","
					+ NumberUtil.displayAsHundredths(home.getZ()) + ") [" + home.getWorldName() + "]")
					.color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null).bold(false);
			sender.spigot().sendMessage(cb.create());
		}
		sender.sendMessage(M.msg + "Use " + M.elm + "/gotohome " + profile.getLatestUsername() + " <name>" + M.msg + " to teleport to a home.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? allLocalPlayers() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? allLocalPlayers() : Collections.emptyList();
	}
}
