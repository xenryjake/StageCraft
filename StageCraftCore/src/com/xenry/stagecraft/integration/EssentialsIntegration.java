package com.xenry.stagecraft.integration;
import com.earth2me.essentials.Essentials;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import net.ess3.api.IUser;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 3/3/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EssentialsIntegration extends Handler<Core,IntegrationManager> {
	
	public final Essentials essentials;
	
	public EssentialsIntegration(IntegrationManager manager, Essentials essentials) {
		super(manager);
		this.essentials = essentials;
	}
	
	public boolean isAFK(Player player){
		IUser user = essentials.getUser(player);
		return user != null && user.isAfk();
	}
	
	public boolean isVanished(Player player){
		IUser user = essentials.getUser(player);
		return user != null && user.isAfk();
	}
	
	@EventHandler
	public void on(AfkStatusChangeEvent event){
		Player player = event.getAffected().getBase();
		if(player == null){
			return;
		}
		Profile profile = manager.plugin.getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		manager.plugin.getServer().getPluginManager().callEvent(new AFKChangeEvent(event.isAsynchronous(), profile,
				event.getValue()));
	}
	
	@EventHandler
	public void on(VanishStatusChangeEvent event){
		Player player = event.getAffected().getBase();
		if(player == null){
			return;
		}
		Profile profile = manager.plugin.getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		manager.plugin.getServer().getPluginManager().callEvent(new VanishChangeEvent(event.isAsynchronous(), profile,
				event.getValue()));
	}
	
}
