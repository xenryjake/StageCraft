package com.xenry.stagecraft.skyblock.island;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.factory.parser.pattern.SingleBlockPatternParser;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.block.BlockReplace;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.operation.SetBlockMap;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.util.Log;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class SchematicHandler extends Handler<SkyBlock,IslandManager> {
	
	private Clipboard mainIslandClipboard;
	
	public SchematicHandler(IslandManager manager) {
		super(manager);
	}
	
	public void loadSchematics(){
		Log.debug("LOADING SCHEMATIC...");
		try{
			String relative = manager.plugin.getConfig().getString("schematics.main-island.file");
			File file = new File(manager.plugin.getDataFolder().getAbsolutePath() + "/" + relative);
			if(!file.exists()){
				Log.warn("main-island schematic file does not exist at: " + relative);
				return;
			}
			ClipboardFormat mainIslandFormat = ClipboardFormats.findByFile(file);
			if(mainIslandFormat == null){
				Log.warn("No ClipboardFormat available for main-island schematic");
				return;
			}
			ClipboardReader reader = mainIslandFormat.getReader(new FileInputStream(file));
			mainIslandClipboard = reader.read();
			if(mainIslandClipboard == null){
				Log.warn("Main island schematic clipboard is null...?");
			}
			Log.debug("SCHEMATIC LOAD SUCCESSFUL");
		}catch(Exception ex){
			Log.warn("Something went wrong when loading the main-island schematic!");
			ex.printStackTrace();
		}
	}
	
	public Clipboard getMainIslandClipboard() {
		return mainIslandClipboard;
	}
	
	public boolean pasteMainIsland(@NotNull World bukkitWorld, @NotNull Island island){
		int x, y, z;
		try{
			x = island.getActualX1() + manager.plugin.getConfig().getInt("schematics.main-island.x");
			y = manager.plugin.getConfig().getInt("schematics.main-island.y");
			z = island.getActualZ1() + manager.plugin.getConfig().getInt("schematics.main-island.z");
		}catch(Exception ex){
			Log.warn("Something went wrong when trying to determine where to paste main-island");
			ex.printStackTrace();
			return false;
		}
		return pasteMainIsland(bukkitWorld, x, y, z);
	}
	
	public boolean pasteMainIsland(@NotNull World bukkitWorld, int x, int y, int z){
		if(mainIslandClipboard == null){
			Log.warn("Tried to paste main island before schematic loaded!");
			return false;
		}
		try{
			Log.debug("PASTING SCHEMATIC... (" + x + "," + y + "," + z + ")");
			com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(bukkitWorld);
			EditSession session = WorldEdit.getInstance().newEditSession(world);
			Operation operation = new ClipboardHolder(mainIslandClipboard).createPaste(session).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(true).build();
			Operations.complete(operation);
			session.close();
			Log.debug("PASTE SUCCESSFUL");
			return true;
		}catch(Exception ex){
			Log.warn("Something went wrong when trying to paste main-island at " + x + "," + y + "," + z);
			return false;
		}
	}
	
	public boolean clearRegion(@NotNull World bukkitWorld, @NotNull Island island){
		return clearRegion(bukkitWorld, island.getActualX1(), island.getActualZ1(), island.getActualX2(), island.getActualZ2());
	}
	
	public boolean clearRegion(@NotNull World bukkitWorld, int x1, int z1, int x2, int z2){
		try{
			Log.debug("CLEARING REGION... (" + x1 + "," + z1 + ") -> (" + x2 + "," + z2 + ")");
			BlockVector3 b1 = BlockVector3.at(x1, 0, z1);
			BlockVector3 b2 = BlockVector3.at(x2, bukkitWorld.getMaxHeight(), z2);
			CuboidRegion region = new CuboidRegion(b1, b2);
			com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(bukkitWorld);
			EditSession session = WorldEdit.getInstance().newEditSession(world);
			BlockType type = BlockTypes.AIR;
			if(type == null){
				Log.warn("air doesn't exist????");
				return false;
			}
			BlockState state = type.getDefaultState();
			BaseBlock pattern = state.toBaseBlock();
			RegionFunction function = new BlockReplace(session, pattern);
			RegionVisitor operation = new RegionVisitor(region, function);
			Operations.complete(operation);
			session.close();
			Log.debug("CLEAR SUCCESSFUL");
			return true;
		}catch(Exception ex){
			Log.warn("Something went wrong when trying to clear a region: (" + x1 + "," + z1 + ") -> (" + x2 + "," + z2 + ")");
			ex.printStackTrace();
			return false;
		}
	}
	
}
