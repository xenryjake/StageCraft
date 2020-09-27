package com.xenry.stagecraft.util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class LocationUtil {
	
	private LocationUtil(){}
	
	public static final int SAFE_LOCATION_RADIUS = 3;
	public static final Vector3D[] SAFE_LOCATION_VOLUME;
	
	public static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();
	public static final Set<Material> TRANSPARENT_MATERIALS = new HashSet<>();
	private static final List<Material> SPECIFIC_BAD_MATERIALS = Arrays.asList(
			Material.LAVA, Material.FIRE, Material.END_PORTAL, Material.NETHER_PORTAL,
			Material.BLACK_BED, Material.RED_BED, Material.GREEN_BED, Material.BROWN_BED,
			Material.BLUE_BED, Material.PURPLE_BED, Material.CYAN_BED, Material.LIGHT_GRAY_BED,
			Material.GRAY_BED, Material.PINK_BED, Material.LIME_BED, Material.YELLOW_BED,
			Material.LIGHT_BLUE_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.WHITE_BED
	);
	
	static {
		for (Material mat : Material.values()) {
			//noinspection deprecation
			if (mat.isTransparent()) {
				HOLLOW_MATERIALS.add(mat);
			}
		}
		//HOLLOW_MATERIALS.add(Material.WATER);
		TRANSPARENT_MATERIALS.addAll(HOLLOW_MATERIALS);
		
		List<Vector3D> pos = new ArrayList<>();
		for (int x = -SAFE_LOCATION_RADIUS; x <= SAFE_LOCATION_RADIUS; x++) {
			for (int y = -SAFE_LOCATION_RADIUS; y <= SAFE_LOCATION_RADIUS; y++) {
				for (int z = -SAFE_LOCATION_RADIUS; z <= SAFE_LOCATION_RADIUS; z++) {
					pos.add(new Vector3D(x, y, z));
				}
			}
		}
		pos.sort(Comparator.comparingInt(a -> (a.x * a.x + a.y * a.y + a.z * a.z)));
		SAFE_LOCATION_VOLUME = pos.toArray(new Vector3D[0]);
	}
	
	public static boolean isBlockUnsafe(Location location, Player player){
		if(location.getWorld() == player.getWorld() && (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) && player.getAllowFlight()){
			return false;
		}
		return isBlockUnsafe(location.getBlock());
	}
	
	public static boolean isBlockUnsafe(Block block, Player player){
		if(block.getWorld() == player.getWorld() && (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) && player.getAllowFlight()){
			return false;
		}
		return isBlockUnsafe(block);
	}
	
	public static boolean isBlockUnsafe(Location location){
		Block block = location.getBlock();
		return isBlockDamaging(block) || isBlockAboveAir(block);
	}
	
	public static boolean isBlockUnsafe(Block block){
		return isBlockDamaging(block) || isBlockAboveAir(block);
	}
	
	public static boolean isBlockAboveAir(Block block){
		return block.getY() > block.getWorld().getMaxHeight() || HOLLOW_MATERIALS.contains(block.getRelative(BlockFace.DOWN).getType());
	}
	
	public static boolean isBlockDamaging(Block block){
		return SPECIFIC_BAD_MATERIALS.contains(block.getRelative(BlockFace.DOWN).getType()) || (!HOLLOW_MATERIALS.contains(block.getType())) || (!HOLLOW_MATERIALS.contains(block.getRelative(BlockFace.UP).getType()));
	}
	
	public static Location center(Location loc) {
		return new Location(loc.getWorld(), loc.getBlockX() + 0.5, (int)Math.round(loc.getY()), loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
	}
	
	public static Location getSafeTeleportDestination(Location loc, Player player){
		if(loc.getWorld() == player.getWorld() && (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) && player.getAllowFlight()){
			return center(loc);
		}
		return getSafeTeleportDestination(loc);
	}
	
	public static Location getSafeTeleportDestination(Location loc) {
		if (loc == null || loc.getWorld() == null) {
			throw new IllegalArgumentException("Location or Location#getWorld cannot be null");
		}
		final World world = loc.getWorld();
		int x = loc.getBlockX();
		int y = (int) Math.round(loc.getY());
		int z = loc.getBlockZ();
		final int origX = x;
		final int origY = y;
		final int origZ = z;
		
		while (isBlockAboveAir(world.getBlockAt(x, y, z))) {
			y -= 1;
			if (y < 0) {
				y = origY;
				break;
			}
		}
		
		if (isBlockUnsafe(world.getBlockAt(x, y, z))) {
			x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
			z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
		}
		
		int i = 0;
		while (isBlockUnsafe(world.getBlockAt(x, y, z))) {
			i++;
			if (i >= SAFE_LOCATION_VOLUME.length) {
				x = origX;
				y = origY + SAFE_LOCATION_RADIUS;
				z = origZ;
				break;
			}
			x = origX + SAFE_LOCATION_VOLUME[i].x;
			y = origY + SAFE_LOCATION_VOLUME[i].y;
			z = origZ + SAFE_LOCATION_VOLUME[i].z;
		}
		
		while (isBlockUnsafe(world.getBlockAt(x, y, z))) {
			y += 1;
			if (y >= world.getMaxHeight()) {
				x += 1;
				break;
			}
		}
		
		while (isBlockUnsafe(world.getBlockAt(x, y, z))) {
			y -= 1;
			if (y <= 1) {
				x += 1;
				y = world.getHighestBlockYAt(x, z);
				if (x - 48 > loc.getBlockX()) {
					return null;
				}
			}
		}
		
		return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
	}
	
	public static boolean shouldFlyOnTeleport(Location loc) {
		World world = loc.getWorld();
		if(world == null){
			throw new IllegalArgumentException("Location#getWorld cannot be null");
		}
		final int x = loc.getBlockX();
		int y = (int) Math.round(loc.getY());
		final int z = loc.getBlockZ();
		int count = 0;
		while (isBlockUnsafe(world.getBlockAt(x, y, z)) && y > -1) {
			y--;
			count++;
			if (count > 2) {
				return true;
			}
		}
		return y < 0;
	}
	
	public static List<String> getAllWorldNames(){
		List<String> names = new ArrayList<>();
		for(World world : Bukkit.getWorlds()){
			names.add(world.getName());
		}
		return names;
	}
	
}
