package com.xenry.stagecraft.bungee.proxy.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.bungee.commands.ProxyAdminCommand;
import com.xenry.stagecraft.bungee.proxy.ProxyManager;
import com.xenry.stagecraft.bungee.util.M;
import net.md_5.bungee.api.CommandSender;

import java.util.Arrays;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/9/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EndCommand extends ProxyAdminCommand<ProxyManager> {
	
	public EndCommand(ProxyManager manager) {
		super(manager, "end");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/end <seconds> [reason]"));
			return;
		}
		if(args[0].equalsIgnoreCase("cancel")){
			if(manager.getShutdownTask() == null){
				sender.sendMessage(M.error("A shutdown has not been initiated."));
				return;
			}
			sender.sendMessage(M.msg("Cancelling network shutdown..."));
			manager.cancelShutdown();
			return;
		}
		if(manager.getShutdownTask() != null){
			sender.sendMessage(M.error("A shutdown is already in progress."));
			return;
		}
		int seconds;
		try{
			seconds = Integer.parseInt(args[0]);
		}catch(Exception ex){
			sender.sendMessage(M.error("Invalid integer: " + args[0]));
			return;
		}
		if(seconds < 0){
			sender.sendMessage(M.error("Time cannot be negative."));
			return;
		}
		String reason = args.length <= 1 ? null : Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		manager.doShutdown(seconds, reason);
	}
	
}
