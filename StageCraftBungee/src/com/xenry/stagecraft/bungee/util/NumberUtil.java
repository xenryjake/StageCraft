package com.xenry.stagecraft.bungee.util;

import java.text.DecimalFormat;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class NumberUtil {
	
	public static final DecimalFormat hundredths = new DecimalFormat("#0.0#");
	
	private NumberUtil(){}
	
	public static boolean isIntWithin(int number, int a, int b){
		if(a > b){
			return a >= number && number >= b;
		}else{
			return b >= number && number >= a;
		}
	}
	
	public static Float doubleToFloat(Double d){
		return toFloat(d);
	}
	
	public static Float toFloat(Object o){
		return Float.parseFloat(o.toString());
	}
	
	public static String displayAsHundredths(double number){
		return hundredths.format(number);
	}
	
}
