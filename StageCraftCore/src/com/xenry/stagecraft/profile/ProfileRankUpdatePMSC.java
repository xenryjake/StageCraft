package com.xenry.stagecraft.profile;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.xenry.stagecraft.profile.commands.rank.RankSetCommand.SEE_RANK_UPDATES;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/29/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class ProfileRankUpdatePMSC extends PluginMessageSubChannel<Core,ProfileManager> {
	
	public ProfileRankUpdatePMSC(ProfileManager manager){
		super("ProfileRankUpdate", manager);
	}
	
	public void send(Player sender, Profile profile, String who){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(manager.plugin.getServerName());
		out.writeUTF(who);
		out.writeUTF(profile.getUUID());
		out.writeUTF(profile.getRank().name());
		send(out.toByteArray(), sender);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String serverOriginName = in.readUTF();
		if(serverOriginName.equals(manager.plugin.getServerName())){
			return;
		}
		String who = in.readUTF();
		
		String uuid = in.readUTF();
		String rankName = in.readUTF();
		
		
		
		Rank rank;
		try{
			rank = Rank.valueOf(rankName);
		}catch(Exception ex){
			Log.warn("Received ProfileRankUpdatePMSC with invalid rank.");
			return;
		}
		Profile profile = manager.getProfileByUUID(uuid);
		if(profile == null){
			return;
		}
		if(profile.isOnline()){
			profile.setRank(rank);
			profile.updateDisplayName();
			manager.save(profile);
		}
		String rankUpdateMessage = M.elm + who + M.msg + " set " + M.elm + profile.getLatestUsername() + M.msg + "'s rank to " + rank.getColoredName() + M.msg + ".";
		Log.toCS(rankUpdateMessage);
		for(Player player : Bukkit.getOnlinePlayers()){
			Profile prof = manager.plugin.getProfileManager().getProfile(player);
			if(prof != null && SEE_RANK_UPDATES.has(prof) && !player.getName().equals(who)){
				player.sendMessage(rankUpdateMessage);
			}
		}
	}
	
}
