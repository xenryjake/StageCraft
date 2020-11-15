package com.xenry.stagecraft.survival.jail.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.survival.teleportation.commands.warp.WarpCommand;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class JailListCommand extends Command<Survival,JailManager> {
	
	public JailListCommand(JailManager manager){
		super(manager, Rank.MOD, "jails", "jaillist", "jailslist", "listjail", "listjails");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		listJails(sender, args);
	}
	
	private void listJails(CommandSender sender, String[] args){
		List<String> jails = manager.getJailHandler().getJailNameList();
		if(jails.isEmpty()){
			sender.sendMessage(M.error("There are no jails set."));
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
		
		final int JAILS_PER_PAGE = WarpCommand.WARPS_PER_PAGE;
		int maxPages = (int) Math.ceil(jails.size() / (double)JAILS_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstJail = (page-1) * JAILS_PER_PAGE;
		int jailsOnThisPage = Math.min(jails.size() - firstJail, JAILS_PER_PAGE);
		List<String> jailsToDisplay = jails.subList(firstJail, firstJail + jailsOnThisPage);
		StringBuilder sb = new StringBuilder();
		for(String name : jailsToDisplay){
			sb.append(M.WHITE).append(name).append(M.gry).append(", ");
		}
		String jailString = sb.toString().trim();
		if(jailString.endsWith(",")){
			jailString = jailString.substring(0, jailString.length() - 1);
		}
		
		if(jails.size() > JAILS_PER_PAGE){
			sender.sendMessage(M.msg + "Showing jails, page " + M.elm + page + M.msg + " of " + M.elm + maxPages + M.msg + " (" + M.elm + (firstJail+1) + M.msg + "-" + M.elm + (firstJail + jailsOnThisPage) + M.msg + " of " + M.elm + jails.size() + M.msg + " jails):");
		}else{
			sender.sendMessage(M.msg + "Showing all jails:");
		}
		sender.sendMessage(M.arrow(jailString));
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
