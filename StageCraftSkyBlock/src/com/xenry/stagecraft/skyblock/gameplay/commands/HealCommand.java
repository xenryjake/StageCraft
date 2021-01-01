package com.xenry.stagecraft.skyblock.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public final class HealCommand extends Command<SkyBlock,GameplayManager> {
	
	private static final List<Integer> potionEffectTypesToRemove = Arrays.asList(7, 17, 19, 20);
	
	public HealCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "heal");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.usage("/" + label + " <player>"));
			return;
		}
		Player player = null;
		if(!args[0].equals("**")){
			player = Bukkit.getPlayer(args[0]);
			if(player == null){
				sender.sendMessage(M.playerNotFound(args[0]));
				return;
			}
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
			AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
			if(healthAttribute == null){
				healer.sendMessage(M.error("Something went wrong."));
				continue;
			}
			
			final EntityRegainHealthEvent erhe = new EntityRegainHealthEvent(player,
					healthAttribute.getValue() - player.getHealth(), EntityRegainHealthEvent.RegainReason.CUSTOM);
			manager.plugin.getServer().getPluginManager().callEvent(erhe);
			if (erhe.isCancelled()) {
				healer.sendMessage(M.error("Unable to heal."));
				continue;
			}
			
			player.setHealth(healthAttribute.getValue());
			player.setFoodLevel(20);
			player.setFireTicks(0);
			player.setRemainingAir(player.getMaximumAir());
			for(PotionEffect effect : player.getActivePotionEffects()){
				//noinspection deprecation
				if(potionEffectTypesToRemove.contains(effect.getType().getId())){
					player.removePotionEffect(effect.getType());
				}
			}
			player.sendMessage(M.msg + "You have been healed.");
		}
		if(healer != target){
			healer.sendMessage(M.msg + "You have healed " + M.elm + targetName + M.msg + ".");
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
