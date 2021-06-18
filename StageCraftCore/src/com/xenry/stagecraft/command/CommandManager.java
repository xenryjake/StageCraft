package com.xenry.stagecraft.command;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.commands.*;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 1/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class CommandManager extends Manager<Core> implements TabExecutor {
	
	protected final List<Command<?,?>> commands;
	private final SudoPMSC sudoPMSC;
	
	private final List<String> defaultNamespacePrefixes = Arrays.asList("minecraft:", "bukkit:", "spigot:", "paper:");
	private final List<String> unsafeForPlayers = Arrays.asList("op", "minecraft:op", "deop", "minecraft:deop");
	
	public CommandManager(Core plugin){
		super("Commands", plugin);
		commands = new ArrayList<>();
		sudoPMSC = new SudoPMSC(this);
	}
	
	@Override
	public void onEnable(){
		registerCommand(new CommandCommand(this));
		registerCommand(new DiceRollCommand(this));
		registerCommand(new SudoCommand(this));
		registerCommand(new InfoCommand(this));
		
		plugin.getPluginMessageManager().registerSubChannel(sudoPMSC);
	}
	
	public SudoPMSC getSudoPMSC() {
		return sudoPMSC;
	}
	
	public Command<?,?> getCommand(String label){
		label = label.toLowerCase();
		for(Command<?,?> command : commands){
			if(command.getLabels().contains(label)){
				return command;
			}
		}
		return null;
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command bukkitCommand, @NotNull String label, @NotNull String[] args) {
		label = stripPluginPrefix(label.toLowerCase());
		Command<?,?> cmd = getCommand(label);
		if(cmd == null){
			sender.sendMessage("That command doesn't exist.");
			return true;
		}
		if(sender instanceof Player){
			Player player = (Player)sender;
			Profile profile = plugin.getProfileManager().getProfile(player);
			if(profile == null){
				sender.sendMessage(M.error("Couldn't find your profile."));
				return true;
			}
			try{
				cmd.playerExecute(profile, args, label);
			}catch(Exception ex){
				sender.sendMessage(M.error("Failed to process command"));
				Log.warn("Failed to process player command: /" + label);
				ex.printStackTrace();
			}
		}else{
			try{
				cmd.serverExecute(sender, args, label);
			}catch(Exception ex){
				sender.sendMessage(M.error("Failed to process server command: /" + label));
				ex.printStackTrace();
			}
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command bukkitCommand, @NotNull String label, @NotNull String[] args) {
		label = stripPluginPrefix(label.toLowerCase());
		Command<?,?> cmd = getCommand(label);
		if(cmd == null){
			return Collections.emptyList();
		}
		if(sender instanceof Player){
			Player player = (Player)sender;
			Profile profile = plugin.getProfileManager().getProfile(player);
			if(profile == null){
				return Collections.emptyList();
			}
			return cmd.playerTabCompleteExecute(profile, args, label);
		}else{
			return cmd.serverTabCompleteExecute(sender, args, label);
		}
	}
	
	@EventHandler
	public void onPlayer(PlayerCommandPreprocessEvent event){
		String buffer = event.getMessage();
		if(buffer.startsWith("/")){
			buffer = buffer.substring(1);
		}
		while(buffer.startsWith(" ")){
			buffer = buffer.substring(1);
		}
		buffer = stripPluginPrefix(buffer);
		String[] split = buffer.split(" ");
		if(split.length < 1){
			return;
		}
		String label = split[0].toLowerCase();
		if(unsafeForPlayers.contains(label)){
			event.getPlayer().sendMessage(M.error("This command isn't available for players."));
			event.setCancelled(true);
			return;
		}
		String[] args = new String[split.length - 1];
		System.arraycopy(split, 1, args, 0, split.length - 1);
		Command<?,?> cmd = getCommand(label);
		if(cmd != null && cmd.usesEvents()){
			event.setCancelled(true);
			Profile profile = plugin.getProfileManager().getProfile(event.getPlayer());
			if(profile == null){
				event.getPlayer().sendMessage(M.error("Couldn't find your profile."));
				return;
			}
			try{
				cmd.playerExecute(profile, args, label);
			}catch(Exception ex){
				event.getPlayer().sendMessage(M.error("Failed to process command"));
				Log.warn("Failed to process player command: /" + label);
				ex.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onConsole(ServerCommandEvent event){
		String buffer = event.getCommand();
		if(buffer.startsWith("/")){
			buffer = buffer.substring(1);
		}
		while(buffer.startsWith(" ")){
			buffer = buffer.substring(1);
		}
		buffer = stripPluginPrefix(buffer);
		String[] split = buffer.split(" ");
		if(split.length < 1){
			return;
		}
		String label = split[0].toLowerCase();
		String[] args = new String[split.length - 1];
		System.arraycopy(split, 1, args, 0, split.length - 1);
		Command<?,?> cmd = getCommand(label);
		if(cmd != null && cmd.usesEvents()){
			event.setCancelled(true);
			try{
				cmd.serverExecute(event.getSender(), args, label);
			}catch(Exception ex){
				event.getSender().sendMessage(M.error("Failed to process server command: /" + label));
				ex.printStackTrace();
			}
		}
	}
	
	/*@EventHandler
	public void onTabComplete(TabCompleteEvent event){
		String buffer = event.getBuffer();
		if(buffer.startsWith("/")){
			buffer = buffer.substring(1);
		}
		while(buffer.startsWith(" ")){
			buffer = buffer.substring(1);
		}
		if(buffer.toLowerCase().startsWith("stagecraft:")){
			buffer = buffer.substring(11);
		}
		String[] split = buffer.split(" ");
		if(split.length < 1){
			return;
		}
		String label = split[0].toLowerCase();
		String[] args = new String[split.length - 1];
		System.arraycopy(split, 1, args, 0, split.length - 1);
		Command<?> cmd = null;
		for(Command<?> command : commands){
			if(command.getLabels().contains(label)){
				cmd = command;
			}
		}
		if(cmd == null || !cmd.usesEvents()){
			return;
		}
		CommandSender sender = event.getSender();
		if(sender instanceof Player){
			Player player = (Player)sender;
			Profile profile = plugin.getProfileManager().getProfile(player);
			if(profile == null){
				return;
			}
			List<String> completions = cmd.playerTabCompleteExecute(profile, args, label);
			if(completions != null){
				event.setCompletions(completions);
			}
		}else{
			List<String> completions = cmd.serverTabCompleteExecute(sender, args, label);
			if(completions != null){
				event.setCompletions(completions);
			}
		}
	}*/
	
	public void register(Command<?,?> command){
		commands.add(command);
	}
	
	public List<String> getAllCommandLabels(){
		List<String> labels = new ArrayList<>();
		for(Command<?,?> command : commands){
			labels.addAll(command.getLabels());
		}
		return labels;
	}
	
	public List<String> getCommandLabels(String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> labels = new ArrayList<>();
		for(Command<?,?> command : commands){
			for(String label : command.getLabels()){
				if(label.toLowerCase().startsWith(startsWith)){
					labels.add(label);
				}
			}
		}
		return labels;
	}
	
	private String stripPluginPrefix(String buffer){
		// todo make this better/less spaghetti
		if(buffer.toLowerCase().startsWith("stagecraft:")){
			buffer = buffer.substring(11);
		}
		if(buffer.toLowerCase().startsWith("stagecraftfun:")){
			buffer = buffer.substring(14);
		}
		if(buffer.toLowerCase().startsWith("stagecraftcore:")){
			buffer = buffer.substring(15);
		}
		if(buffer.toLowerCase().startsWith("stagecraftsurvival:")
				|| buffer.toLowerCase().startsWith("stagecraftcreative:")
				|| buffer.toLowerCase().startsWith("stagecraftskyblock:")){
			buffer = buffer.substring(19);
		}
		return buffer;
	}
	
}
