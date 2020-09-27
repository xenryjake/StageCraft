package com.xenry.stagecraft.survival.gameplay.villagers;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class VillagersHandler extends Handler<Survival,GameplayManager> {
	
	private final List<String> villagerDamageEnabled;
	private final Cooldown cooldown;
	
	public VillagersHandler(GameplayManager manager){
		super(manager);
		villagerDamageEnabled = new ArrayList<>();
		cooldown = new Cooldown(10000, null);
	}
	
	public boolean hasVillagerDamageEnabled(Player player){
		return villagerDamageEnabled.contains(player.getUniqueId().toString());
	}
	
	public void setVillagerDamageEnabled(Player player, boolean enabled){
		if(enabled){
			if(!villagerDamageEnabled.contains(player.getUniqueId().toString())){
				villagerDamageEnabled.add(player.getUniqueId().toString());
			}
		}else{
			villagerDamageEnabled.remove(player.getUniqueId().toString());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		Entity damager = event.getDamager();
		if(!(damager instanceof Player) || !(event.getEntity() instanceof Villager)){
			return;
		}
		Player player = (Player)damager;
		if(!hasVillagerDamageEnabled(player)){
			sendNoDamageVillagerMessage(player);
			event.setCancelled(true);
		}
	}
	
	public void sendNoDamageVillagerMessage(Player player){
		if(cooldown.use(player, false)) {
			player.sendMessage(M.err + "Warning!" + M.msg + " Killing villagers is against server rules. If you still need to kill a villager, enable villager damage by typing " + M.elm + "/villagerdamage on");
		}
	}
	
}
