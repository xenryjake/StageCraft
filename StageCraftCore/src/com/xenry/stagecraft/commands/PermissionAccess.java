package com.xenry.stagecraft.commands;
import com.xenry.stagecraft.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PermissionAccess implements Access {
	
	private final String permission;
	
	public PermissionAccess(String permission){
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
	@Override
	public boolean has(@NotNull Profile profile) {
		Player player = profile.getPlayer();
		if(player == null){
			return false;
		}
		return player.hasPermission(permission);
	}
	
	@Override
	public String toString() {
		return permission;
	}
	
	@Override
	public boolean has(@NotNull CommandSender sender) {
		return sender.hasPermission(permission);
	}
	
}
