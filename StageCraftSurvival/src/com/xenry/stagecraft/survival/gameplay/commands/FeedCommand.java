package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class FeedCommand extends Command<Survival,GameplayManager> {
	
	private static final List<Integer> potionEffectTypesToRemove = Collections.singletonList(17);
	
	public FeedCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "feed", "eat");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Player target = null;
		if(!args[0].equals("**")){
			target = Bukkit.getPlayer(args[0]);
			if(target == null){
				sender.sendMessage(M.playerNotFound(args[0]));
				return;
			}
		}
		feedPlayer(target, sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			feedPlayer(profile.getPlayer(), profile.getPlayer());
			return;
		}
		serverPerform(profile.getPlayer(), args, label);
	}
	
	private void feedPlayer(Player target, CommandSender feeder){
		String targetName;
		List<Player> targets;
		if(target == null){
			targets = new ArrayList<>(Bukkit.getOnlinePlayers());
			targetName = "everyone";
		}else{
			targets = Collections.singletonList(target);
			targetName = target.getName();
		}
		for(Player player : targets){
			final FoodLevelChangeEvent flce = new FoodLevelChangeEvent(player, 20);
			manager.plugin.getServer().getPluginManager().callEvent(flce);
			if (flce.isCancelled()) {
				feeder.sendMessage(M.error("Unable to feed " + M.elm + player.getName() + M.err + "."));
				continue;
			}
			
			player.setFoodLevel(Math.min(flce.getFoodLevel(), 20));
			player.setSaturation(player.getFoodLevel());
			player.setExhaustion(0);
			for(PotionEffect effect : player.getActivePotionEffects()){
				//noinspection deprecation
				if(potionEffectTypesToRemove.contains(effect.getType().getId())){
					player.removePotionEffect(effect.getType());
				}
			}
			player.sendMessage(M.msg + "You have been fed.");
		}
		if(feeder != target){
			feeder.sendMessage(M.msg + "You have fed " + M.elm + targetName + M.msg + ".");
		}
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
