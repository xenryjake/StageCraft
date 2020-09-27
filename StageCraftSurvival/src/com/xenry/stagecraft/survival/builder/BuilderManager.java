package com.xenry.stagecraft.survival.builder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.survival.builder.commands.BuilderAdminCommand;
import com.xenry.stagecraft.survival.builder.commands.BuilderCommand;
import com.xenry.stagecraft.survival.builder.commands.BuilderGameModeCommand;
import com.xenry.stagecraft.survival.builder.handlers.BlacklistHandler;
import com.xenry.stagecraft.survival.builder.handlers.PlayerLimitHandler;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.NumberUtil;
import com.xenry.stagecraft.util.PlayerUtil;
import com.xenry.stagecraft.survival.Survival;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public class BuilderManager extends Manager<Survival> {
	
	private final DBCollection buildAreaCollection;
	private final List<BuildArea> buildAreas;
	private final List<String> builders;
	private final BlacklistHandler blacklistHandler;
	private final PlayerLimitHandler playerLimitHandler;
	
	private boolean builderModeEnabled = true;
	
	public BuilderManager(Survival plugin){
		super("Builder", plugin);
		buildAreaCollection = plugin.getCore().getMongoManager().getCoreCollection("buildAreas");
		buildAreaCollection.setObjectClass(BuildArea.class);
		buildAreas = new ArrayList<>();
		builders = new ArrayList<>();
		blacklistHandler = new BlacklistHandler(this);
		playerLimitHandler = new PlayerLimitHandler(this);
	}
	
	@Override
	protected void onEnable() {
		registerListener(blacklistHandler);
		registerListener(playerLimitHandler);
		
		registerCommand(new BuilderCommand(this));
		registerCommand(new BuilderAdminCommand(this));
		registerCommand(new BuilderGameModeCommand(this));
		
		downloadBuildAreas();
		//addBuildArea(new BuildArea("default", "world", -4499, 301, -4119, 602));
	}
	
	@Override
	protected void onDisable() {
		saveAllBuildAreasSync();
	}
	
	public List<BuildArea> getBuildAreas(){
		return buildAreas;
	}
	
	public boolean addBuildArea(BuildArea area){
		for(BuildArea a : buildAreas){
			if(area.getName().equalsIgnoreCase(a.getName())){
				return false;
			}
		}
		buildAreas.add(area);
		saveBuildArea(area);
		return true;
	}
	
	public BuildArea getBuildArea(String name){
		for(BuildArea area : buildAreas){
			if(area.getName().equalsIgnoreCase(name)){
				return area;
			}
		}
		return null;
	}
	
	public List<String> getBuildAreaNames(){
		List<String> names = new ArrayList<>();
		for(BuildArea area : buildAreas){
			names.add(area.getName());
		}
		return names;
	}
	
	public void downloadBuildAreas(){
		buildAreas.clear();
		DBCursor c = buildAreaCollection.find();
		while(c.hasNext()) {
			buildAreas.add((BuildArea)c.next());
		}
	}
	
	public void saveAllBuildAreas(){
		for(BuildArea area : buildAreas){
			saveBuildArea(area);
		}
	}
	
	public void saveBuildArea(BuildArea area){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> buildAreaCollection.save(area));
	}
	
	public void saveAllBuildAreasSync(){
		for(BuildArea area : buildAreas){
			saveBuildAreaSync(area);
		}
	}
	
	public void saveBuildAreaSync(BuildArea area){
		buildAreaCollection.save(area);
	}
	
	public void removeBuildArea(BuildArea area){
		buildAreas.remove(area);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> buildAreaCollection.remove(new BasicDBObject("_id", area.get("_id"))));
	}
	
	public List<String> getBuilders() {
		return builders;
	}
	
	public boolean isBuilder(Profile profile){
		return isBuilder(profile.getPlayer());
	}
	
	public boolean isBuilder(Player player){
		return builders.contains(player.getName());
	}
	
	public void addBuilder(Profile profile){
		addBuilder(profile.getPlayer());
	}
	
	public void addBuilder(Player player){
		player.getInventory().clear();
		player.setGameMode(GameMode.CREATIVE);
		PlayerUtil.clearPotionEffects(player);
		builders.add(player.getName());
	}
	
	public void removeBuilder(Profile profile){
		removeBuilder(profile.getPlayer());
	}
	
	public void removeBuilder(Player player){
		if(!builders.contains(player.getName())){
			return;
		}
		player.getInventory().clear();
		player.setGameMode(GameMode.SURVIVAL);
		PlayerUtil.clearPotionEffects(player);
		builders.remove(player.getName());
	}
	
	public List<BuildArea> getBuildAreasAtLocation(String world, int x, int z){
		List<BuildArea> areas = new ArrayList<>();
		for(BuildArea area : buildAreas){
			if(!area.getWorld().equalsIgnoreCase(world)){
				continue;
			}
			if(NumberUtil.isIntWithin(x, area.getAX(), area.getBX()) && NumberUtil.isIntWithin(z, area.getAZ(), area.getBZ())){
				areas.add(area);
			}
		}
		return areas;
	}
	
	public List<BuildArea> getBuildAreasAtLocation(Location location){
		return getBuildAreasAtLocation(location.getWorld() == null ? null : location.getWorld().getName(), location.getBlockX(), location.getBlockZ());
	}
	
	public List<BuildArea> getBuildAreasAtLocation(Player player){
		return getBuildAreasAtLocation(player.getLocation());
	}
	
	public List<BuildArea> getBuildAreasAtLocation(Block block){
		return getBuildAreasAtLocation(block.getLocation());
	}
	
	public boolean isInBuildArea(String world, int x, int z){
		for(BuildArea area : buildAreas){
			if(!area.getWorld().equalsIgnoreCase(world)) continue;
			if(NumberUtil.isIntWithin(x, area.getAX(), area.getBX()) && NumberUtil.isIntWithin(z, area.getAZ(), area.getBZ())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isInBuildArea(Location location){
		return isInBuildArea(location.getWorld() == null ? null : location.getWorld().getName(), location.getBlockX(), location.getBlockZ());
	}
	
	public boolean isInBuildArea(Player player){
		return isInBuildArea(player.getLocation());
	}
	
	public boolean isInBuildArea(Block block){
		return isInBuildArea(block.getLocation());
	}
	
	public BlacklistHandler getBlacklistHandler() {
		return blacklistHandler;
	}
	
	public boolean isBuilderModeEnabled() {
		return builderModeEnabled;
	}
	
	public void setBuilderModeEnabled(boolean builderModeEnabled) {
		if(!builderModeEnabled){
			for(String name : new ArrayList<>(builders)){
				Player player = Bukkit.getPlayer(name);
				if(player == null){
					continue;
				}
				removeBuilder(player);
				player.sendMessage(M.err + "Builder mode is no longer available.");
			}
		}
		this.builderModeEnabled = builderModeEnabled;
	}
	
	public boolean isValidBuilderLocation(Player player){
		if(!builders.contains(player.getName())){
			return true;
		}
		if(!isInBuildArea(player)){
			removeBuilder(player);
			player.sendMessage(M.err + "Your builder mode was deactivated because you left the build area.");
			return false;
		}
		return true;
	}
	
	public boolean isValidNonBuilderLocation(Player player){
		if(builders.contains(player.getName()) || plugin.getHideNSeekManager().getPlayerHandler().isInGame(player)){
			return true;
		}
		List<BuildArea> areas = getBuildAreasAtLocation(player);
		for(BuildArea area : areas){
			if(!area.allowsNonBuilderInteraction()){
				player.sendMessage(M.err + "You can't do that in a build area unless you are in builder mode. Type /builder to enter builder mode.");
				return false;
			}
		}
		return true;
	}
	
}
