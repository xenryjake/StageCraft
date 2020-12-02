package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class LockoutCommand extends Command<Creative,GameplayManager> {
	
	public static final Access ACCESS = Rank.MOD;
	
	private Rank rank;
	
	public LockoutCommand(GameplayManager manager){
		super(manager, ACCESS, "lockout");
		setCanBeDisabled(true);
		rank = null;
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		if(args.length < 1){
			enable(player, profile.getRank());
			return;
		}
		switch(args[0].toLowerCase()){
			case "on":
			case "en":
			case "enable":
				enable(player, profile.getRank());
				return;
			case "off":
			case "dis":
			case "disable":
				disable(player, profile.getRank());
				return;
			default:
				player.sendMessage(M.error("Invalid state. Please specify enable or disable."));
		}
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			enable(sender, Rank.ADMIN);
			return;
		}
		switch(args[0].toLowerCase()){
			case "on":
			case "en":
			case "enable":
				enable(sender, Rank.ADMIN);
				return;
			case "off":
			case "dis":
			case "disable":
				disable(sender, Rank.ADMIN);
				return;
			default:
				sender.sendMessage(M.error("Invalid state. Please specify enable or disable."));
		}
	}
	
	private void enable(CommandSender sender, Rank rank){
		if(manager.isLockoutMode()){
			sender.sendMessage(M.error("Lockout mode is already enabled."));
			return;
		}
		this.rank = rank;
		String name = sender instanceof Player ? sender.getName() : M.CONSOLE_NAME;
		manager.setLockoutMode(true, name);
	}
	
	private void disable(CommandSender sender, Rank rank){
		if(!manager.isLockoutMode()){
			sender.sendMessage(M.error("Lockout mode is already disabled."));
			return;
		}
		if(!rank.check(this.rank)){
			sender.sendMessage(M.error("The person who enabled the lockout has a higher rank than you."));
			return;
		}
		String name = sender instanceof Player ? sender.getName() : M.CONSOLE_NAME;
		manager.setLockoutMode(false, name);
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("enable", "disable") : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("enable", "disable") : Collections.emptyList();
	}
	
}
