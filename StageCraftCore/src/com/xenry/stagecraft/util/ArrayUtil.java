package com.xenry.stagecraft.util;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArrayUtil {
	
	private ArrayUtil(){}
	
	public static void toLowerCase(String[] array){
		for(int i = 0; i < array.length; i++){
			array[i] = array[i].toLowerCase();
		}
	}
	
	public static String[] toUpperCase(String[] array){
		for(int i = 0; i < array.length; i++){
			array[i] = array[i].toUpperCase();
		}
		return array;
	}
	
	public static String[] insert(String[] array, String value, int index){
		String[] result = new String[array.length + 1];
		System.arraycopy(array, 0, result, 0, index);
		result[index] = value;
		System.arraycopy(array, index, result, index + 1, array.length - index);
		return result;
	}
	
	public static String[] insertAtStart(String[] array, String value){
		return insert(array, value, 0);
	}
	
	/*public static <T> T[] insert(T[] array, T value, int index){
		Object[] result = new Object[array.length + 1];
		System.arraycopy(array, 0, result, 0, index);
		result[index] = value;
		System.arraycopy(array, index, result, index + 1, array.length - index);
		return (T[])result;
	}
	
	@Deprecated
	public static <T> T[] addItemToArrayAtIndex(T label, T[] args, int index){
		List<T> newArgsList = Arrays.asList(args);
		if(index > newArgsList.size()){
			index = newArgsList.size();
		}
		if(index < 0){
			index = 0;
		}
		newArgsList.add(index, label);
		return (T[])newArgsList.toArray();
	}
	
	@Deprecated
	public static <T> T[] addItemToArrayAtStart(T label, T[] args){
		return addItemToArrayAtIndex(label, args, 0);
	}
	
	@Deprecated
	public static <T> T[] addItemToArrayAtEnd(T item, T[] args){
		return addItemToArrayAtIndex(item, args, args.length);
	}*/
	
}
