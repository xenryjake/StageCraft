package com.xenry.stagecraft.util;
/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/4/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class LocationVector extends Vector3DDouble {
	
	public final float yaw;
	public final float pitch;
	
	public LocationVector(double x, double y, double z, float yaw, float pitch){
		super(x, y, z);
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public boolean isZero(){
		return x == 0 && y == 0 && z == 0 && yaw == 0 && pitch == 0;
	}
	
}
