package com.xenry.stagecraft.survival.jail.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.jail.JailManager;
import com.xenry.stagecraft.survival.jail.Sentence;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeLength;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.xenry.stagecraft.punishment.commands.PunishmentViewCommand.ITEMS_PER_PAGE;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SentenceViewCommand extends Command<Survival,JailManager> {
	
	public SentenceViewCommand(JailManager manager) {
		super(manager, Rank.MOD, "view", "see", "lookup");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/sentence " + label + " <player>"));
			return;
		}
		
		Profile target;
		if(args[0].length() <= 17) {
			if(Bukkit.getPlayer(args[0]) != null) {
				target = getCore().getProfileManager().getProfile(Bukkit.getPlayer(args[0]));
			} else {
				target = getCore().getProfileManager().getProfileByLatestUsername(args[0]);
			}
		} else {
			target = getCore().getProfileManager().getProfileByUUID(args[0]);
		}
		if(target == null) {
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		List<Sentence> list = manager.getSentences(target);
		if(list.isEmpty()){
			sender.sendMessage(M.error(M.elm + target.getLatestUsername() + M.err + " doesn't have any jail sentences on their record."));
			return;
		}
		
		int page = 1;
		if(args.length > 2){
			try{
				page = Integer.parseInt(args[2]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Please enter a valid integer."));
				return;
			}
		}
		
		int maxPages = (int) Math.ceil(list.size() / (double)ITEMS_PER_PAGE);
		if(page > maxPages){
			page = maxPages;
		}
		
		int firstItem = (page-1) * ITEMS_PER_PAGE;
		int itemsOnThisPage = Math.min(list.size() - firstItem, ITEMS_PER_PAGE);
		if(list.size() > ITEMS_PER_PAGE){
			sender.sendMessage(M.msg + "Showing jail sentences, page " + M.elm + page + M.msg + " of " + M.elm + maxPages + M.msg + " (" + M.elm + firstItem + M.msg + "-" + M.elm + (firstItem + itemsOnThisPage) + M.msg + " of " + M.elm + list.size() + M.msg + "):");
		}else{
			sender.sendMessage(M.msg + "Showing all jail sentences:");
		}
		List<Sentence> sentencesToDisplay = list.subList(firstItem, firstItem + itemsOnThisPage);
		for(Sentence sentence : sentencesToDisplay){
			String punishedByString;
			if(sentence.getPunishedByUUID().equals(M.CONSOLE_NAME)){
				punishedByString = M.CONSOLE_NAME;
			}else{
				Profile punishedBy = getCore().getProfileManager().getProfileByUUID(sentence.getPunishedByUUID());
				if(punishedBy == null){
					punishedByString = sentence.getPunishedByUUID();
				}else{
					punishedByString = punishedBy.getLatestUsername();
				}
			}
			sender.sendMessage(M.arrow((sentence.isActive() ? "§a§lACTIVE§r " + M.msg : "")) + "Jail sentence at " + M.elm + TimeUtil.dateFormat(sentence.getTimestamp() * TimeLength.SECOND) + M.msg + " by " + M.elm + punishedByString + M.msg + ". Jail: " + M.WHITE + sentence.getJailName() + (sentence.getReason().trim().isEmpty() ? "" : M.msg + " Reason: " + M.WHITE + sentence.getReason()));
		}
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
