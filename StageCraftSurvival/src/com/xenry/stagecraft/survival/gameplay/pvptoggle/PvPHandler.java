package com.xenry.stagecraft.survival.gameplay.pvptoggle;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Setting;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Iterator;

import static com.xenry.stagecraft.survival.gameplay.pvptoggle.PvPLock.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PvPHandler extends Handler<Survival,GameplayManager> {
	
	private final Cooldown cooldown;
	private PvPLock lockState;
	
	public PvPHandler(GameplayManager manager){
		super(manager);
		cooldown = new Cooldown(2000, null);
		lockState = NONE;
	}
	
	public PvPLock getLockState() {
		return lockState;
	}
	
	public void setLockState(PvPLock lockState) {
		this.lockState = lockState;
	}
	
	public boolean isPvPEnabled(Profile profile){
		if(lockState == LOCK_ON){
			return true;
		}
		if(lockState == LOCK_OFF){
			return false;
		}
		return profile.getSetting(Setting.SURVIVAL_PVP_ENABLED)
				|| manager.plugin.getJailManager().getOutstandingSentence(profile) != null;
	}
	
	public boolean isPvPEnabled(Player player){
		if(lockState == LOCK_ON){
			return true;
		}
		if(lockState == LOCK_OFF){
			return false;
		}
		Profile profile = getCore().getProfileManager().getProfile(player);
		if(profile == null){
			return Setting.SURVIVAL_PVP_ENABLED.getDefaultValue();
		}
		return isPvPEnabled(profile);
	}
	
	public void setPvPEnabled(Profile profile, boolean enabled){
		profile.setSetting(Setting.SURVIVAL_PVP_ENABLED, enabled);
	}
	
	public void setPvPEnabled(Player player, boolean enabled){
		Profile profile = getCore().getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		setPvPEnabled(profile, enabled);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onDamageByEntity(EntityDamageByEntityEvent event){
		if(!(event.getEntity() instanceof Player)){
			return;
		}
		Player damagee = (Player)event.getEntity();
		Player damager;
		if(event.getDamager() instanceof Player){
			damager = (Player)event.getDamager();
		}else if(event.getDamager() instanceof Projectile){
			Projectile projectile = (Projectile)event.getDamager();
			if(!(projectile.getShooter() instanceof Player)){
				return;
			}
			damager = (Player)projectile.getShooter();
		}else if(event.getDamager() instanceof Firework){
			damager = null;
		}else if(event.getDamager() instanceof LightningStrike
				&& event.getDamager().getMetadata("pvpTrident").size() > 0){
			damager = null;
		}else{
			return;
		}
		if(damager == damagee){
			return;
		}
		
		//this can handle damager=null
		Profile damagerProfile = getCore().getProfileManager().getProfile(damager);
		if(damagerProfile != null && !isPvPEnabled(damagerProfile)){
			sendPvPDisabledMessage(damager);
			event.setCancelled(true);
			return;
		}
		Profile damageeProfile = getCore().getProfileManager().getProfile(damagee);
		if(damageeProfile != null && !isPvPEnabled(damageeProfile)){
			if(damager != null){
				sendPvPDisabledMessage(damager, damagee);
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onLightningStrike(LightningStrikeEvent event){
		if(event.getCause() == LightningStrikeEvent.Cause.TRIDENT){
			event.getLightning().setMetadata("pvpTrident", new FixedMetadataValue(getCore(),
					event.getLightning().getLocation()));
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onCombust(EntityCombustByEntityEvent event){
		if(!(event.getEntity() instanceof Player)){
			return;
		}
		Player damagee = (Player)event.getEntity();
		if(!(event.getCombuster() instanceof Arrow) || !(((Arrow)event.getCombuster()).getShooter() instanceof Player)){
			return;
		}
		Player damager = (Player)((Arrow)event.getCombuster()).getShooter();
		if(damager == damagee){
			return;
		}
		
		Profile damagerProfile = getCore().getProfileManager().getProfile(damager);
		if(damagerProfile != null && !isPvPEnabled(damagerProfile)){
			sendPvPDisabledMessage(damager);
			event.setCancelled(true);
			return;
		}
		Profile damageeProfile = getCore().getProfileManager().getProfile(damagee);
		if(damageeProfile != null && !isPvPEnabled(damageeProfile)){
			if(damager != null){
				sendPvPDisabledMessage(damager, damagee);
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event){
		if(!(event.getEntity().getShooter() instanceof Player)){
			return;
		}
		Player damager = (Player)event.getEntity().getShooter();
		boolean damagerPvPEnabled = true;
		Profile damagerProfile = getCore().getProfileManager().getProfile(damager);
		if(damagerProfile != null){
			damagerPvPEnabled = isPvPEnabled(damagerProfile);
		}
		for(LivingEntity entity : event.getAffectedEntities()){
			if(!(entity instanceof Player) || damager == entity){
				continue;
			}
			Player damagee = (Player)entity;
			boolean damageePvPEnabled = true;
			Profile damageeProfile = getCore().getProfileManager().getProfile(damagee);
			if(damageeProfile != null){
				damageePvPEnabled = isPvPEnabled(damageeProfile);
			}
			if(!damagerPvPEnabled){
				event.setIntensity(entity, 0);
				sendPvPDisabledMessage(damager);
			}else if(!damageePvPEnabled){
				event.setIntensity(entity, 0);
				sendPvPDisabledMessage(damager, damagee);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onAreaEffectCloud(AreaEffectCloudApplyEvent event){
		if(!(event.getEntity().getSource() instanceof Player)){
			return;
		}
		Player damager = (Player)event.getEntity();
		boolean damagerPvPEnabled = true;
		Profile damagerProfile = getCore().getProfileManager().getProfile(damager);
		if(damagerProfile != null){
			damagerPvPEnabled = isPvPEnabled(damagerProfile);
		}
		Iterator<LivingEntity> affected = event.getAffectedEntities().iterator();
		while(affected.hasNext()){
			LivingEntity entity = affected.next();
			if(!(entity instanceof Player) || damager == entity){
				continue;
			}
			Player damagee = (Player)entity;
			boolean damageePvPEnabled = true;
			Profile damageeProfile = getCore().getProfileManager().getProfile(damagee);
			if(damageeProfile != null){
				damageePvPEnabled = isPvPEnabled(damageeProfile);
			}
			if(!damagerPvPEnabled || !damageePvPEnabled){
				affected.remove();
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onFish(PlayerFishEvent event){
		if(!(event.getCaught() instanceof Player)){
			return;
		}
		Player damager = event.getPlayer();
		boolean damagerPvPEnabled = true;
		Profile damagerProfile = getCore().getProfileManager().getProfile(damager);
		if(damagerProfile != null){
			damagerPvPEnabled = isPvPEnabled(damagerProfile);
		}
		Player damagee = (Player)event.getCaught();
		boolean damageePvPEnabled = true;
		Profile damageeProfile = getCore().getProfileManager().getProfile(damagee);
		if(damageeProfile != null){
			damageePvPEnabled = isPvPEnabled(damageeProfile);
		}
		if(!damagerPvPEnabled){
			event.setCancelled(true);
			sendPvPDisabledMessage(damager);
		}else if(!damageePvPEnabled){
			event.setCancelled(true);
			sendPvPDisabledMessage(damager, damagee);
		}
	}
	
	public void sendPvPDisabledMessage(Player player, Player damagee) {
		if(!cooldown.use(player, false)){
			return;
		}
		if(damagee == null) {
			player.sendMessage(M.err + "You have PVP disabled.");
		} else {
			player.sendMessage(M.err + damagee.getName() + " has PvP disabled.");
		}
	}
	
	public void sendPvPDisabledMessage(Player player){
		sendPvPDisabledMessage(player, null);
	}
	
}
