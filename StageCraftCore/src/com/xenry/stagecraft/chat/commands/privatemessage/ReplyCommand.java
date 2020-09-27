package com.xenry.stagecraft.chat.commands.privatemessage;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ReplyCommand extends Command<Core,ChatManager> {
	
	public ReplyCommand(ChatManager manager){
		super(manager, Rank.MEMBER, "reply", "respond", "r");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		String name = manager.getConversation(M.CONSOLE_NAME);
		if(name == null){
			sender.sendMessage(M.error("You haven't messaged anyone recently."));
			return;
		}
		Player to = Bukkit.getPlayer(name);
		if(to == null){
			sender.sendMessage(M.error("The player who you last messaged isn't online."));
			return;
		}
		StringBuilder message = new StringBuilder();
		for(String arg : args) {
			message.append(arg).append(" ");
		}
		String msg = message.toString().trim();
		manager.sendPrivateMessage(manager.plugin.getProfileManager().getProfile(to), null, msg);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		String name = manager.getConversation(profile.getLatestUsername());
		if(name == null){
			profile.sendMessage(M.error("You haven't messaged anyone recently."));
			return;
		}
		Player to;
		if(name.equals(M.CONSOLE_NAME)){
			to = null;
		}else{
			to = Bukkit.getPlayer(name);
			if(to == null){
				profile.sendMessage(M.error("The player who you last messaged isn't online."));
				return;
			}
		}
		StringBuilder message = new StringBuilder();
		for(String arg : args) {
			message.append(arg).append(" ");
		}
		String msg = message.toString().trim();
		manager.sendPrivateMessage(manager.plugin.getProfileManager().getProfile(to), profile, msg);
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
