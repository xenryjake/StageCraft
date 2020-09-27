package com.xenry.stagecraft.survival.hidenseek.player;
import com.sun.istack.internal.NotNull;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameStatus;
import com.xenry.stagecraft.survival.hidenseek.game.GameType;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.Log;
import com.xenry.stagecraft.util.PlayerUtil;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xenry.stagecraft.survival.hidenseek.HM.msg;
import static com.xenry.stagecraft.survival.hidenseek.HM.prefix;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/16/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerHandler extends Handler<Survival,HideNSeekManager> {
	
	final HashMap<String,PlayerMode> players;
	
	public PlayerHandler(HideNSeekManager manager){
		super(manager);
		players = new HashMap<>();
	}
	
	public boolean addPlayer(Profile profile){
		return addPlayer(profile.getPlayer());
	}
	
	public boolean addPlayer(Player player){
		if(manager.getType() == null){
			return false;
		}
		return addPlayer(player, manager.getType().getDefaultPlayerMode());
	}
	
	public boolean addPlayer(@NotNull Player player, @NotNull PlayerMode mode){
		if(player == null || mode == null || players.containsKey(player.getName())
				|| manager.getStatus() == GameStatus.NONE || PlayerUtil.hasItemsInInventory(player)){
			return false;
		}
		players.put(player.getName(), mode);
		sendMessageToPlayers(prefix + mode.getColoredName() + " " + player.getName() + msg + " joined the game!");
		
		applyPlayerState(player);
		if(manager.getStatus() == GameStatus.PRE_GAME){
			player.teleport(manager.getMap().getLobbyPoint());
		}else if(manager.getStatus() == GameStatus.HIDING || manager.getStatus() == GameStatus.SEEKING){
			player.teleport(manager.getMap().getGamePoint());
		}
		
		return true;
	}
	
	public boolean removePlayer(@NotNull Profile profile){
		return removePlayer(profile.getPlayer());
	}
	
	public boolean removePlayer(@NotNull Player player){
		return removePlayer(player, true);
	}
	
	public boolean removePlayer(@NotNull Player player, boolean message){
		if(player == null || !players.containsKey(player.getName())){
			return false;
		}
		PlayerMode mode = players.get(player.getName());
		players.remove(player.getName());
		if(message){
			sendMessageToPlayers(prefix + mode.getColoredName() + " " + player.getName() + msg + " left the game.");
		}
		manager.getTeams().removePlayerFromTeams(player);
		TitleManagerAPI api = getCore().getIntegrationManager().getTitleManager();
		if(api != null){
			api.removeScoreboard(player);
			api.clearActionbar(player);
		}
		player.getInventory().clear();
		PlayerUtil.clearPotionEffects(player);
		World world = Bukkit.getWorld("world");
		if(world != null){
			player.teleport(world.getSpawnLocation());
		}
		player.setGameMode(GameMode.SURVIVAL);
		
		if(players.size() <= 0){
			manager.endGame();
		}
		
		manager.checkGame();
		return true;
	}
	
	public PlayerMode getPlayerMode(@NotNull Profile profile){
		return getPlayerMode(profile.getPlayer());
	}
	
	public PlayerMode getPlayerMode(@NotNull Player player){
		if(player == null){
			return null;
		}
		if(!players.containsKey(player.getName())){
			return null;
		}
		return players.get(player.getName());
	}
	
	public void sendMessageToPlayers(String message){
		for(String name : players.keySet()){
			Player player = Bukkit.getPlayer(name);
			if(player == null){
				continue;
			}
			player.sendMessage(message);
		}
		Log.info(message);
	}
	
	public boolean setMode(@NotNull Player player, @NotNull PlayerMode mode) {
		if(!players.containsKey(player.getName())) {
			return false;
		}
		if(players.get(player.getName()) == mode) {
			return false;
		}
		players.remove(player.getName());
		players.put(player.getName(), mode);
		return applyPlayerState(player);
	}
	
	public boolean isInGame(HumanEntity player){
		return players.containsKey(player.getName());
	}
	
	public boolean applyPlayerState(@NotNull Player player){
		if(!isInGame(player)){
			return false;
		}
		PlayerMode mode = players.get(player.getName());
		
		PlayerUtil.clearPotionEffects(player);
		player.setHealthScaled(false);
		player.setHealth(20);
		player.setFoodLevel(20);
		
		if(manager.getStatus() == GameStatus.NONE){
			return true;
		}
		
		if(mode == PlayerMode.HIDER || mode == PlayerMode.SEEKER){
			player.setGameMode(GameMode.ADVENTURE);
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 255, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 255, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 255, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, Integer.MAX_VALUE, 255, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 255, false, false, false));
		}
		if(mode == PlayerMode.SEEKER){
			player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 255, false, false, false));
		}
		if(!manager.getSettings().isHiderMovementAllowed() && mode == PlayerMode.HIDER && manager.getStatus() == GameStatus.SEEKING){
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, false, false, false));
		}
		if(mode == PlayerMode.SPECTATOR){
			player.setGameMode(GameMode.SPECTATOR);
		}
		
		manager.getTeams().addPlayerToTeam(player, mode);
		
		return true;
	}
	
	public void findPlayer(Player finder, Player found){
		if(manager.getStatus() != GameStatus.SEEKING){
			return;
		}
		if(getPlayerMode(finder) != PlayerMode.SEEKER || getPlayerMode(found) != PlayerMode.HIDER){
			return;
		}
		
		if(manager.getType() == GameType.HIDE_N_SEEK){
			sendMessageToPlayers(prefix + getPlayerMode(finder).getColoredName() + " " + finder.getName() + msg + " found " + getPlayerMode(found).getColoredName() + " " + found.getName() + msg + ".");
			setMode(found, PlayerMode.SEEKER);
			sendMessageToPlayers(prefix + getPlayerMode(found).getColor() + found.getName() + msg + " is now a " + getPlayerMode(found).getColoredName() + msg + ".");
		}else if(manager.getType() == GameType.SARDINES){
			setMode(finder, PlayerMode.HIDER);
		}
		
		manager.checkGame();
	}
	
	public List<String> getPlayerNamesByMode(PlayerMode mode){
		List<String> playerList = new ArrayList<>();
		for(String name : players.keySet()){
			if(players.get(name) == mode){
				playerList.add(name);
			}
		}
		return playerList;
	}
	
	public int getNumberOfPlayersByMode(PlayerMode mode){
		int i = 0;
		for(String name : players.keySet()){
			if(players.get(name) == mode) {
				i++;
			}
		}
		return i;
	}
	
	public int getNumberOfActivePlayers(){
		int i = 0;
		for(String name : players.keySet()){
			if(players.get(name) == PlayerMode.SEEKER || players.get(name) == PlayerMode.HIDER) {
				i++;
			}
		}
		return i;
	}
	
	public Player getClosestPlayerInMode(Player from, PlayerMode mode, double range){
		Player closest = null;
		for(Map.Entry<String,PlayerMode> entry : players.entrySet()){
			if(entry.getValue() != mode || entry.getKey().equals(from.getName())){
				continue;
			}
			Player player = Bukkit.getPlayer(entry.getKey());
			if(player == null){
				continue;
			}
			double dist = from.getLocation().distance(player.getLocation());
			if(dist < range){
				closest = player;
				range = dist;
			}
		}
		return closest;
	}
	
	public HashMap<String,PlayerMode> getPlayers() {
		return players;
	}
	
	public void clearPlayersHashMap(){
		players.clear();
	}
	
}
