package com.xenry.stagecraft.survival.gameplay.villagers;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class VillagersDamageCommand extends Command<Survival,GameplayManager> {
	
	public VillagersDamageCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "villagersdamage", "villagerdamage", "vd");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		VillagersHandler vh = manager.getVillagersHandler();
		boolean status = vh.hasVillagerDamageEnabled(player);
		if(args.length < 1){
			profile.sendMessage(M.msg + "You currently have villager damage " + (status ? "§aenabled" : "§cdisabled") + M.msg + ".");
			return;
		}
		boolean enable;
		args[0] = args[0].toLowerCase();
		if(args[0].startsWith("e") || args[0].startsWith("on")){
			enable = true;
		}else if(args[0].startsWith("d") || args[0].startsWith("off")){
			enable = false;
		}else if(args[0].startsWith("t")){
			enable = !status;
		}else{
			profile.sendMessage(M.error("Invalid argument: " + args[0]));
			return;
		}
		if(status == enable){
			profile.sendMessage(M.error("Your villager damage is already " + (status ? "enabled" : "disabled") + "."));
			return;
		}
		vh.setVillagerDamageEnabled(player, enable);
		profile.sendMessage(M.msg + "Villager damage " + (enable ? "§aenabled" : "§cdisabled") + M.msg + ".");
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Arrays.asList("enable", "disable");
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Arrays.asList("enable", "disable");
	}
	
}