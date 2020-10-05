package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class NickCommand extends Command<Core,ProfileManager> {
	
	public NickCommand(ProfileManager manager){
		super(manager, Rank.ADMIN, "nick", "nickname");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player|me> <nickname>"));
			return;
		}
		Profile target;
		if(args[0].equalsIgnoreCase("me")){
			target = sender;
		}else{
			if(args[0].length() <= 17){
				if(Bukkit.getPlayer(args[0]) != null){
					target = manager.getProfile(Bukkit.getPlayer(args[0]));
				}else{
					target = manager.getProfileByLatestUsername(args[0]);
				}
			}else{
				target = manager.getProfileByUUID(args[0]);
			}
			if(target == null){
				sender.sendMessage(M.error("There is no profile for that player."));
				return;
			}
		}
		String nick;
		boolean disable = false;
		switch(args[1].toLowerCase()){
			case "none":
			case "off":
			case "clear":
				disable = true;
				nick = "none";
				break;
			default:
				StringBuilder sb = new StringBuilder();
				for(int i = 1; i < args.length; i++){
					sb.append(args[i]).append(" ");
				}
				nick = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
				break;
		}
		
		if(ChatColor.stripColor(nick).length() > 32){
			sender.sendMessage(M.error("Nicknames must be no longer than 32 characters."));
			return;
		}
		target.setNick(nick);
		if(disable){
			sender.sendMessage(M.msg + "Removed " + M.elm + target.getLatestUsername() + M.msg + "'s nickname.");
		}else{
			sender.sendMessage(M.msg + "Set " + M.elm + target.getLatestUsername() + M.msg + "'s nickname to " + M.WHITE + nick + M.msg + ".");
		}
		if(target.isOnline()){
			target.updateDisplayName();
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player|me> <nickname>"));
			return;
		}
		Profile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = manager.getProfileByLatestUsername(args[0]);
			}
		}else{
			target = manager.getProfileByUUID(args[0]);
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		String nick;
		boolean disable = false;
		switch(args[1].toLowerCase()){
			case "none":
			case "off":
			case "clear":
				disable = true;
				nick = "none";
				break;
			default:
				StringBuilder sb = new StringBuilder();
				for(int i = 1; i < args.length; i++){
					sb.append(args[i]).append(" ");
				}
				nick = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
				break;
		}
		
		if(ChatColor.stripColor(nick).length() > 32){
			sender.sendMessage(M.error("Nicknames must be no longer than 32 characters."));
			return;
		}
		target.setNick(nick);
		if(disable){
			sender.sendMessage(M.msg + "Removed " + M.elm + target.getLatestUsername() + M.msg + "'s nickname.");
		}else{
			sender.sendMessage(M.msg + "Set " + M.elm + target.getLatestUsername() + M.msg + "'s nickname to " + M.WHITE + nick + M.msg + ".");
		}
		if(target.isOnline()){
			target.updateDisplayName();
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? null : Collections.emptyList();
	}
	
}
