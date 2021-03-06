package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class LightningCommand extends Command<Creative,GameplayManager> {
	
	public static final Access COOLDOWN_BYPASS = Rank.HEAD_MOD;
	private final Cooldown cooldown;
	
	public LightningCommand(GameplayManager manager){
		super(manager, Rank.PREMIUM, "lightning", "strike", "smite");
		cooldown = new Cooldown(2000, M.error("Please wait %t% to strike lightning again."));
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(!COOLDOWN_BYPASS.has(profile) && cooldown.use(profile)){
			return;
		}
		if(args.length < 1){
			Block targetBlock = profile.getPlayer().getTargetBlock(null, 128);
			targetBlock.getWorld().strikeLightningEffect(targetBlock.getLocation());
			return;
		}
		serverPerform(profile.getPlayer(), args, label);
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
		String targetName = target == null ? "everyone" : target.getName();
		if(target == null){
			for(Player player : Bukkit.getOnlinePlayers()){
				player.getWorld().strikeLightningEffect(player.getLocation());
			}
		}else{
			target.getWorld().strikeLightningEffect(target.getLocation());
		}
		sender.sendMessage(M.msg + "Smiting " + M.elm + targetName + M.msg + ".");
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
