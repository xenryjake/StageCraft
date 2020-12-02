package com.xenry.stagecraft.creative.gameplay;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.util.Log;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class F3NHandler extends Handler<Creative,GameplayManager> {
	
	public static final byte STATUS_BYTE = 28;
	
	public F3NHandler(GameplayManager manager){
		super(manager);
	}
	
	private void sendPacket(Player player){
		try{
			EntityPlayer entity = ((CraftPlayer)player).getHandle();
			entity.playerConnection.sendPacket(new PacketPlayOutEntityStatus(entity, STATUS_BYTE));
		}catch(Exception ignored){
			Log.debug("F3N handler packet exception");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		manager.plugin.getServer().getScheduler().runTaskLater(manager.plugin, () -> sendPacket(event.getPlayer()), 10L);
	}
	
}
