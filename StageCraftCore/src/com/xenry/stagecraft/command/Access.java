package com.xenry.stagecraft.command;
import com.xenry.stagecraft.profile.Profile;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/21/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public interface Access {
	
	boolean has(@NotNull Profile profile);
	
	boolean has(@NotNull CommandSender sender);
	
	class Any implements Access {
		
		private final Access[] accesses;
		
		public Any(Access...accesses) {
			this.accesses = accesses;
		}
		
		@Override
		public boolean has(@NotNull Profile profile) {
			for(Access access : accesses){
				if(access.has(profile)){
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean has(@NotNull CommandSender sender) {
			for(Access access : accesses){
				if(access.has(sender)){
					return true;
				}
			}
			return false;
		}
		
	}
	
	class All implements Access {
		
		private final Access[] accesses;
		
		public All(Access...accesses) {
			this.accesses = accesses;
		}
		
		@Override
		public boolean has(@NotNull Profile profile) {
			for(Access access : accesses){
				if(!access.has(profile)){
					return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean has(@NotNull CommandSender sender) {
			for(Access access : accesses){
				if(access.has(sender)){
					return false;
				}
			}
			return true;
		}
		
	}
	
}
