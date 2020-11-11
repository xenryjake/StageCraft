package com.xenry.stagecraft.survival.teleportation.commands.warp;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.teleportation.Teleportation;
import com.xenry.stagecraft.survival.teleportation.TeleportationManager;
import com.xenry.stagecraft.survival.teleportation.Warp;
import com.xenry.stagecraft.survival.teleportation.commands.TPCommand;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WarpCommand extends Command<Survival,TeleportationManager> {
	
	public static final int WARPS_PER_PAGE = 20;
	
	public WarpCommand(TeleportationManager manager){
		super(manager, Rank.MEMBER, "warp", "w", "warpo", "wo", "warps");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(label.equalsIgnoreCase("warps") || args.length == 0 || args[0].matches("[0-9]+")){
			listWarps(profile.getPlayer(), args);
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
		boolean canDoOther = TPCommand.OTHER_RANK.has(profile);
		if(args.length > 1 && canDoOther){
			serverPerform(profile.getPlayer(), args, label);
			return;
		}
		Warp warp = manager.getWarpHandler().getWarp(args[0]);
		if(warp == null){
			profile.sendMessage(M.error("That warp does not exist: " + args[0]));
			return;
		}
		manager.createAndExecuteTeleportation(profile.getPlayer(), profile.getPlayer(), profile.getPlayer().getLocation(), warp.getLocation(), safe ? Teleportation.Type.WARP : Teleportation.Type.ADMIN, safe);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(label.equalsIgnoreCase("warps") || args.length == 0 || args[0].matches("[0-9]+")){
			listWarps(sender, args);
			return;
		}
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <warp>"));
			return;
		}
		Warp warp = manager.getWarpHandler().getWarp(args[1]);
		if(warp == null){
			sender.sendMessage(M.error("That warp does not exist: " + args[1]));
			return;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(M.error("Player not found: " + args[0]));
			return;
		}
		manager.createAndExecuteTeleportation(target, sender, target.getLocation(), warp.getLocation(), Teleportation.Type.ADMIN, !label.endsWith("o"));
	}
	
	private void listWarps(CommandSender sender, String[] args){
		List<String> warps = manager.getWarpHandler().getWarpNameList();
		if(warps.isEmpty()){
			sender.sendMessage(M.error("There are no warps set."));
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
		
		int maxPages = (int) Math.ceil(warps.size() / (double)WARPS_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstWarp = (page-1) * WARPS_PER_PAGE;
		int warpsOnThisPage = Math.min(warps.size() - firstWarp, WARPS_PER_PAGE);
		List<String> warpsToDisplay = warps.subList(firstWarp, firstWarp + warpsOnThisPage);
		
		ComponentBuilder cb = new ComponentBuilder(" » ").color(ChatColor.DARK_GRAY).bold(true);
		Iterator<String> it = warpsToDisplay.iterator();
		while(it.hasNext()){
			String name = it.next();
			ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + name);
			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/warp " + name));
			cb.append(name).color(M.WHITE).bold(false).event(ce).event(he);
			if(it.hasNext()){
				cb.append(", ").color(ChatColor.GRAY).event((ClickEvent)null).event((HoverEvent)null);
			}
		}
		
		if(warps.size() > WARPS_PER_PAGE){
			sender.sendMessage(M.msg + "Showing warps, page " + M.elm + page + M.msg + " of " + M.elm + maxPages + M.msg + " (" + M.elm + (firstWarp+1) + M.msg + "-" + M.elm + (firstWarp + warpsOnThisPage) + M.msg + " of " + M.elm + warps.size() + M.msg + " warps):");
		}else{
			sender.sendMessage(M.msg + "Showing all warps:");
		}
		sender.spigot().sendMessage(cb.create());
		sender.sendMessage(M.msg + "Use " + M.elm + "/warp <name>" + M.msg + " to teleport to a warp.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? manager.getWarpHandler().getWarpNameList() : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? manager.getWarpHandler().getWarpNameList() : Collections.emptyList();
	}
	
}
