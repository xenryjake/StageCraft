package com.xenry.stagecraft.util;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class MapUtil {
	
	private MapUtil(){}
	
	public static <K,V extends Comparable<? super V>> Map<K,V> sortByValue(Map<K,V> map){
		List<Entry<K,V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());
		Map<K,V> result = new LinkedHashMap<>();
		for(Entry<K,V> entry : list){
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
}
