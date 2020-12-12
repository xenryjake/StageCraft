package com.xenry.stagecraft.chat.commands.privatemessage;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.CollectionUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class MessageCommand extends Command<Core,ChatManager> {
	
	public MessageCommand(ChatManager manager){
		super(manager, Rank.MEMBER, "message", "msg", "m", "tell", "t", "whisper", "w", "privatemessage",
				"pmsg", "pm");
		setCanBeDisabled(true);
	}
	
	/*@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <player> <message>"));
			return;
		}
		Player to;
		if(args[0].equalsIgnoreCase(M.CONSOLE_NAME)) {
			to = null;
		}else{
			to = Bukkit.getPlayer(args[0]);
			if(to == null){
				sender.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}
		String msg = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		manager.sendPrivateMessage(manager.plugin.getProfileManager().getProfile(to), null, msg);
	}*/
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 2){
			profile.sendMessage(M.usage("/" + label + " <player> <message>"));
			return;
		}
		/*Player to;
		if(args[0].equalsIgnoreCase(M.CONSOLE_NAME)) {
			to = null;
		}else{
			to = Bukkit.getPlayer(args[0]);
			if(to == null){
				profile.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}*/
		String toName = CollectionUtil.findClosestMatchByStart(allNetworkPlayers(), args[0]);
		if(toName == null){
			profile.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		
		String msg = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		//manager.sendPrivateMessage(manager.plugin.getProfileManager().getProfile(to), profile, msg);
		manager.handlePrivateMessage(profile, toName, msg);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return networkPlayers(args[args.length-1]);
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return networkPlayers(args[args.length-1]);
	}
	
}
