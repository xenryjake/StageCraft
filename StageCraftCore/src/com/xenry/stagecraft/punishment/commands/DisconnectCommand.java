package com.xenry.stagecraft.punishment.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.punishment.PunishmentManager;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DisconnectCommand extends Command<Core,PunishmentManager> {
	
	private final Cooldown confirms;
	
	public DisconnectCommand(PunishmentManager manager){
		super(manager, Rank.ADMIN, "disconnect", "discon");
		confirms = new Cooldown(15000, null);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(@NotNull CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player> [message]"));
			return;
		}
		Player target = null;
		if(args[0].equals("**")){
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(confirms.canUse(player)){
					confirms.use(player);
					player.sendMessage(M.msg + "Are you sure you want to disconnect ALL PLAYERS? Type " + M.elm
							+ "/" + label + " **" + M.msg + " again to confirm.");
					return;
				}else{
					confirms.removeRecharge(player);
				}
			}
		}else{
			target = Bukkit.getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}
		String message = "Connection lost";
		if(args.length > 1){
			message = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		}
		message = ChatColor.translateAlternateColorCodes('&', message);
		String targetName = target == null ? "everyone" : target.getName();
		if(target == null){
			for(Player player : Bukkit.getOnlinePlayers()){
				if(player != sender){
					player.kickPlayer(message);
				}
			}
		}else{
			target.kickPlayer(message);
		}
		sender.sendMessage(M.msg + "You disconnected " + M.elm + targetName + M.msg + " with message " + M.elm + message + M.msg + ".");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0], "**") : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? localPlayers(args[0], "**") : Collections.emptyList();
	}
	
}
