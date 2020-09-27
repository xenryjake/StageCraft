package com.xenry.stagecraft.survival.hidenseek.map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/18/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class Map {
	
	private String name;
	private String world;
	private Location gamePoint, lobbyPoint;
	private Boundary gameBoundary, lobbyBoundary;
	
	public Map(String name, World world, Location gamePoint, Location lobbyPoint, Boundary gameBoundary, Boundary lobbyBoundary) {
		this(name, world == null ? null : world.getName(), gamePoint, lobbyPoint, gameBoundary, lobbyBoundary);
	}
	
	public Map(String name, String world, Location gamePoint, Location lobbyPoint, Boundary gameBoundary, Boundary lobbyBoundary) {
		this.name = name;
		this.world = world;
		this.gamePoint = gamePoint;
		this.lobbyPoint = lobbyPoint;
		this.gameBoundary = gameBoundary;
		this.lobbyBoundary = lobbyBoundary;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getWorldName() {
		return world;
	}
	
	public World getWorld(){
		return Bukkit.getWorld(world);
	}
	
	public void setWorld(String world) {
		this.world = world;
	}
	
	public Location getGamePoint() {
		return gamePoint;
	}
	
	public void setGamePoint(Location gamePoint) {
		this.gamePoint = gamePoint;
	}
	
	public Location getLobbyPoint() {
		return lobbyPoint;
	}
	
	public void setLobbyPoint(Location lobbyPoint) {
		this.lobbyPoint = lobbyPoint;
	}
	
	public Boundary getGameBoundary() {
		return gameBoundary;
	}
	
	public void setGameBoundary(Boundary gameBoundary) {
		this.gameBoundary = gameBoundary;
	}
	
	public Boundary getLobbyBoundary() {
		return lobbyBoundary;
	}
	
	public void setLobbyBoundary(Boundary lobbyBoundary) {
		this.lobbyBoundary = lobbyBoundary;
	}
	
}
