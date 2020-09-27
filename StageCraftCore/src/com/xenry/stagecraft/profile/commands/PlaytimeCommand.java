package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/1/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlaytimeCommand extends Command<Core,ProfileManager> {
	
	public static final Access VIEW_OTHER = Rank.MEMBER;
	
	public PlaytimeCommand(ProfileManager manager){
		super(manager, Rank.MEMBER, "playtime");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(@NotNull Profile sender, String[] args, String label) {
		Profile target = sender;
		if(args.length > 0 && VIEW_OTHER.has(sender)){
			if(args[0].length() <= 17){
				if(Bukkit.getPlayer(args[0]) != null){
					target = manager.getProfile(Bukkit.getPlayer(args[0]));
				}else{
					target = manager.getProfileByLatestUsername(args[0]);
				}
			}else{
				target = manager.getProfileByUUID(args[0]);
			}
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		sender.sendMessage(M.elm + target.getLatestUsername() + M.msg + "'s total playtime: " + M.WHITE + TimeUtil.simplerString(target.getTotalPlaytime()));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/playtime <player>"));
			return;
		}
		Profile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = manager.getProfileByLatestUsername(args[0]);
				/*try{
					target = manager.getProfileByUUID(UUIDFetcher.getUUIDOf(args[0]).toString());
				}catch(Exception ex){
					sender.sendMessage(M.error("That username is invalid."));
					return;
				}*/
			}
		}else{
			target = manager.getProfileByUUID(args[0]);
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		sender.sendMessage(M.elm + target.getLatestUsername() + M.msg + "'s total playtime: " + M.WHITE + TimeUtil.simplerString(target.getTotalPlaytime()));
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
}