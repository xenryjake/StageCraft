package com.xenry.stagecraft.survival.hidenseek.map;
import com.xenry.stagecraft.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class Boundary {
	
	private String world;
	private int ax, ay, az, bx, by, bz;
	
	public Boundary(World world, int ax, int ay, int az, int bx, int by, int bz){
		this(world == null ? null : world.getName(), ax, ay, az, bx, by, bz);
	}
	
	public Boundary(String world, int ax, int ay, int az, int bx, int by, int bz) {
		this.world = world;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
		this.bx = bx;
		this.by = by;
		this.bz = bz;
	}
	
	public String getWorldName() {
		return world;
	}
	
	public World getWorld(){
		return Bukkit.getWorld(world);
	}
	
	public void setWorld(String world) {
		this.world = world;
	}
	
	public int getAX() {
		return ax;
	}
	
	public void setAX(int ax) {
		this.ax = ax;
	}
	
	public int getAY() {
		return ay;
	}
	
	public void setAY(int ay) {
		this.ay = ay;
	}
	
	public int getAZ() {
		return az;
	}
	
	public void setAZ(int az) {
		this.az = az;
	}
	
	public int getBX() {
		return bx;
	}
	
	public void setBX(int bx) {
		this.bx = bx;
	}
	
	public int getBY() {
		return by;
	}
	
	public void setBY(int by) {
		this.by = by;
	}
	
	public int getBZ() {
		return bz;
	}
	
	public void setBZ(int bz) {
		this.bz = bz;
	}
	
	public boolean isIn(String world, int x, int y, int z){
		if(!this.world.equalsIgnoreCase(world)){
			return false;
		}
		return NumberUtil.isIntWithin(x, ax, bx) && NumberUtil.isIntWithin(y, ay, by) && NumberUtil.isIntWithin(z, az, bz);
	}
	
	public boolean isIn(Location location){
		return isIn(location.getWorld() == null ? null : location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	public boolean isIn(HumanEntity player){
		return isIn(player.getLocation());
	}
	
	public boolean isIn(Block block){
		return isIn(block.getLocation());
	}
	
}
