package com.xenry.stagecraft.chat.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SayCommand extends Command<Core,ChatManager> {
	
	public SayCommand(ChatManager manager){
		super(manager, Rank.MEMBER, "say");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			profile.sendMessage(M.usage("/say <message>"));
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(String arg : args){
			sb.append(arg).append(" ");
		}
		profile.getPlayer().chat(sb.toString().trim());
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/say <message>"));
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(String arg : args){
			sb.append(arg).append(" ");
		}
		String message = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
		message = Emote.replaceEmotes(message, ChatColor.WHITE);
		Bukkit.broadcastMessage("§e§lServer§8:§r " + message);
	}
	
}
