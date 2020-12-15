package com.xenry.stagecraft.command.commands;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.command.CommandManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DiceRollCommand extends Command<Core,CommandManager> {
	
	public static final HashMap<Integer,String> DICE = new HashMap<>();
	
	static {
		DICE.put(1, "⚀");
		DICE.put(2, "⚁");
		DICE.put(3, "⚂");
		DICE.put(4, "⚃");
		DICE.put(5, "⚄");
		DICE.put(6, "⚅");
	}
	
	public DiceRollCommand(CommandManager manager){
		super(manager, Rank.MEMBER, "diceroll", "dieroll", "rolldice", "rolldie");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		int dice = 1;
		if(args.length > 0){
			try{
				dice = Integer.parseInt(args[0]);
			}catch(Exception ex){
				sender.sendMessage(M.error("Invalid integer: " + args[0]));
				return;
			}
		}
		if(dice < 1){
			sender.sendMessage(M.error("You cannot roll less than 1 die."));
			return;
		}
		if(dice > 16){
			sender.sendMessage(M.error("You cannot roll more than 16 dice."));
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < dice; i++) {
			int value = manager.plugin.getRandom().nextInt(6) + 1;
			sb.append(M.WHITE).append(DICE.get(value)).append(M.gry).append(" (").append(value).append(")").append(M.DGRAY).append(", ");
		}
		String message = sb.toString().trim();
		if(message.endsWith(",")){
			message = message.substring(0, message.length() - 1);
		}
		sender.sendMessage(M.msg + "You rolled " + M.elm + dice + M.msg + (dice == 1 ? " die:" : " dice:"));
		sender.sendMessage(M.arrow(message));
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
}
