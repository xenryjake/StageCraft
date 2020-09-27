package com.xenry.stagecraft.util.time;
import com.xenry.stagecraft.util.MathUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TimeUtil {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	
	private TimeUtil(){}
	
	public static String nowString(){
		return dateFormat.format(Calendar.getInstance().getTime());
	}
	
	public static String dateFormat(long timestamp){
		return dateFormat.format(new Date(timestamp));
	}
	
	public static long now(){
		return System.currentTimeMillis();
	}
	
	public static long nowSeconds(){
		return System.currentTimeMillis()/1000;
	}
	
	public static String simpleString(long time, int trim, TimeUnit unit) {
		if (time == -1L) {
			return "forever";
		}
		if(unit == TimeUnit.AUTO) {
			unit = time < 60000L ? TimeUnit.SECONDS
					: (time < 3600000L ? TimeUnit.MINUTES
					: (time < 86400000L ? TimeUnit.HOURS
					: TimeUnit.DAYS));
		}
		return unit == TimeUnit.DAYS ? MathUtil.trim(trim, (double) time / 8.64E7D) + " days"
				: (unit == TimeUnit.HOURS ? MathUtil.trim(trim, (double) time / 3600000.0D) + " hours"
				: (unit == TimeUnit.MINUTES ? MathUtil.trim(trim, (double) time / 60000.0D) + " minutes"
				: (unit == TimeUnit.SECONDS ? MathUtil.trim(trim, (double) time / 1000.0D) + " seconds"
				: MathUtil.trim(trim, (double) time) + " milliseconds")));
	}
	
	public static String simplerString(long seconds){
		return simplerString(seconds, true);
	}
	
	public static String simplerString(long seconds, boolean allow0){
		if(seconds < 0){
			return "forever";
		}
		long minutes = 0;
		long hours = 0;
		long days = 0;
		if(seconds >= 60){
			minutes = seconds / 60;
			seconds = seconds % 60;
		}
		if(minutes >= 60){
			hours = minutes / 60;
			minutes = minutes % 60;
		}
		if(hours >= 24){
			days = hours / 24;
			hours = hours % 24;
		}
		
		StringBuilder sb = new StringBuilder();
		if(days > 0){
			sb.append(days).append(days == 1 ? " day " : " days ");
		}
		if(hours > 0){
			sb.append(hours).append(hours == 1 ? " hour " : " hours ");
		}
		if(minutes > 0){
			sb.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
		}
		if(seconds > 0){
			sb.append(seconds).append(seconds == 1 ? " second " : " seconds ");
		}
		if(sb.length() == 0){
			sb.append(allow0 ? "0 seconds" : "1 second");
		}
		return sb.toString().trim();
	}
	
	public String getClock(long seconds){
		if(seconds < 0){
			seconds = 0;
		}
		
		String mins = String.valueOf((int)Math.floor(seconds/60d));
		String secs = String.valueOf(seconds % 60);
		
		if((seconds % 60) < 10){
			secs = "0" + secs;
		}
		return mins + ":" + secs;
	}
	
	public static String simpleString(long time, TimeUnit unit){
		return simpleString(time, 1, unit);
	}
	
	public static String since(long time){
		return simpleString(now() - time, TimeUnit.AUTO);
	}
	
	public static String since(int seconds){
		return since(fromSeconds(seconds));
	}
	
	public static String until(long time){
		return simpleString(time - now(), TimeUnit.AUTO);
	}
	
	public static String until(int seconds){
		return until(fromSeconds(seconds));
	}
	
	public enum TimeUnit {
		AUTO, DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS
	}
	
	public static long toSecond(long time){
		return time / TimeLength.SECOND;
	}
	
	public static long fromSeconds(int second){
		return (long)second * TimeLength.SECOND;
	}
	
	public static int currentSecond(){
		return (int)(System.currentTimeMillis()/1000);
	}
	
	public static String getClockFromSeconds(double seconds){
		if(seconds < 0){
			seconds = 0;
		}
		
		String mins = String.valueOf((int)Math.floor(seconds/60d));
		String secs = String.valueOf(seconds % 60);
		
		if((seconds % 60) < 10){
			secs = "0" + secs;
		}
		return mins + ":" + secs;
	}
	
	public static String getTimeStringFromSeconds(double seconds){
		if(seconds < 0){
			seconds = 0;
		}
		
		String mins = String.valueOf((int)Math.floor(seconds / 60d));
		String secs = String.valueOf(Math.round(Math.floor(seconds % 60)));
		
		if((seconds % 60) < 10){
			secs = "0" + secs;
		}
		return mins + "m " + secs + "s";
	}
	
}
