package com.xenry.stagecraft.profile.permissions;
import com.xenry.stagecraft.profile.Profile;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class PermissionSet {
	
	public final String name;
	protected final HashMap<String,Boolean> permissions;
	
	public PermissionSet(String name){
		this.name = name;
		this.permissions = new HashMap<>();
	}
	
	public HashMap<String,Boolean> getPermissions() {
		return permissions;
	}
	
	public boolean isSet(String permission){
		return permissions.containsKey(permission);
	}
	
	@Nullable
	public Boolean get(String permission){
		return permissions.getOrDefault(permission, null);
	}
	
	public void set(String permission, boolean value){
		permissions.put(permission, value);
	}
	
	public void unset(String permission){
		permissions.remove(permission);
	}
	
	public void clear(){
		permissions.clear();
	}
	
	public abstract boolean appliesTo(Profile profile);
	
	public abstract String getAccessName();
	
}
