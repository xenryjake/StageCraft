package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HealCommand extends Command<Survival,GameplayManager> {
	
	//todo add ** selector
	
	private static final List<Integer> potionEffectTypesToRemove = Arrays.asList(7, 17, 19, 20);
	
	public HealCommand(GameplayManager manager){
		super(manager, Rank.ADMIN, "heal");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null){
			sender.sendMessage(M.playerNotFound(args[0]));
			return;
		}
		healPlayer(player, sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 1){
			healPlayer(profile.getPlayer(), profile.getPlayer());
			return;
		}
		serverPerform(profile.getPlayer(), args, label);
	}
	
	private void healPlayer(Player target, CommandSender healer){
		AttributeInstance healthAttribute = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if(healthAttribute == null){
			healer.sendMessage(M.error("Something went wrong."));
			return;
		}
		
		final EntityRegainHealthEvent erhe = new EntityRegainHealthEvent(target,
				healthAttribute.getValue() - target.getHealth(), EntityRegainHealthEvent.RegainReason.CUSTOM);
		manager.plugin.getServer().getPluginManager().callEvent(erhe);
		if (erhe.isCancelled()) {
			healer.sendMessage(M.error("Unable to heal."));
			return;
		}
		
		target.setHealth(healthAttribute.getValue());
		target.setFoodLevel(20);
		target.setFireTicks(0);
		for(PotionEffect effect : target.getActivePotionEffects()){
			//noinspection deprecation
			if(potionEffectTypesToRemove.contains(effect.getType().getId())){
				target.removePotionEffect(effect.getType());
			}
		}
		target.sendMessage(M.msg + "You have been healed.");
		if(healer != target){
			healer.sendMessage(M.msg + "You have healed " + M.elm + target.getName() + M.msg + ".");
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
