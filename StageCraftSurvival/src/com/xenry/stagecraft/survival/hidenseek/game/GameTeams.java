package com.xenry.stagecraft.survival.hidenseek.game;
import com.sun.istack.internal.NotNull;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GameTeams extends Handler<Survival,HideNSeekManager> {
	
	private Team hidersTeam, seekersTeam, spectatorsTeam;
	
	public GameTeams(HideNSeekManager manager){
		super(manager);
	}
	
	public void setupTeams(){
		deleteTeams();
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		if(scoreboardManager == null){
			return;
		}
		Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
		
		hidersTeam = scoreboard.registerNewTeam("hns.hiders");
		hidersTeam.setColor(hiderColor);
		hidersTeam.setAllowFriendlyFire(false);
		hidersTeam.setDisplayName("Hiders");
		hidersTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		
		seekersTeam = scoreboard.registerNewTeam("hns.seekers");
		seekersTeam.setColor(seekerColor);
		seekersTeam.setAllowFriendlyFire(false);
		seekersTeam.setDisplayName("Seekers");
		seekersTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
		
		spectatorsTeam = scoreboard.registerNewTeam("hns.spectators");
		spectatorsTeam.setColor(spectatorColor);
		spectatorsTeam.setAllowFriendlyFire(false);
		spectatorsTeam.setCanSeeFriendlyInvisibles(true);
		spectatorsTeam.setDisplayName("Spectators");
	}
	
	public void clearEntriesFromTeams(){
		for(String entry : spectatorsTeam.getEntries()){
			spectatorsTeam.removeEntry(entry);
		}
		for(String entry : hidersTeam.getEntries()){
			hidersTeam.removeEntry(entry);
		}
		for(String entry : seekersTeam.getEntries()){
			seekersTeam.removeEntry(entry);
		}
	}
	
	public void removeEntryFromTeams(String entry){
		if(spectatorsTeam.hasEntry(entry)){
			spectatorsTeam.removeEntry(entry);
		}
		if(hidersTeam.hasEntry(entry)){
			hidersTeam.removeEntry(entry);
		}
		if(seekersTeam.hasEntry(entry)){
			seekersTeam.removeEntry(entry);
		}
	}
	
	public void removePlayerFromTeams(@NotNull Player player){
		removeEntryFromTeams(player.getName());
	}
	
	public boolean addPlayerToTeam(@NotNull Player player){
		return addPlayerToTeam(player, manager.getPlayerHandler().getPlayerMode(player));
	}
	
	public boolean addPlayerToTeam(@NotNull Player player, PlayerMode mode){
		if(player == null){
			return false;
		}
		String name = player.getName();
		switch(mode){
			case HIDER:
				if(seekersTeam.hasEntry(name)){
					seekersTeam.removeEntry(name);
				}
				if(spectatorsTeam.hasEntry(name)){
					spectatorsTeam.removeEntry(name);
				}
				if(!hidersTeam.hasEntry(name)){
					hidersTeam.addEntry(name);
				}
				return true;
			case SEEKER:
				if(hidersTeam.hasEntry(name)){
					hidersTeam.removeEntry(name);
				}
				if(spectatorsTeam.hasEntry(name)){
					spectatorsTeam.removeEntry(name);
				}
				if(!seekersTeam.hasEntry(name)){
					seekersTeam.addEntry(name);
				}
				return true;
			case SPECTATOR:
				if(hidersTeam.hasEntry(name)){
					hidersTeam.removeEntry(name);
				}
				if(seekersTeam.hasEntry(name)){
					seekersTeam.removeEntry(name);
				}
				if(!spectatorsTeam.hasEntry(name)){
					spectatorsTeam.addEntry(name);
				}
				return true;
			default:
				return false;
		}
	}
	
	public void deleteTeams(){
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		if(scoreboardManager == null){
			return;
		}
		Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
		Team hidersTeam = scoreboard.getTeam("hns.hiders");
		if(hidersTeam != null){
			for(String entry : hidersTeam.getEntries()){
				hidersTeam.removeEntry(entry);
			}
			hidersTeam.unregister();
		}
		Team seekersTeam = scoreboard.getTeam("hns.seekers");
		if(seekersTeam != null){
			for(String entry : seekersTeam.getEntries()){
				seekersTeam.removeEntry(entry);
			}
			seekersTeam.unregister();
		}
		Team spectatorsTeam = scoreboard.getTeam("hns.spectators");
		if(spectatorsTeam != null){
			for(String entry : spectatorsTeam.getEntries()){
				spectatorsTeam.removeEntry(entry);
			}
			spectatorsTeam.unregister();
		}
	}
	
	public Team getHidersTeam() {
		return hidersTeam;
	}
	
	public Team getSeekersTeam() {
		return seekersTeam;
	}
	
	public Team getSpectatorsTeam() {
		return spectatorsTeam;
	}
	
}
