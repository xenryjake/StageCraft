package com.xenry.stagecraft.util;
import org.bukkit.util.Vector;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum BlockRelative {
	
	//center (1)
	SELF(0,0,0),
	
	//face centers (6)
	UP(0,1,0),
	DOWN(0,-1,0),
	NORTH(0,0,-1),
	EAST(1,0,0),
	SOUTH(0,0,1),
	WEST(-1,0,0),
	
	//edge centers (12)
	UP_NORTH(UP,NORTH),
	UP_EAST(UP,EAST),
	UP_SOUTH(UP,SOUTH),
	UP_WEST(UP,WEST),
	NORTH_EAST(NORTH,EAST),
	NORTH_WEST(NORTH,WEST),
	SOUTH_EAST(SOUTH,EAST),
	SOUTH_WEST(SOUTH,WEST),
	DOWN_NORTH(DOWN,NORTH),
	DOWN_EAST(DOWN,EAST),
	DOWN_SOUTH(DOWN,SOUTH),
	DOWN_WEST(DOWN,WEST),
	
	//cube corners (8)
	UP_NORTH_WEST(UP,NORTH,WEST),
	UP_NORTH_EAST(UP,NORTH,EAST),
	UP_SOUTH_WEST(UP,SOUTH,WEST),
	UP_SOUTH_EAST(UP,SOUTH,EAST),
	DOWN_NORTH_WEST(DOWN,NORTH,WEST),
	DOWN_NORTH_EAST(DOWN,NORTH,EAST),
	DOWN_SOUTH_WEST(DOWN,SOUTH,WEST),
	DOWN_SOUTH_EAST(DOWN,SOUTH,EAST);
	
	public static final BlockRelative[] faces = new BlockRelative[]{UP,DOWN,NORTH,EAST,SOUTH,WEST};
	public static final BlockRelative[] edges = new BlockRelative[]{UP_NORTH,UP_EAST,UP_SOUTH,UP_WEST,NORTH_EAST,NORTH_WEST,SOUTH_EAST,SOUTH_WEST,DOWN_NORTH,DOWN_EAST,DOWN_SOUTH,DOWN_WEST};
	public static final BlockRelative[] corners = new BlockRelative[]{UP_NORTH_WEST,UP_NORTH_EAST,UP_SOUTH_WEST,UP_SOUTH_EAST,DOWN_NORTH_WEST,DOWN_NORTH_EAST,DOWN_SOUTH_WEST,DOWN_SOUTH_EAST};
	
	private final int x,y,z;
	
	BlockRelative(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	BlockRelative(BlockRelative a, BlockRelative b) {
		this.x = a.x + b.x;
		this.y = a.y + b.y;
		this.z = a.z + b.z;
	}
	
	BlockRelative(BlockRelative a, BlockRelative b, BlockRelative c){
		this.x = a.x + b.x + c.x;
		this.y = a.y + b.y + c.y;
		this.z = a.z + b.z + c.z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public Vector getVector(){
		return new Vector(x,y,z);
	}
	
}
