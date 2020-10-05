package com.xenry.stagecraft.hologram;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.hologram.commands.HologramCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HologramManager extends Manager<Core> {
	
	private final DBCollection hologramCollection;
	private final List<Hologram> holograms;
	
	public HologramManager(Core plugin){
		super("Hologram", plugin);
		hologramCollection = plugin.getMongoManager().getCoreCollection("holograms");
		hologramCollection.setObjectClass(Hologram.class);
		holograms = new ArrayList<>();
	}
	
	@Override
	protected void onEnable() {
		registerCommand(new HologramCommand(this));
		download();
		for(Hologram hologram : holograms){
			hologram.spawn();
		}
	}
	
	@Override
	protected void onDisable() {
		saveAllSync();
		for(Hologram hologram : holograms){
			hologram.remove();
		}
	}
	
	/**
	 * Adds and spawns a hologram to the system.
	 *
	 * @param hologram the hologram to add
	 */
	public void addHologram(Hologram hologram){
		holograms.add(hologram);
		hologram.spawn();
	}
	
	/**
	 * Downloads all the holograms of the current server type from the database.
	 * This does NOT remove holograms from the world or spawn new holograms.
	 */
	private void download(){
		holograms.clear();
		DBCursor c = hologramCollection.find(new BasicDBObject("serverName", plugin.getServerName()));
		while(c.hasNext()) {
			holograms.add((Hologram)c.next());
		}
	}
	
	/**
	 * Saves all holograms that have been modified.
	 */
	public void saveAll(){
		for(Hologram holo : holograms){
			if(holo.isModified() && holo.canSave()){
				save(holo);
			}
		}
	}
	
	/**
	 * Saves the specified hologram to the database.
	 *
	 * @param hologram the hologram to save
	 */
	public void save(final Hologram hologram){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> hologramCollection.save(hologram));
	}
	
	/**
	 *  SYNCHRONOUSLY Saves all holograms that have been modified.
	 *  This function should only be used when the server is shutting down.
	 */
	private void saveAllSync(){
		for(Hologram holo : holograms){
			if(holo.isModified() && holo.canSave()){
				saveSync(holo);
			}
		}
	}
	
	/**
	 * SYNCHRONOUSLY Saves the specified hologram to the database.
	 * This function should only be used when the server is shutting down.
	 *
	 * @param hologram hologram to save
	 */
	private void saveSync(Hologram hologram){
		hologramCollection.save(hologram);
	}
	
	/**
	 * Deletes a hologram from the database and server.
	 *
	 * @param hologram the hologram to delete
	 */
	public void delete(final Hologram hologram){
		hologram.remove();
		holograms.remove(hologram);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> hologramCollection.remove(new BasicDBObject("_id", hologram.get("_id"))));
	}
	
	/**
	 * Updates the location of a hologram.
	 *
	 * @param hologram The hologram to update.
	 */
	public void update(Hologram hologram){
		hologram.remove();
		hologram.spawn();
	}
	
	/**
	 * Updates the holograms from the database.
	 */
	public void updateAll(){
		for(Hologram hologram : holograms){
			hologram.remove();
		}
		download();
		for(Hologram hologram : holograms){
			hologram.spawn();
		}
	}
	
	public List<Hologram> getHolograms(){
		return holograms;
	}
	
	public List<String> getAllHologramNames(){
		List<String> names = new ArrayList<>();
		for(Hologram hologram : holograms){
			names.add(hologram.getName());
		}
		return names;
	}
	
	/**
	 * Get the hologram with the specified name. Current server type only.
	 *
	 * @param name the name of the hologram
	 * @return the hologram, or null if no hologram has the specified name
	 */
	public Hologram getHologram(String name){
		for(Hologram hologram : holograms){
			if(hologram.getName().equalsIgnoreCase(name)){
				return hologram;
			}
		}
		return null;
	}
	
	/**
	 * Check if an entity is a hologram
	 * @param entity the entity to check
	 * @return if the entity is a hologram
	 */
	public boolean isHologram(Entity entity){
		if(!(entity instanceof ArmorStand)){
			return false;
		}
		ArmorStand as = (ArmorStand)entity;
		return as.isCustomNameVisible() && !as.isVisible() && !as.hasGravity() && !as.hasBasePlate()
				&& !as.hasArms() && as.isSmall() && as.hasMetadata(Hologram.METADATA_KEY);
	}
	
	@EventHandler
	public void on(EntityInteractEvent e){
		for(Hologram hologram : holograms){
			for(ArmorStand stand : hologram.getArmorStands()){
				if(stand.equals(e.getEntity())){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void on(EntityDamageEvent e){
		for(Hologram hologram : holograms){
			for(ArmorStand stand : hologram.getArmorStands()){
				if(stand == null) continue;
				if(stand.equals(e.getEntity())){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void on(WorldLoadEvent e){
		for(Hologram hologram : holograms){
			if(hologram.getWorldName().equals(e.getWorld().getName())){
				hologram.spawn();
			}
		}
	}
	
	@EventHandler
	public void on(WorldUnloadEvent e){
		for(Hologram hologram : holograms){
			if(hologram.getWorldName().equals(e.getWorld().getName())){
				hologram.remove();
			}
		}
	}
	
}
