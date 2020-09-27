package com.xenry.stagecraft.skyblock;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.StageCraftPlugin;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * SCSkyBlock created by Henry Blasingame (Xenry) on 9/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SkyBlock extends StageCraftPlugin {
	
	public SkyBlock(){
		super("SkyBlock", Core.getInstance());
	}
	
	@Override
	public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
		return new VoidGenerator();
	}
	
}
