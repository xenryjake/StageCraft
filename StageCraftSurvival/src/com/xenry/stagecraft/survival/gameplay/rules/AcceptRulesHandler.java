package com.xenry.stagecraft.survival.gameplay.rules;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.profile.SurvivalProfile;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class AcceptRulesHandler extends Handler<Survival,GameplayManager> {
	
	private final Cooldown cooldown;
	
	public AcceptRulesHandler(GameplayManager manager){
		super(manager);
		cooldown = new Cooldown(10000, null);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event){
		SurvivalProfile profile = manager.plugin.getSurvivalProfileManager().getProfile(event.getPlayer());
		if(profile != null && !profile.hasAcceptedRules()){
			sendAcceptRulesMessage(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event){
		SurvivalProfile profile = manager.plugin.getSurvivalProfileManager().getProfile(event.getPlayer());
		if(profile != null && !profile.hasAcceptedRules()){
			sendAcceptRulesMessage(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onHangingPlace(HangingPlaceEvent event){
		Player player = event.getPlayer();
		if(player == null){
			return;
		}
		SurvivalProfile profile = manager.plugin.getSurvivalProfileManager().getProfile(player);
		if(profile != null && !profile.hasAcceptedRules()){
			sendAcceptRulesMessage(player);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHangingBreak(HangingBreakByEntityEvent event){
		Entity entity = event.getEntity();
		if(!(entity instanceof Player)){
			return;
		}
		Player player = (Player)entity;
		SurvivalProfile profile = manager.plugin.getSurvivalProfileManager().getProfile(player);
		if(profile != null && !profile.hasAcceptedRules()){
			sendAcceptRulesMessage(player);
			event.setCancelled(true);
		}
	}
	
	public void sendAcceptRulesMessage(Player player){
		if(!cooldown.use(player, false)){
			return;
		}
		player.sendMessage(M.elm + M.BOLD + "You must accept the rules before playing.");
		player.sendMessage(M.msg + "View the " + M.SERVER_NAME_FORMATTED + M.msg + " server rules at " + M.elm + "town hall" + M.msg + " or at " + M.elm + M.BOLD + "http://mc.xenry.com/rules");
	}
	
}
