package com.xenry.stagecraft.util;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class BungeeUtil {
	
	private BungeeUtil(){}
	
	public static void kickPlayer(Player sender, @NotNull String playerName, @NotNull String reason){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(playerName);
		out.writeUTF(reason);
		sender.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void connect(Player sender, @NotNull String serverName){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		sender.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void connectOther(Player sender, @NotNull String playerName, @NotNull String serverName){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(playerName);
		out.writeUTF(serverName);
		sender.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void message(Player sender, @NotNull String playerName, @NotNull String message){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(playerName);
		out.writeUTF(message);
		sender.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void messageAll(Player sender, @NotNull String message){
		message(sender, "ALL", message);
	}
	
	public static void messageRaw(Player sender, @NotNull String playerName, @NotNull String message){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("MessageRaw");
		out.writeUTF(playerName);
		out.writeUTF(message);
		sender.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void messageRawAll(Player sender, @NotNull String message){
		messageRaw(sender, "ALL", message);
	}
	
	public static void messageRaw(Player sender, @NotNull String playerName, @NotNull BaseComponent[] components){
		messageRaw(sender, playerName, ComponentSerializer.toString(components));
	}
	
	public static void messageRawAll(Player sender, @NotNull BaseComponent[] components){
		messageRaw(sender, "ALL", components);
	}
	
}
