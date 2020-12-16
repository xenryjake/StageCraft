package com.xenry.stagecraft.skyblock;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SkyBlockVoidGenerator extends JavaPlugin {
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
		return new VoidGenerator();
	}
	
	public static class VoidGenerator extends ChunkGenerator {
		
		@Override
		public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z,
													@NotNull BiomeGrid biome) {
			return createChunkData(world);
		}
		
		@Override
		public boolean canSpawn(@NotNull World world, int x, int z) {
			return true;
		}
		
	}
	
}
