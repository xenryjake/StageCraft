package com.xenry.stagecraft.survival.hidenseek;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.commands.HNSAdminCommand;
import com.xenry.stagecraft.survival.hidenseek.commands.HNSCommand;
import com.xenry.stagecraft.survival.hidenseek.game.*;
import com.xenry.stagecraft.survival.hidenseek.map.Boundary;
import com.xenry.stagecraft.survival.hidenseek.map.Map;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerHandler;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerListener;
import com.xenry.stagecraft.survival.hidenseek.player.PlayerMode;
import com.xenry.stagecraft.util.Log;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/9/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class HideNSeekManager extends Manager<Survival> {
	
	/**
	 * TODO LIST:
	 * 	"ready" system:
	 * 		/hns ready
	 * 		add countdown to start
	 * 	improve end conditions
	 * 		include winner determination
	 *  teleportation
	 * 		make some sort of admin-unstuck command for hiders restricted by movement
	 * 		add warp command to warp to game point [not needed, ess warps is fine]
	 *  potentially don't stop player move event for frozen players...it makes things too crazy
	 */
	
	final PlayerHandler playerHandler;
	final GameTeams teams;
	
	private GameType type;
	private GameStatus status;
	private final GameSettings settings;
	private Map map;
	private final GameTimer gameTimer;
	private int taskID;
	
	private List<Map> allMaps;
	
	public HideNSeekManager(Survival plugin){
		super("Hide N Seek", plugin);
		type = null;
		status = GameStatus.NONE;
		gameTimer = new GameTimer();
		settings = new GameSettings(120, 1200, 2, false, true);
		
		playerHandler = new PlayerHandler(this);
		teams = new GameTeams(this);
	}
	
	@Override
	protected void onEnable(){
		
		World world = Bukkit.getWorld("world");
		if(world == null){
			Log.warn("Couldn't find default world!");
		}
		
		
		allMaps = new ArrayList<>();
		{
			Location gamePoint = new Location(world, -4406.5, 64.5, 495.5, -90, 0);
			Location lobbyPoint = new Location(world, -4403.5, 173.5, 495.5, -90, 0);
			Boundary gameBoundary = new Boundary(world, -4499, 0, 301, -4119, 256, 602);
			Boundary lobbyBoundary = new Boundary(world, -4421, 169, 512, -4387, 180,478);
			map = new Map("default", world, gamePoint, lobbyPoint, gameBoundary, lobbyBoundary);
			allMaps.add(map);
		}
		
		World hauntedWorld = Bukkit.getWorld("spookyhouse");
		if(hauntedWorld != null){
			Location gamePoint = new Location(hauntedWorld, 115.5, 13.5, -181.5, 0, 0);
			Location lobbyPoint = new Location(world, -4403.5, 173.5, 495.5, -90, 0);
			Boundary gameBoundary = new Boundary(hauntedWorld, 188, 0, -210, 50, 256, -34);
			Boundary lobbyBoundary = new Boundary(world, -4421, 169, 512, -4387, 180,478);
			allMaps.add(new Map("hauntedHouse", hauntedWorld, gamePoint, lobbyPoint, gameBoundary, lobbyBoundary));
		}
		
		teams.setupTeams();
		
		registerListener(new PlayerListener(this));
		
		registerCommand(new HNSCommand(this));
		registerCommand(new HNSAdminCommand(this));
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			updateScoreboard();
			updateActionBar();
			handleTimer();
		}, 10L, 10L);
	}
	
	@Override
	public void onDisable(){
		for(Player player : Bukkit.getOnlinePlayers()){
			playerHandler.removePlayer(player);
		}
		Bukkit.getScheduler().cancelTask(taskID);
		teams.deleteTeams();
	}
	
	public void handleTimer(){
		gameTimer.update();
		if(status == GameStatus.PRE_GAME && gameTimer.getMode() == GameTimer.Mode.COUNT_DOWN){
			startHiding(true);
		}else if(status == GameStatus.HIDING && gameTimer.getSeconds() <= 0){
			startSeeking();
		}else if(status == GameStatus.SEEKING && gameTimer.getSeconds() <= 0){
			playerHandler.sendMessageToPlayers(prefix + "The clock has run out! Game over.");
			endGame();
		}
	}
	
	public boolean createGame(GameType type){
		if(type == null || status != GameStatus.NONE){
			return false;
		}
		status = GameStatus.PRE_GAME;
		this.type = type;
		gameTimer.reset();
		gameTimer.setMode(GameTimer.Mode.OFF);
		Bukkit.broadcast("", "hns.play");
		Bukkit.broadcast(msg + "§lA " + elm + "§l" + type.toString() + msg + "§l game has been created!", "hns.play");
		Bukkit.broadcast(msg + "Type " + hlt + "/hns join" + msg + " to join the game!", "hns.play");
		Bukkit.broadcast("", "hns.play");
		return true;
	}
	
	public void endGame(){
		if(status == GameStatus.NONE){
			return;
		}
		playerHandler.sendMessageToPlayers(prefix + "The game has ended!");
		status = GameStatus.NONE;
		
		type = null;
		teams.clearEntriesFromTeams();
		gameTimer.reset();
		gameTimer.setMode(GameTimer.Mode.OFF);
		for(String name : new ArrayList<>(playerHandler.getPlayers().keySet())){
			Player player = Bukkit.getPlayer(name);
			if(player == null){
				continue;
			}
			playerHandler.removePlayer(player, false);
		}
		playerHandler.clearPlayersHashMap();
	}
	
	public boolean startHiding(boolean announceOnFail){
		if(status != GameStatus.PRE_GAME){
			if(announceOnFail){
				playerHandler.sendMessageToPlayers(prefix + err + "The game can't start right now.");
			}
			return false;
		}
		if(playerHandler.getNumberOfActivePlayers() < settings.getMinimumPlayers()){
			if(announceOnFail){
				playerHandler.sendMessageToPlayers(prefix + err + "There aren't enough players to start the game.");
			}
			return false;
		}
		if(!playerHandler.getPlayers().containsValue(PlayerMode.HIDER) || !playerHandler.getPlayers().containsValue(PlayerMode.SEEKER)){
			if(announceOnFail){
				playerHandler.sendMessageToPlayers(prefix + err + "There must be at least 1 hider and 1 seeker to start the game.");
			}
			return false;
		}
		gameTimer.setMode(GameTimer.Mode.COUNT_DOWN);
		gameTimer.setSeconds(settings.getHideSeconds());
		status = GameStatus.HIDING;
		
		playerHandler.sendMessageToPlayers(prefix + "Hiding starts now! You have " + hlt + gameTimer.getClock() + msg + " to hide!");
		
		for(String name : playerHandler.getPlayers().keySet()){
			Player player = Bukkit.getPlayer(name);
			if(player == null){
				continue;
			}
			playerHandler.applyPlayerState(player);
			if(playerHandler.getPlayers().get(player.getName()) == PlayerMode.HIDER){
				player.teleport(map.getGamePoint());
			}
		}
		return true;
	}
	
	private void startSeeking(){
		/*if(status != GameStatus.HIDING || type == GameType.NONE){
			return false;
		}
		if(!checkGame()){
			return false;
		}*/
		gameTimer.setMode(GameTimer.Mode.COUNT_DOWN);
		gameTimer.setSeconds(settings.getSeekSeconds());
		status = GameStatus.SEEKING;
		
		playerHandler.sendMessageToPlayers(prefix + "The seekers have been released! Don't get found!");
		
		for(String name : playerHandler.getPlayers().keySet()){
			Player player = Bukkit.getPlayer(name);
			if(player == null){
				continue;
			}
			playerHandler.applyPlayerState(player);
			if(playerHandler.getPlayers().get(name) == PlayerMode.SEEKER){
				player.teleport(map.getGamePoint());
			}
		}
	}
	
	public void checkGame(){
		if(status == GameStatus.NONE || status == GameStatus.PRE_GAME){
			return;
		}
		if(playerHandler.getNumberOfActivePlayers() < settings.getMinimumPlayers()){
			playerHandler.sendMessageToPlayers(prefix + err + "There are no longer enough players in the game.");
			endGame();
			return;
		}
		int numHiders = playerHandler.getNumberOfPlayersByMode(PlayerMode.HIDER);
		int numSeekers = playerHandler.getNumberOfPlayersByMode(PlayerMode.SEEKER);
		if(numHiders < 1){
			playerHandler.sendMessageToPlayers(prefix + err + "There are no hiders left.");
			endGame();
			return;
		}
		if(numSeekers < 1){
			playerHandler.sendMessageToPlayers(prefix + err + "There are no seekers left.");
			endGame();
		}
	}
	
	public void updateScoreboard(){
		if(type == null){
			return;
		}
		TitleManagerAPI api = getCore().getIntegrationManager().getTitleManager();
		if(api == null){
			return;
		}
		String clock = gameTimer.getClock();
		for(String name : playerHandler.getPlayers().keySet()){
			Player player = Bukkit.getPlayer(name);
			if(player == null){
				continue;
			}
			PlayerMode mode = playerHandler.getPlayers().get(name);
			if(!api.hasScoreboard(player)){
				api.giveScoreboard(player);
			}
			api.setScoreboardTitle(player, msg + "§l" + type.getName());
			api.setScoreboardValue(player, 1, "§1");
			api.setScoreboardValue(player, 2, msg + "Your Mode:");
			api.setScoreboardValue(player, 3, mode.getColoredName());
			api.setScoreboardValue(player, 4, "§4");
			api.setScoreboardValue(player, 5, PlayerMode.HIDER.getColoredName() + "s" + gry + ": " + hlt +  playerHandler.getPlayerNamesByMode(PlayerMode.HIDER).size());
			api.setScoreboardValue(player, 6, PlayerMode.SEEKER.getColoredName() + "s" + gry + ": " + hlt + playerHandler.getPlayerNamesByMode(PlayerMode.SEEKER).size());
			api.setScoreboardValue(player, 7, "§7");
			api.setScoreboardValue(player, 8, msg + "Game Status:");
			api.setScoreboardValue(player, 9, status.getName());
			api.setScoreboardValue(player,10, "§a");
			api.setScoreboardValue(player,11, msg + "Timer:");
			api.setScoreboardValue(player,12, clock);
			api.setScoreboardValue(player,13, "§d");
		}
	}
	
	public void updateActionBar(){
		if(status != GameStatus.SEEKING) return;
		TitleManagerAPI api = getCore().getIntegrationManager().getTitleManager();
		if(api == null){
			return;
		}
		for(String name : playerHandler.getPlayers().keySet()){
			Player player = Bukkit.getPlayer(name);
			if(player == null){
				continue;
			}
			PlayerMode mode = playerHandler.getPlayers().get(name);
			if(mode == null || mode == PlayerMode.SPECTATOR){
				continue;
			}
			Player closest = null;
			PlayerMode closestMode = null;
			if(mode == PlayerMode.HIDER){
				closestMode = PlayerMode.SEEKER;
				closest = playerHandler.getClosestPlayerInMode(player, closestMode, 100);
			}else if(mode == PlayerMode.SEEKER && settings.canSeekersSeeHiderDistance()){
				closestMode = PlayerMode.HIDER;
				closest = playerHandler.getClosestPlayerInMode(player, closestMode, 100);
			}
			if(closest == null){
				continue;
			}
			long distance = Math.round(player.getLocation().distance(closest.getLocation()));
			api.sendActionbar(player, msg + "Closest " + closestMode.getColoredName() + msg + ": " + closestMode.getColor() + closest.getName() + msg + " (" + distance + " blocks)");
		}
	}
	
	public GameType getType() {
		return type;
	}
	
	public GameStatus getStatus() {
		return status;
	}
	
	public GameTimer getGameTimer() {
		return gameTimer;
	}
	
	public boolean isGameActive(){
		return status == GameStatus.HIDING || status == GameStatus.SEEKING;
	}
	
	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}
	
	public GameTeams getTeams() {
		return teams;
	}
	
	public Map getMap() {
		return map;
	}
	
	public GameSettings getSettings() {
		return settings;
	}
	
	public List<Map> getAllMaps() {
		return allMaps;
	}
	
	public List<String> getAllMapNames(){
		List<String> names = new ArrayList<>();
		for(Map map : allMaps){
			names.add(map.getName());
		}
		return names;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
}
