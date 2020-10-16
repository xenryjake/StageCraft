package com.xenry.stagecraft.chat.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.ChatManager;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class StaffChatCommand extends Command<Core,ChatManager> {
	
	public static final Access ACCESS = Rank.MOD;
	
	public StaffChatCommand(ChatManager manager){
		super(manager, ACCESS, "staffchat", "sc");
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
		Player pluginMessageSender;
		if(sender instanceof Player){
			pluginMessageSender = (Player)sender;
		}else{
			pluginMessageSender = PlayerUtil.getAnyPlayer();
			if(pluginMessageSender == null){
				sender.sendMessage(M.error("There are no players on this server. Please try again on another server."));
				return;
			}
		}
		
		String message = Joiner.on(' ').join(args);
		if(ChatManager.COLOR_ACCESS.has(sender)){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		if(Emote.EMOTE_ACCESS.has(sender)){
			message = Emote.replaceEmotes(message, ChatColor.GRAY);
		}
		
		manager.getStaffChatPMSC().send(pluginMessageSender,
				sender instanceof Player ? sender.getName() : M.CONSOLE_NAME,
				TextComponent.fromLegacyText(message, ChatColor.GRAY));
		
		/*String message = Emote.replaceEmotes(Joiner.on(' ').join(args), ChatColor.GRAY);
		String senderName = sender instanceof Player ? sender.getName() : M.CONSOLE_NAME;
		Log.info("[StaffChat] " + senderName + ": " + message);
		for(Profile profile : manager.plugin.getProfileManager().getOnlineProfiles()){
			if(profile.check(Rank.MOD)){
				profile.sendMessage("§5[SC] " + sender.getName() + "§8:§7 " + message);
			}
		}*/
	}
	
}
