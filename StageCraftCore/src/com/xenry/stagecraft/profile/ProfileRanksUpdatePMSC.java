package com.xenry.stagecraft.profile;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.pluginmessage.PluginMessageSubChannel;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * MineTogether created by Henry Blasingame (Xenry) on 3/18/21
 * The content in this file and all related files are
 * Copyright (C) 2021 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
@SuppressWarnings("UnstableApiUsage")
public final class ProfileRanksUpdatePMSC extends PluginMessageSubChannel<Core,ProfileManager> {
	
	public static final Access SEE_RANK_UPDATES = Rank.ADMIN;
	
	public ProfileRanksUpdatePMSC(ProfileManager manager) {
		super("ProfileRanksUpdate", manager);
	}
	
	public void send(Player sender, Profile profile, Rank rank, ProfileRanksUpdateEvent.Action action, long expiry,
					 String who){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(CHANNEL_NAME);
		out.writeUTF(subChannelName);
		out.writeUTF(manager.plugin.getServerName());
		out.writeUTF(profile.getUUID());
		out.writeUTF(rank.name());
		out.writeUTF(action.name());
		out.writeLong(expiry);
		out.writeUTF(who);
		send(out.toByteArray(), sender);
	}
	
	@Override
	protected void receive(ByteArrayDataInput in, Player receiver) {
		String serverOriginName = in.readUTF();
		if(serverOriginName.equals(manager.plugin.getServerName())){
			return;
		}
		String uuid = in.readUTF();
		String rankName = in.readUTF();
		String actionName = in.readUTF();
		long expiry = in.readLong();
		String who = in.readUTF();
		
		Rank rank;
		try{
			rank = Rank.valueOf(rankName);
		}catch(Exception ex){
			Log.warn("Received ProfileRanksUpdatePMSC with invalid rank.");
			return;
		}
		
		ProfileRanksUpdateEvent.Action action;
		try{
			action = ProfileRanksUpdateEvent.Action.valueOf(actionName);
		}catch(Exception ex){
			Log.warn("Received ProfileRanksUpdatePMSC with invalid action.");
			return;
		}
		
		Profile profile = manager.getProfileByUUID(uuid);
		if(profile == null){
			return;
		}
		
		if(profile.isOnline()){
			if(action == ProfileRanksUpdateEvent.Action.ADD){
				if(expiry == -1L){
					profile.addRankPermanent(rank);
				}else{
					profile.rankTemporarily(rank, expiry);
				}
			}else{
				profile.removeRank(rank);
			}
			profile.updateDisplayName();
			manager.save(profile);
		}
		String rankUpdateMessage = M.elm + who + M.msg + " " + action.pastTenseVerb + " rank " + rank.getColoredName()
				+ M.msg + " " + action.toFrom + " " + M.elm + profile.getLatestUsername() + M.msg + ".";
		Log.toCS("[from " + serverOriginName + "] " + rankUpdateMessage);
		if(action != ProfileRanksUpdateEvent.Action.ADJUST && !who.equals(M.CONSOLE_NAME)){
			for(Player player : Bukkit.getOnlinePlayers()){
				Profile prof = manager.plugin.getProfileManager().getProfile(player);
				if(prof != null && SEE_RANK_UPDATES.has(prof) && !player.getName().equals(who)){
					player.sendMessage(rankUpdateMessage);
				}
			}
		}
	}
	
}
