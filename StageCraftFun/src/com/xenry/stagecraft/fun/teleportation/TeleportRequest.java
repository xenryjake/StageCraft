package com.xenry.stagecraft.fun.teleportation;

import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.Warmup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/24/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class TeleportRequest {
	
	public static final int EXPIRY_SECONDS = 60;
	
	public final TeleportationManager manager;
	public final Player fromPlayer;
	public final Player toPlayer;
	public final boolean reverse;
	
	private boolean cancelled = false;
	private Warmup expireWarmup;
	
	TeleportRequest(TeleportationManager manager, Player fromPlayer, Player toPlayer, boolean reverse){
		this.manager = manager;
		this.fromPlayer = fromPlayer;
		this.toPlayer = toPlayer;
		this.reverse = reverse;
	}
	
	public void send(){
		if(cancelled){
			return;
		}
		
		manager.setRequestTo(toPlayer, this);
		
		if(reverse){
			fromPlayer.sendMessage(M.msg + "Requesting " + M.elm + toPlayer.getName() + M.msg + " to teleport to you...");
			toPlayer.sendMessage(M.elm + fromPlayer.getName() + M.msg + " has requested that you teleport to them.\n"
					+ M.msg + "Type " + M.elm + "/tpaccept" + M.msg + " to accept or " + M.elm + "/tpdeny" + M.msg + " to deny.\n"
					+ M.msg + "This request will expire in " + M.elm + EXPIRY_SECONDS + " seconds" + M.msg + ".");
			toPlayer.spigot().sendMessage();
		}else{
			fromPlayer.sendMessage(M.msg + "Requesting to teleport to " + M.elm + toPlayer.getName() + M.msg + "...");
			toPlayer.sendMessage(M.elm + fromPlayer.getName() + M.msg + " has requested to teleport to you.\n"
					+ M.msg + "Type " + M.elm + "/tpaccept" + M.msg + " to accept or " + M.elm + "/tpdeny" + M.msg + " to deny.\n"
					+ M.msg + "This request will expire in " + M.elm + EXPIRY_SECONDS + " seconds" + M.msg + ".");
		}
		{
			TextComponent accept = new TextComponent("[ACCEPT]");
			accept.setColor(ChatColor.GREEN);
			accept.setBold(true);
			accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
			accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("Click to Accept").color(ChatColor.GREEN).create())));
			TextComponent deny = new TextComponent("[DENY]");
			deny.setColor(ChatColor.RED);
			deny.setBold(true);
			deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
			deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("Click to Deny").color(ChatColor.RED).create())));
			toPlayer.spigot().sendMessage(new ComponentBuilder(" ").append(accept).append(" ").append(deny).create());
		}
		expireWarmup = new Warmup(EXPIRY_SECONDS * 20, this::expire);
		expireWarmup.use();
	}
	
	public void expire(){
		if(cancelled){
			return;
		}
		cancel(false);
		fromPlayer.sendMessage(M.msg + "Your teleport request to " + M.elm + toPlayer.getName() + M.msg + " has expired.");
		toPlayer.sendMessage(M.msg + "Your teleport request from " + M.elm + fromPlayer.getName() + M.msg + " has expired.");
	}
	
	public void cancel(boolean notify){
		expireWarmup.cancel();
		cancelled = true;
		TeleportRequest request = manager.getRequestTo(toPlayer);
		if(request == this){
			manager.removeRequestTo(toPlayer);
		}
		if(notify){
			fromPlayer.sendMessage(M.msg + "Your teleport request to " + M.elm + toPlayer.getName() + M.msg + " has been cancelled.");
			toPlayer.sendMessage(M.msg + "Your teleport request from " + M.elm + fromPlayer.getName() + M.msg + " has been cancelled.");
		}
	}
	
	public void deny(){
		cancel(false);
		toPlayer.sendMessage(M.msg + "Teleport request from " + M.elm + fromPlayer.getName() + M.msg + " denied.");
		fromPlayer.sendMessage(M.elm + toPlayer.getName() + M.msg + " has denied your teleport request.");
	}
	
	public void accept(){
		cancel(false);
		toPlayer.sendMessage(M.msg + "Teleport request from " + M.elm + fromPlayer.getName() + M.msg + " accepted...");
		fromPlayer.sendMessage(M.elm + toPlayer.getName() + M.msg + " has accepted your teleport request...");
		
		if(reverse){
			manager.createAndExecuteTeleportation(toPlayer, toPlayer, toPlayer.getLocation(), fromPlayer.getLocation(), Teleportation.Type.REQUEST);
		}else{
			manager.createAndExecuteTeleportation(fromPlayer, fromPlayer, fromPlayer.getLocation(), toPlayer.getLocation(), Teleportation.Type.REQUEST);
		}
	}
	
}
