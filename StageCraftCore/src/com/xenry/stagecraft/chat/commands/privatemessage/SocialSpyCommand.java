package com.xenry.stagecraft.chat.commands.privatemessage;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.Setting;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SocialSpyCommand extends Command<Core,ChatManager> {
	
	public static final Access ACCESS = Rank.ADMIN;
	
	public SocialSpyCommand(ChatManager manager){
		super(manager, ACCESS, "socialspy");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		boolean enabled = profile.getSetting(Setting.SOCIAL_SPY);
		if(args.length < 1){
			profile.sendMessage(M.msg + "Your SocialSpy is currently " + (enabled ? "§aenabled" : "§cdisabled") + M.msg
					+ ".");
			return;
		}
		args[0] = args[0].toLowerCase();
		if(args[0].startsWith("on") || args[0].startsWith("en")){
			enabled = true;
		}else if(args[0].startsWith("off") || args[0].startsWith("dis")){
			enabled = false;
		}else if(args[0].startsWith("t")){
			enabled = !enabled;
		}else{
			profile.sendMessage(M.usage("/" + label + " [on|off|toggle]"));
			return;
		}
		profile.setSetting(Setting.SOCIAL_SPY, enabled);
		profile.sendMessage(M.msg + "Your SocialSpy has been " + (enabled ? "§aenabled" : "§cdisabled") + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("on", "off") : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? Arrays.asList("on", "off") : Collections.emptyList();
	}
	
}
