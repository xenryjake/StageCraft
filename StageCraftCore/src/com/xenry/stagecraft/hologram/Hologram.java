package com.xenry.stagecraft.hologram;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.chat.emotes.Emote;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Hologram extends BasicDBObject {
	
	public static final String METADATA_KEY = "SC-HOLOGRAM";
	public static final FixedMetadataValue METADATA_VALUE = new FixedMetadataValue(Core.getInstance(), 0);
	
	private ArmorStand[] armorStands;
	private boolean modified = false, save = true;
	
	@Deprecated
	public Hologram() {
		// mongo requires
	}
	
	/**
	 * Create a hologram
	 * @param name name of the hologram
	 * @param location location of the hologram
	 * @param save whether or not the hologram is saved to the database
	 */
	public Hologram(String serverName, String name, Location location, boolean save){
		put("serverName", serverName);
		put("name", name);
		World world = location.getWorld();
		if(world != null){
			put("world", world.getName());
		}else{
			put("world", "world");
		}
		put("location", new double[]{location.getX(), location.getY(), location.getZ()});
		put("lines", new ArrayList<String>());
		modified = true;
		this.save = save;
	}
	
	/**
	 * Create a hologram
	 * @param name name of the hologram
	 * @param location whether or not the hologram is saved to the database
	 */
	public Hologram(String serverName, String name, Location location){
		this(serverName, name, location, true);
	}
	
	/**
	 * Gets the Server Name
	 *
	 * @return the server name
	 */
	public String getServerName(){
		Object obj = get("serverName");
		if(obj instanceof String){
			return (String)obj;
		}else{
			put("serverName", "NONE");
			return getServerName();
		}
	}
	
	/**
	 * Returns if the hologram has been modified.
	 *
	 * @return if the hologram has been modified
	 */
	public boolean isModified() {
		return modified;
	}
	
	/**
	 * Sets the hologram modified state.
	 *
	 * @param modified the new value for modified
	 */
	public void setModified(boolean modified){
		this.modified = modified;
	}
	
	/**
	 * Returns if the hologram should be saved or not.
	 *
	 * @return if the hologram should be saved
	 */
	public boolean canSave() {
		return save;
	}
	
	public ArmorStand[] getArmorStands() {
		return armorStands;
	}
	
	/**
	 * Spawns the hologram's ArmorStands
	 */
	public void spawn(){
		if(getWorld() == null){
			return;
		}
		if(armorStands != null){
			for(ArmorStand stand : armorStands){
				if(!stand.isDead()) stand.remove();
			}
		}
		List<String> lines = getLines();
		armorStands = new ArmorStand[lines.size()];
		Location location = getLocation();
		World world = location.getWorld();
		if(world == null){
			throw new IllegalArgumentException("Location#getWorld cannot be null");
		}
		for(int i = armorStands.length; i > 0; i--){
			int j = armorStands.length - i;
			armorStands[j] = world.spawn(location, ArmorStand.class);
			armorStands[j].setCustomName(Emote.replaceAllEmotes(lines.get(i-1), ChatColor.WHITE));
			armorStands[j].setCustomNameVisible(true);
			armorStands[j].setVisible(false);
			armorStands[j].setGravity(false);
			armorStands[j].setBasePlate(false);
			armorStands[j].setArms(false);
			armorStands[j].setSmall(true);
			armorStands[j].setMarker(true);
			/*if(j > 0){
				armorStands[j].setPassenger(armorStands[j - 1]);
			}*/
			armorStands[j].setMetadata(METADATA_KEY, METADATA_VALUE);
			location.add(0, 0.25, 0);
		}
	}
	
	/**
	 * Removes the hologram's ArmorStands
	 */
	public void remove(){
		if(armorStands == null) return;
		for(ArmorStand as : armorStands) {
			if(as == null) continue;
			as.remove();
		}
		armorStands = null;
	}
	
	public String getName(){
		return (String) get("name");
	}
	
	public String getWorldName(){
		return (String) get("world");
	}
	
	/**
	 * Gets the actual World object from the world name.
	 *
	 * @return the World object, or null if the world is not loaded
	 */
	public World getWorld(){
		return Bukkit.getWorld(getWorldName());
	}
	
	public double[] getLocationArray(){
		Object obj = get("location");
		if(obj instanceof double[]) return (double[])obj;
		BasicDBList list = (BasicDBList) get("location");
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++){
			try {
				array[i] = Double.parseDouble(list.get(i).toString());
			}catch(NumberFormatException ex){
				return new double[]{0, 0, 0};
			}
		}
		return array;
	}
	
	/**
	 * Converts the location text/array values to a Location.
	 *
	 * @return the converted Location
	 */
	public Location getLocation(){
		double[] locationArray = getLocationArray();
		return new Location(getWorld(), locationArray[0], locationArray[1], locationArray[2]);
	}
	
	/**
	 * Updates the hologram's location to the specified value.
	 *
	 * @param location the new location of the hologram
	 */
	public void setLocation(Location location){
		World world = location.getWorld();
		if(world == null){
			throw new IllegalArgumentException("Location#getWorld cannot be null");
		}
		put("world", world.getName());
		put("location", new double[]{location.getX(), location.getY(), location.getZ()});
		
		this.modified = true;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getLines(){
		return (List<String>) get("lines");
	}
	
	/**
	 * Adds a line of text to the list at the specified index.
	 *
	 * @param text the text to add
	 * @param index the index at which to add the text
	 * @throws IndexOutOfBoundsException if the index is out of bounds (>size of list or <0)
	 */
	public void addLine(String text, int index){
		List<String> lines = getLines();
		lines.add(index, text);
		put("lines", lines);
		modified = true;
	}
	
	/**
	 * Adds a line of text to the end of the list.
	 *
	 * @param text the text to add
	 */
	public void addLine(String text){
		addLine(text, getLines().size());
		modified = true;
	}
	
	/**
	 * Removes the line at line index.
	 *
	 * @param index the index of the line to remove
	 * @throws IndexOutOfBoundsException if index is out of bounds (greater than or equal to the size of the list)
	 */
	public void removeLine(int index){
		List<String> lines = getLines();
		lines.remove(index);
		put("lines", lines);
		modified = true;
	}
	
	/**
	 * Replaces the line at index with the new specified text.
	 *
	 * @param index the index to replace
	 * @param text the new text to replace the old text with
	 * @throws IndexOutOfBoundsException if the index is out of bounds (greater than or equal to the size of the list)
	 */
	public void editLine(int index, String text){
		List<String> lines = getLines();
		lines.remove(index);
		lines.add(index, text);
		put("lines", lines);
		modified = true;
	}
	
}
