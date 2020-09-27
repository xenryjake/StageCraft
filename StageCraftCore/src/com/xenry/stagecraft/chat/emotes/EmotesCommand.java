package com.xenry.stagecraft.chat.emotes;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class EmotesCommand extends Command<Core,ChatManager> {
	
	public static final int EMOTES_PER_PAGE = 10;
	
	public EmotesCommand(ChatManager manager){
		super(manager, Emote.EMOTE_ACCESS, "emotes", "emotelist", "emote");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		List<Emote> emotes = Arrays.asList(Emote.values());
		if(emotes.size() == 0){
			sender.sendMessage(M.error("There are no emotes set."));
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
		
		int maxPages = (int) Math.ceil(emotes.size() / (double)EMOTES_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstWarp = (page-1) * EMOTES_PER_PAGE;
		int emotesOnThisPage = Math.min(emotes.size() - firstWarp, EMOTES_PER_PAGE);
		List<Emote> emotesToDisplay = emotes.subList(firstWarp, firstWarp + emotesOnThisPage);
		StringBuilder sb = new StringBuilder();
		for(Emote emote : emotesToDisplay){
			if(emote.keywords.length == 0){
				continue;
			}
			sb.append("§r").append(M.arrow("§r")).append(emote.keywords[0]).append("§7 - §r").append(emote.replacement).append("\n");
		}
		String emoteString = sb.toString().trim();
		if(emoteString.endsWith(",")){
			emoteString = emoteString.substring(0, emoteString.length() - 1);
		}
		
		if(emotes.size() > EMOTES_PER_PAGE){
			sender.sendMessage(M.msg + "Showing emotes, page " + M.elm + page + M.msg + " of " + M.elm + maxPages + M.msg + " (" + M.elm + (firstWarp+1) + M.msg + "-" + M.elm + (firstWarp + emotesOnThisPage) + M.msg + " of " + M.elm + emotes.size() + M.msg + " emotes):\n" + emoteString);
		}else{
			sender.sendMessage(M.msg + "Showing all emotes:\n" + emoteString);
		}
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
