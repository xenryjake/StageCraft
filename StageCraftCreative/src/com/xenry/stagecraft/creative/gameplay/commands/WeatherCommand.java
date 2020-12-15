package com.xenry.stagecraft.creative.gameplay.commands;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.ArrayUtil;
import com.xenry.stagecraft.util.LocationUtil;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class WeatherCommand extends Command<Creative,GameplayManager> {
	
	private static final HashMap<String,Type> types;
	
	static{
		types = new HashMap<>();
		types.put("sun", Type.SUN);
		types.put("clear", Type.SUN);
		types.put("rain", Type.RAIN);
		types.put("storm", Type.RAIN);
		types.put("thunder", Type.THUNDER);
		types.put("thunderstorm", Type.THUNDER);
		types.put("lightning", Type.THUNDER);
	}
	
	public WeatherCommand(GameplayManager manager){
		super(manager, Rank.HEAD_MOD, "weather", "sun", "rain", "storm", "thunder");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(types.containsKey(label)){
			args = ArrayUtil.insertAtStart(args, label);
			label = "weather";
		}
		if(args.length < 1){
			profile.sendMessage(M.usage("/" + label + " <type> [world]"));
			return;
		}
		
		Type type = types.getOrDefault(args[0], null);
		if(type == null){
			profile.sendMessage(M.error("Invalid weather type: " + args[0]));
			return;
		}
		
		World world = profile.getPlayer().getWorld();
		if(args.length > 1){
			world = Bukkit.getWorld(args[1]);
			if(world == null){
				profile.sendMessage(M.error("World not found: " + args[1]));
				return;
			}
		}
		
		setWeather(world, type);
		profile.sendMessage(M.msg + "Set weather in " + M.elm + world.getName().toLowerCase() + M.msg + " to " + M.elm
				+ type.name() + M.msg + ".");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(types.containsKey(label)){
			args = ArrayUtil.insertAtStart(args, label);
			label = "weather";
		}
		if(args.length < 2){
			sender.sendMessage(M.usage("/" + label + " <type> <world>"));
			return;
		}
		
		Type type = types.getOrDefault(args[0], null);
		if(type == null){
			sender.sendMessage(M.error("Invalid weather type: " + args[0]));
			return;
		}
		
		World world = Bukkit.getWorld(args[1]);
		if(world == null){
			sender.sendMessage(M.error("World not found: " + args[1]));
			return;
		}
		
		setWeather(world, type);
		sender.sendMessage(M.msg + "Weather set to " + M.elm + type.name().toLowerCase() + M.msg + " in " + M.elm
				+ world.getName() + M.msg + ".");
	}
	
	private void setWeather(World world, Type type){
		if(type.type == WeatherType.DOWNFALL){
			world.setStorm(true);
			world.setThundering(type.thunder);
		}else{
			world.setThundering(false);
			world.setStorm(false);
		}
	}
	
	private enum Type {
		
		SUN(WeatherType.CLEAR, false),
		RAIN(WeatherType.DOWNFALL, false),
		THUNDER(WeatherType.DOWNFALL, true);
		
		public final WeatherType type;
		public final boolean thunder;
		
		Type(WeatherType type, boolean thunder){
			this.type = type;
			this.thunder = thunder;
		}
		
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		int mod = types.containsKey(label) ? 1 : 0;
		switch(args.length + mod){
			case 1:
				return filter(new ArrayList<>(types.keySet()), args[-mod]);
			case 2:
				return LocationUtil.getWorldNames(args[1-mod]);
			default:
				return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		int mod = types.containsKey(label) ? 1 : 0;
		switch(args.length + mod){
			case 1:
				return filter(new ArrayList<>(types.keySet()), args[-mod]);
			case 2:
				return LocationUtil.getWorldNames(args[1-mod]);
			default:
				return Collections.emptyList();
		}
	}
	
}
