package com.xenry.stagecraft.profile.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.server.PlayerState;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.TimeUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.xenry.stagecraft.profile.commands.LookupCommand.SENSITIVE_VIEW_ACCESS;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SeenCommand extends Command<Core,ProfileManager> {
	
	public static final Access DETAILED_VIEW_ACCESS = Rank.MOD;
	
	public SeenCommand(ProfileManager manager){
		super(manager, Rank.MEMBER, "seen");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		doCommand(profile.getPlayer(), args, label, DETAILED_VIEW_ACCESS.has(profile), SENSITIVE_VIEW_ACCESS.has(profile));
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		doCommand(sender, args, label, true, true);
	}
	
	private void doCommand(CommandSender sender, String[] args, String label, boolean detailedViewAccess, boolean sensitiveViewAccess){
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Profile target;
		if(args[0].length() <= 17){
			if(Bukkit.getPlayer(args[0]) != null){
				target = manager.getProfile(Bukkit.getPlayer(args[0]));
			}else{
				target = manager.getProfileByLatestUsername(args[0]);
			}
		}else{
			target = manager.getProfileByUUID(args[0]);
		}
		if(target == null){
			sender.sendMessage(M.error("There is no profile for that player."));
			return;
		}
		
		PlayerState state = manager.plugin.getServerManager().getPlayerState(target.getUUID());
		boolean onlineLocal = target.isOnline();
		boolean onlineGlobal = state != null;
		
		ComponentBuilder cb = new ComponentBuilder(target.getLatestUsername()).color(M.elm).append(" is ").color(M.msg);
		if(onlineGlobal){
			cb.append("online").color(ChatColor.GREEN);
			cb.append(" at ").color(M.msg);
			cb.append(state.getServerName()).color(M.elm);
			if(onlineLocal){
				long time = target.getSecondsSinceLastLogin(manager.plugin.getServerName());
				cb.append(" (").color(M.msg).append(TimeUtil.simplerString(time)).color(M.elm).append(")").color(M.msg);
			}else{
				cb.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + state.getServerName()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go to " + state.getServerName())));
			}
		}else{
			cb.append("offline").color(ChatColor.RED);
			long time = target.getMostRecentLogout();
			cb.append(" (").color(M.msg).append(TimeUtil.simplerString(time)).color(M.elm).append(")").color(M.msg);
		}
		sender.sendMessage(cb.create());
		if(sensitiveViewAccess){
			sender.sendMessage(M.arrow("Latest address: " + M.WHITE + target.getLatestAddress()));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? networkPlayers(args[0]) : Collections.emptyList();
	}
	
}
