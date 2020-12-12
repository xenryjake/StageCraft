package com.xenry.stagecraft.profile.permissions.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.permissions.PermissionSet;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PermissionSetViewCommand extends Command<Core,ProfileManager> {
	
	public PermissionSetViewCommand(ProfileManager manager){
		super(manager, Rank.ADMIN, "view", "info");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/perm set view <set-name>"));
			return;
		}
		PermissionSet set = manager.getPermissionsHandler().getPermissionSet(args[0]);
		if(set == null){
			sender.sendMessage(M.error("There is no permission set named " + args[0] + "."));
			return;
		}
		
		ComponentBuilder cb = new ComponentBuilder(" » ").color(ChatColor.DARK_GRAY).bold(true);
		Iterator<Map.Entry<String,Boolean>> it = set.getPermissions().entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,Boolean> entry = it.next();
			BaseComponent[] hoverText = new ComponentBuilder(entry.getKey()).color(ChatColor.WHITE)
					.append(": ").color(ChatColor.DARK_GRAY)
					.append(entry.getValue().toString()).color(entry.getValue() ? ChatColor.GREEN : ChatColor.RED)
					.create();
			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText));
			cb.append(entry.getKey()).color(entry.getValue() ? ChatColor.WHITE : ChatColor.GRAY).event(he).bold(false);
			if(it.hasNext()){
				cb.append(", ").color(ChatColor.DARK_GRAY).event((HoverEvent)null);
			}
		}
		
		sender.sendMessage(M.msg + "Viewing permission set " + M.elm + set.name + M.msg + ":");
		sender.sendMessage(M.arrow("Assigned to: " + M.WHITE + set.getAccessName()));
		sender.sendMessage(cb.create());
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(manager.getPermissionsHandler().getPermissionSetNames(), args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? filter(manager.getPermissionsHandler().getPermissionSetNames(), args[0]) : Collections.emptyList();
	}
	
}
