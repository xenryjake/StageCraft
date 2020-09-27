package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
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
		super(manager, Rank.ADMIN, "feed", "eat");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Player target;
		if(args[0].equals("**")){
			target = null;
		}else{
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
		final FoodLevelChangeEvent flce = new FoodLevelChangeEvent(target, 20);
		manager.plugin.getServer().getPluginManager().callEvent(flce);
		if (flce.isCancelled()) {
			feeder.sendMessage(M.error("Unable to feed " + M.elm + target.getName() + M.err + "."));
			return;
		}
		
		target.setFoodLevel(Math.min(flce.getFoodLevel(), 20));
		target.setSaturation(target.getFoodLevel());
		target.setExhaustion(0);
		for(PotionEffect effect : target.getActivePotionEffects()){
			//noinspection deprecation
			if(potionEffectTypesToRemove.contains(effect.getType().getId())){
				target.removePotionEffect(effect.getType());
			}
		}
		target.sendMessage(M.msg + "You have been fed.");
		if(feeder != target){
			feeder.sendMessage(M.msg + "You have fed " + M.elm + target.getName() + M.msg + ".");
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return args.length <= 1 ? null : Collections.emptyList();
	}
	
}
