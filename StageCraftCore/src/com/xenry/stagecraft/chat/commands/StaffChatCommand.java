package com.xenry.stagecraft.chat.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class StaffChatCommand extends Command<Core,ChatManager> {
	
	public StaffChatCommand(ChatManager manager){
		super(manager, Rank.MOD, "staffchat", "sc");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <message>"));
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(String arg : args){
			sb.append(arg).append(" ");
		}
		String message = Emote.replaceEmotes(sb.toString().trim(), ChatColor.GRAY);
		Log.info("[StaffChat] " + sender.getName() + ": " + message);
		for(Profile profile : manager.plugin.getProfileManager().getOnlineProfiles()){
			if(profile.check(Rank.MOD)){
				profile.sendMessage("§5[SC] " + sender.getName() + "§8:§7 " + message);
			}
		}
	}
	
}
