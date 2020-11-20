package com.xenry.stagecraft.survival.gameplay.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.ArrayUtil;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.time.GameTimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TimeCommand extends Command<Survival,GameplayManager> {
	
	private static final HashMap<String,Integer> times;
	
	static{
		times = new HashMap<>();
		times.put("daystart", 0);
		times.put("day", 0);
		times.put("morning", 1000);
		times.put("midday", 6000);
		times.put("noon", 6000);
		times.put("afternoon", 9000);
		times.put("sunset", 12000);
		times.put("dusk", 12000);
		times.put("sundown", 12000);
		times.put("nightfall", 12000);
		times.put("nightstart", 14000);
		times.put("night", 14000);
		times.put("midnight", 18000);
		times.put("dawn", 23000);
		times.put("sunrise", 23000);
	}
	
	public TimeCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "time", "day", "night", "dawn", "dusk", "noon", "midnight");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		ArrayUtil.toLowerCase(args);
		if(times.containsKey(label)){
			args = ArrayUtil.insertAtStart(args, label);
			label = "time";
		}
		
		boolean add = false;
		List<String> argList = new ArrayList<>(Arrays.asList(args));
		if(argList.contains("add")){
			add = true;
			argList.remove("add");
		}
		argList.remove("set");
		
		if(argList.size() < 1){
			profile.sendMessage(M.usage("/" + label + " <time> [world]"));
			return;
		}
		
		Long time = parseTime(argList.get(0));
		if(time == null){
			profile.sendMessage(M.error("Invalid time: " + argList.get(0)));
			return;
		}
		
		World world = profile.getPlayer().getWorld();
		if(argList.size() > 1){
			world = Bukkit.getWorld(argList.get(1));
			if(world == null){
				profile.sendMessage(M.error("World not found: " + argList.get(1)));
				return;
			}
		}
		
		setTime(profile.getPlayer(), world, time, add);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		ArrayUtil.toLowerCase(args);
		if(times.containsKey(label)){
			args = ArrayUtil.insertAtStart(args, label);
			label = "time";
		}
		
		boolean add = false;
		List<String> argList = new ArrayList<>(Arrays.asList(args));
		if(argList.contains("add")){
			add = true;
			argList.remove("add");
		}
		argList.remove("set");
		
		if(argList.size() < 2){
			sender.sendMessage(M.usage("/" + label + " <time> <world>"));
			return;
		}
		
		Long time = parseTime(argList.get(0));
		if(time == null){
			sender.sendMessage(M.error("Invalid time: " + argList.get(0)));
			return;
		}
		
		World world = Bukkit.getWorld(argList.get(1));
		if(world == null){
			sender.sendMessage(M.error("World not found: " + argList.get(1)));
			return;
		}
		
		setTime(sender, world, time, add);
	}
	
	private void setTime(CommandSender sender, World world, long time, boolean add){
		world.setTime(add ? world.getTime() + time : time);
		time = world.getTime();
		sender.sendMessage(M.msg + "Time set to " + M.elm + GameTimeUtil.formatTicks(time) + M.msg + " or " + M.elm
				+ GameTimeUtil.format12(time) + M.msg + " or " + M.elm + GameTimeUtil.format24(time) + M.msg + " in "
				+ M.elm + world.getName() + M.msg + ".");
	}
	
	public static Long parseTime(String input){
		input = input.toLowerCase().replaceAll("[^A-Za-z0-9:]", "");
		
		//defined word
		if(times.containsKey(input)){
			return times.get(input).longValue();
		}
		
		//24-hour time
		if(input.matches("^[0-9]{1,2}:[0-9]{2}$")){
			String[] split = input.split(":");
			return GameTimeUtil.hoursMinutesToTicks(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}
		
		//12-hour time
		if(input.matches("^[0-9]{1,2}(:[0-9]{2})?([ap]m?)$")){
			String hoursString;
			String minutesString = null;
			
			if(input.contains(":")){
				String[] split = input.replaceAll("[a-z]", "").split(":");
				hoursString = split[0];
				if(split.length > 1){
					minutesString = split[1];
				}
			}else{
				hoursString = input.replaceAll("^[0-9]", "");
			}
			
			if(minutesString == null){
				minutesString = "0";
			}
			
			int hours, minutes;
			try{
				hours = Integer.parseInt(hoursString);
				minutes = Integer.parseInt(minutesString);
			}catch(Exception ex){
				return null;
			}
			
			if(hours != 12 && (input.endsWith("pm") || input.endsWith("p"))){
				hours += 12;
			}else if(hours == 12 && (input.endsWith("am") || input.endsWith("a"))){
				hours -= 12;
			}
			
			return GameTimeUtil.hoursMinutesToTicks(hours, minutes);
		}
		
		//raw tick value
		if(input.matches("^[0-9]+t?i?c?k?s?$")){
			input = input.replaceAll("[^0-9]", "");
			return Long.parseLong(input) % 24000;
		}
		
		return null;
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				return new ArrayList<>(times.keySet());
			case 2:
				return LocationUtil.getAllWorldNames();
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		switch(args.length){
			case 1:
				return new ArrayList<>(times.keySet());
			case 2:
				return LocationUtil.getAllWorldNames();
			default:
				return Collections.emptyList();
		}
	}
	
}
