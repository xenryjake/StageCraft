package com.xenry.stagecraft.command;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public class SudoPMSC extends PluginMessageSubChannel<Core,CommandManager> {
	
	public SudoPMSC(CommandManager manager) {
		super("Sudo", manager);
	}
	
	public void send(Player sender, String sudoer, String target, String content){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(manager.plugin.getServerName());
		out.writeUTF(sudoer);
		out.writeUTF(target);
		out.writeUTF(content);
		send(out.toByteArray(), sender);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String originServerName = in.readUTF();
		String sudoer = in.readUTF();
		String target = in.readUTF();
		String content = in.readUTF();
		
		Log.toCS(M.msg + "Executing remote sudo for " + M.elm + target + M.msg + " by " + M.elm + sudoer + M.msg +
				" on " + M.elm + originServerName + M.msg + ": " + M.WHITE + content);
		if(target.equalsIgnoreCase("#console")) {
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), content);
		}else if(target.equals("**")){
			for(Player player : Bukkit.getOnlinePlayers()){
				player.chat(content);
			}
		}else{
			Player player = Bukkit.getPlayerExact(target);
			if(player == null){
				Log.warn("Received SudoPMSC for an offline or invalid target.");
				return;
			}
			player.chat(content);
		}
	}
	
}
