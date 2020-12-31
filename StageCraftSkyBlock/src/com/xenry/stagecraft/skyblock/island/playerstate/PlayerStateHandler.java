package com.xenry.stagecraft.skyblock.island.playerstate;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.profile.GenericProfile;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.Island;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerStateHandler extends Handler<SkyBlock,IslandManager> {
	
	public PlayerStateHandler(IslandManager manager) {
		super(manager);
	}
	
	@Nullable
	public PlayerStateFile getFile(@NotNull Island island, @NotNull String uuid){
		try{
			return new PlayerStateFile(manager, island.getID(), uuid);
		}catch(Exception ex){
			return null;
		}
	}
	
	@Nullable
	public PlayerStateFile getFile(@NotNull Island island, @NotNull Player player){
		return getFile(island, player.getUniqueId().toString());
	}
	
	@Nullable
	public PlayerStateFile getFile(@NotNull Island island, @NotNull GenericProfile profile){
		return getFile(island, profile.getUUID());
	}
	
	@Nullable
	public PlayerState getState(@NotNull Island island, @NotNull String uuid){
		PlayerStateFile file = getFile(island, uuid);
		if(file == null){
			return null;
		}
		return file.toPlayerState();
	}
	
	@Nullable
	public PlayerState getState(@NotNull Island island, @NotNull Player player){
		return getState(island, player.getUniqueId().toString());
	}
	
	@Nullable
	public PlayerState getState(@NotNull Island island, @NotNull GenericProfile profile){
		return getState(island, profile.getUUID());
	}
	
}
