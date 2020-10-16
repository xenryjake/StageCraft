package com.xenry.stagecraft.util;
import java.lang.reflect.Field;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/15/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ReflectionUtil {
	
	private ReflectionUtil(){}
	
	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch(NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
