package com.xenry.stagecraft.survival.gameplay.rules;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

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
	
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		Profile profile = getCore().getProfileManager().getProfile(event.getPlayer());
		if(profile != null && !profile.hasAcceptedRules()){
			sendAcceptRulesMessage(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		Profile profile = getCore().getProfileManager().getProfile(event.getPlayer());
		if(profile != null && !profile.hasAcceptedRules()){
			sendAcceptRulesMessage(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	public void sendAcceptRulesMessage(Player player){
		if(!cooldown.use(player, false)){
			return;
		}
		player.sendMessage(M.elm + M.BOLD + "You must accept the rules before playing.");
		player.sendMessage(M.msg + "View the §a§lStage§9§lCraft" + M.msg + " server rules at " + M.elm + "town hall" + M.msg + " or at " + M.elm + M.BOLD + "http://mc.xenry.com/rules");
	}
	
}
