package com.xenry.stagecraft.util;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class CollectionUtil {
	
	private CollectionUtil(){}
	
	@Nullable
	public static String findClosestMatchByStart(Collection<String> collection, String query){
		if(collection == null || query == null){
			return null;
		}
		if(collection.contains(query)){
			return query;
		}
		String value = null;
		query = query.toLowerCase();
		int delta = 2147483647;
		for(String string : collection){
			if(string.toLowerCase().startsWith(query)){
				int curDelta = Math.abs(string.length() - query.length());
				if(curDelta < delta){
					delta = curDelta;
					value = string;
				}
				if(curDelta < 1){
					break;
				}
			}
		}
		return value;
	}
	
}
