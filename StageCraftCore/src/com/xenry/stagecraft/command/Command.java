package com.xenry.stagecraft.command;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.StageCraftPlugin;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.PlayerUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 1/26/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public abstract class Command<P extends StageCraftPlugin,T extends Manager<P>> {
	
	protected final T manager;
	protected final Access access;
	protected final List<String> labels;
	protected final List<Command<P,T>> subCommands;
	public final boolean canBeDisabled;
	private boolean disabled = false;
	private boolean useEvents = true;
	
	protected Command(T manager, Access access, boolean canBeDisabled, String...labels){
		this.manager = manager;
		this.access = access;
		this.canBeDisabled = canBeDisabled;
		this.labels = new ArrayList<>();
		for(String lb : labels) {
			this.labels.add(lb.toLowerCase());
		}
		subCommands = new ArrayList<>();
	}
	
	protected Command(T manager, Access access, String...labels){
		this(manager, access, true, labels);
	}
	
	public Access getAccess() {
		return access;
	}
	
	public List<String> getLabels() {
		return labels;
	}
	
	public List<Command<P,T>> getSubCommands() {
		return subCommands;
	}
	
	protected abstract void playerPerform(Profile profile, String[] args, String label);
	
	protected abstract void serverPerform(CommandSender sender, String[] args, String label);
	
	@NotNull
	protected abstract List<String> playerTabComplete(Profile profile, String[] args, String label);
	
	@NotNull
	protected abstract List<String> serverTabComplete(CommandSender sender, String[] args, String label);
	
	public Command<P,T> getSubCommand(String label){
		for(Command<P,T> cmd : subCommands) {
			if(cmd.getLabels().contains(label.toLowerCase())){
				return cmd;
			}
		}
		return null;
	}
	
	public boolean isDisabled(){
		return disabled;
	}
	
	public void setDisabled(boolean disabled){
		if(!canBeDisabled){
			return;
		}
		this.disabled = disabled;
	}
	
	public boolean usesEvents(){
		return useEvents;
	}
	
	public void setUseEvents(boolean useEvents){
		this.useEvents = useEvents;
	}
	
	protected final void addSubCommand(Command<P,T> command){
		subCommands.add(command);
	}
	
	public boolean hasNoSubCommands(){
		return subCommands.isEmpty();
	}
	
	public final void playerExecute(Profile profile, String[] args, String label){
		if(!access.has(profile)){
			noPermission(profile);
			return;
		}
		if(disabled){
			profile.sendMessage(M.error("This command is currently disabled."));
			return;
		}
		if(args.length < 1 || hasNoSubCommands()){
			playerPerform(profile, args, label);
			return;
		}
		Command<P,T> sub = getSubCommand(args[0].toLowerCase());
		if(sub == null){
			playerPerform(profile, args, label);
		}else{
			String[] newArgs = new String[args.length - 1];
			System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			sub.playerExecute(profile, newArgs, args[0].toLowerCase());
		}
	}
	
	public final void serverExecute(CommandSender sender, String[] args, String label){
		if(disabled){
			sender.sendMessage(M.error("This command is currently disabled."));
			return;
		}
		if(args.length < 1 || hasNoSubCommands()){
			serverPerform(sender, args, label);
			return;
		}
		Command<P,T> sub = getSubCommand(args[0].toLowerCase());
		if(sub == null){
			serverPerform(sender, args, label);
		}else{
			String[] newArgs = new String[args.length - 1];
			System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			sub.serverExecute(sender, newArgs, args[0].toLowerCase());
		}
	}
	
	public final List<String> playerTabCompleteExecute(Profile profile, String[] args, String label){
		if(!access.has(profile) || disabled){
			return Collections.emptyList();
		}
		if(args.length < 1){
			return Collections.emptyList();
		}
		Command<P,T> sub = getSubCommand(args[0].toLowerCase());
		if(sub == null){
			return playerTabComplete(profile, args, label);
		}else{
			String[] newArgs = new String[args.length - 1];
			System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			return sub.playerTabComplete(profile, newArgs, args[0].toLowerCase());
		}
	}
	
	public final List<String> serverTabCompleteExecute(CommandSender sender, String[] args, String label){
		if(disabled){
			return Collections.emptyList();
		}
		if(args.length < 1){
			return Collections.emptyList();
		}
		Command<P,T> sub = getSubCommand(args[0].toLowerCase());
		if(sub == null){
			return serverTabComplete(sender, args, label);
		}else{
			String[] newArgs = new String[args.length - 1];
			System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			return sub.serverTabComplete(sender, newArgs, args[0].toLowerCase());
		}
	}
	
	public T getManager() {
		return manager;
	}
	
	public P getPlugin(){
		return manager.plugin;
	}
	
	public Core getCore(){
		return manager.getCore();
	}
	
	protected final void onlyForPlayers(CommandSender sender){
		sender.sendMessage(M.error("This command is only available for players."));
	}
	
	protected final void notForPlayers(Player player){
		player.sendMessage(M.error("This command is not available to players."));
	}
	
	protected final void noPermission(Profile profile){
		noPermission(profile.getPlayer());
	}
	
	protected final void noPermission(Player player){
		player.sendMessage(M.DRED + "You do not have access to that command.");
	}
	
	protected final List<String> allNetworkPlayers(){
		return manager.getCore().getServerManager().getAllNetworkPlayers();
	}
	
	protected final List<String> allLocalPlayers(){
		return PlayerUtil.getOnlinePlayerNames();
	}
	
	protected final List<String> networkPlayers(String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> players = new ArrayList<>();
		for(String player : allNetworkPlayers()){
			if(player.toLowerCase().startsWith(startsWith)){
				players.add(player);
			}
		}
		return players;
	}
	
	protected final List<String> localPlayers(String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> players = new ArrayList<>();
		for(String player : allLocalPlayers()){
			if(player.toLowerCase().startsWith(startsWith)){
				players.add(player);
			}
		}
		return players;
	}
	
	protected final List<String> filter(List<String> list, String startsWith){
		startsWith = startsWith.toLowerCase();
		List<String> newList = new ArrayList<>();
		for(String string : list){
			if(string.toLowerCase().startsWith(startsWith)){
				newList.add(string);
			}
		}
		return newList;
	}
	
}
