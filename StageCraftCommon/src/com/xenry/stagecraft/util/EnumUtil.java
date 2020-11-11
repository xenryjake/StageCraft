package com.xenry.stagecraft.util;
import com.google.common.base.Joiner;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EnumUtil {
	
	public static String generateName(Enum<?> object){
		String[] split = object.name().split("[ _]+");
		String[] words = new String[split.length];
		for(int i = 0; i < split.length; i++){
			if(split[i].length() < 1){
				words[i] = "";
			}else{
				words[i] = split[i].substring(0,1).toUpperCase() + split[i].substring(1).toLowerCase();
			}
		}
		return Joiner.on(' ').join(words);
	}
	
}
