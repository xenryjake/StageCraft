package com.xenry.stagecraft.chat.emotes;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
public final class EmotesCommand extends Command<Core,ChatManager> {
	
	public static final int EMOTES_PER_PAGE = 9;
	
	public EmotesCommand(ChatManager manager){
		super(manager, Rank.MEMBER, "emotes", "emotelist", "emote");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		sendList(profile.getPlayer(), args, Emote.getAvailableEmotes(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		sendList(sender, args, Arrays.asList(Emote.values()));
	}
	
	private void sendList(CommandSender sender, String[] args, List<Emote> emotes){
		if(emotes.size() == 0){
			sender.sendMessage(M.error("You don't have access to any emotes."));
			return;
		}
		int page = 1;
		if(args.length > 0){
			try{
				page = Integer.parseInt(args[0]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Please enter a valid page number."));
				return;
			}
		}
		if(page < 1){
			page = 1;
		}
		int maxPages = (int) Math.ceil(emotes.size() / (double)EMOTES_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		int firstWarp = (page-1) * EMOTES_PER_PAGE;
		int emotesOnThisPage = Math.min(emotes.size() - firstWarp, EMOTES_PER_PAGE);
		List<Emote> emotesToDisplay = emotes.subList(firstWarp, firstWarp + emotesOnThisPage);
		
		ComponentBuilder cb = new ComponentBuilder();
		if(emotes.size() > EMOTES_PER_PAGE){
			cb.append("Your emotes, page ").color(M.msg).append(page + " ").color(M.elm).append("of")
					.color(M.msg).append(" " + maxPages + " ").color(M.elm).append("[PREV]");
			if(page > 1){
				cb.color(M.GREEN).bold(true);
				cb.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/emotes " + (page - 1)));
				cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Page " + (page - 1))));
			}else{
				cb.color(M.DGRAY).bold(false);
			}
			cb.append(" ").reset().append("[NEXT]");
			if(page < maxPages){
				cb.color(M.GREEN).bold(true);
				cb.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/emotes " + (page + 1)));
				cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Page " + (page + 1))));
			}else{
				cb.color(M.DGRAY).bold(false);
			}
		}else{
			cb.append("Your emotes:").color(M.msg);
		}
		for(Emote emote : emotesToDisplay){
			if(emote.keywords.length == 0){
				continue;
			}
			cb.append("\n » ").color(M.DGRAY).bold(true).append(emote.keywords[0]).color(M.WHITE).bold(false)
					.append(" - ").color(M.gry).append(TextComponent.fromLegacyText(emote.replacement, M.WHITE))
					.reset();
		}
		sender.sendMessage(cb.create());
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
