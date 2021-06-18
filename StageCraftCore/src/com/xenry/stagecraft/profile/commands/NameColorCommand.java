package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.ui.NameColorMenu;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class NameColorCommand extends Command<Core,ProfileManager> {
	
	public static final Access OTHERS = Rank.ADMIN;
	
	// todo add admin ability to change others
	// todo update to multi-rank
	
	public NameColorCommand(ProfileManager manager){
		super(manager, Access.FALSE, "namecolor");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			//profile.sendMessage(M.usage("/" + label + " <color>"));
			new NameColorMenu(manager, profile.getUUID()).open(profile.getPlayer());
			return;
		}
		ChatColor color;
		try{
			color = ChatColor.of(args[0].toLowerCase());
		}catch(Exception ex){
			profile.sendMessage(M.error("Invalid color: " + args[0]));
			return;
		}
		if(!profile.getMainRank().getAvailableColors().contains(color)){
			profile.sendMessage(M.error("You don't have access to " + color + color.getName() + M.err + "."));
			return;
		}
		profile.setNameColor(color);
		manager.save(profile);
		profile.updateDisplayName();
		profile.sendMessage(M.msg + "Your name color is now " + color + color.getName() + M.msg + ".");
		//manager.getProfileNameInfoUpdatePMSC().send(profile.getPlayer(), profile);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
	
	}
	
	private void handle(CommandSender sender, String[] args, String label, boolean others){
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			return filter(profile.getMainRank().getAvailableColorNames(), args[0]);
		}else if(args.length == 2 && OTHERS.has(profile)){
			return networkPlayers(args[1]);
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 2 ? networkPlayers(args[1]) : Collections.emptyList();
	}
	
}
