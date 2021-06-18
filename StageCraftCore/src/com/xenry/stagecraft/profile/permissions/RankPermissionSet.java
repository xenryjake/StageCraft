package com.xenry.stagecraft.profile.permissions;

import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class RankPermissionSet extends PermissionSet {
	
	public final Rank rank;
	
	public RankPermissionSet(String name, Rank rank){
		super(name);
		this.rank = rank;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public boolean appliesTo(Rank rank){
		return rank.check(this.rank);
	}
	
	@Override
	public boolean appliesTo(Profile profile){
		for(Rank rank : profile.getRanks()){
			if(appliesTo(rank)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getAccessName() {
		return rank.getColoredName();
	}
	
}
