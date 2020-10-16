package com.xenry.stagecraft.util;
import java.text.DecimalFormat;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class MathUtil {
	
	private MathUtil(){}
	
	public static double trim(int degree, double d){
		StringBuilder format = new StringBuilder("#.#");
		for(int i = 1; i < degree; ++i)
			format.append("#");
		return Double.parseDouble(new DecimalFormat(format.toString()).format(d));
	}
	
}
