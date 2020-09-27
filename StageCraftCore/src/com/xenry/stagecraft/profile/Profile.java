package com.xenry.stagecraft.profile;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.util.Vector3D;
import com.xenry.stagecraft.util.time.TimeUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class Profile extends BasicDBObject {
	
	public boolean sessionInJail = false;
	
	@Deprecated
	public Profile(){
		//required for Mongo instantiation
	}
	
	public Profile(Player p){
		put("uuid", p.getUniqueId().toString());
		
		updateUsernames(p.getName());
		updateAddresses(p.getAddress());
		
		put("lastLogin", 0L);
		put("lastLogout", 0L);
		
		put("lastPlaytimeUpdate", 0L);
		put("totalPlaytime", 0L);
		
		put("lastLocationX", 0);
		put("lastLocationY", 0);
		put("lastLocationZ", 0);
		put("lastLocationWorldName", "world");
		
		put("rank", Rank.MEMBER.toString());
		
		put("currency", new HashMap<String,Integer>());
		put("settings", new HashMap<String,Boolean>());
		
		put("nick", "none");
		
		put("hasAcceptedRules", false);
	}
	
	public void updateDisplayName(){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		updateDisplayName(player);
	}
	
	public void updateDisplayName(Player player){
		if(!player.getUniqueId().toString().equals(getUUID())){
			return;
		}
		String displayName = getDisplayName();
		player.setDisplayName(displayName);
		player.setPlayerListName(displayName);
	}
	
	public String getDisplayName(){
		return getRank().getColor() + getColorlessDisplayName();
	}
	
	public String getColorlessDisplayName(){
		String name = getNick();
		return name.equals("none") ? getLatestUsername() : name;
	}
	
	public String getUUID(){
		Object obj = get("uuid");
		if(obj instanceof String){
			return (String)obj;
		}else{
			return "";
		}
	}
	
	public String getLatestUsername(){
		Object obj = get("latestUsername");
		if(obj instanceof String){
			return (String)obj;
		}else{
			Player player = getPlayer();
			if(player == null){
				return "";
			}
			put("latestUsername", player.getName());
			return getLatestUsername();
		}
	}
	
	public void updateUsernames(String newName){
		put("latestUsername", newName);
		List<String> usernames = getUsernames();
		if(!usernames.contains(newName)){
			usernames.add(newName);
			put("usernames", usernames);
		}
	}
	
	public void updateUsernames(){
		if(!isOnline()){
			return;
		}
		updateUsernames(getPlayer().getName());
	}
	
	public String getLatestAddress(){
		Object obj = get("latestAddress");
		if(obj instanceof String){
			return (String)obj;
		}else{
			Player player = getPlayer();
			if(player == null){
				return "";
			}
			updateAddresses();
			return getLatestAddress();
		}
	}
	
	public void updateAddresses(InetSocketAddress socketAddress){
		if(socketAddress == null){
			return;
		}
		InetAddress address = socketAddress.getAddress();
		put("latestAddress", address.toString());
		if(!getAddresses().contains(address.toString())){
			List<String> addresses = getAddresses();
			addresses.add(address.toString());
			put("addresses", addresses);
		}
	}
	
	public void updateAddresses(){
		if(!isOnline()){
			return;
		}
		updateAddresses(getPlayer().getAddress());
	}
	
	public long getLastLogin(){
		Object obj = get("lastLogin");
		if(obj instanceof Long){
			return (Long)obj;
		}else{
			put("lastLogin", 0L);
			return getLastLogin();
		}
	}
	
	public long getSecondsSinceLastLogin(){
		return TimeUtil.nowSeconds() - getLastLogin();
	}
	
	public void setLastLogin(long timestampInSeconds){
		setLastPlaytimeUpdate(timestampInSeconds);
		put("lastLogin", timestampInSeconds);
	}
	
	public long getLastLogout(){
		Object obj = get("lastLogout");
		if(obj instanceof Long){
			return (Long)obj;
		}else{
			put("lastLogout", 0L);
			return getLastLogout();
		}
	}
	
	public long getSecondsSinceLastLogout(){
		return TimeUtil.nowSeconds() - getLastLogout();
	}
	
	public void setLastLogout(long timestampInSeconds){
		put("lastLogout", timestampInSeconds);
	}
	
	public long getLastPlaytimeUpdate(){
		Object obj = get("lastPlaytimeUpdate");
		if(obj instanceof Long){
			return (Long)obj;
		}else{
			put("lastPlaytimeUpdate", 0L);
			return getLastPlaytimeUpdate();
		}
	}
	
	public void setLastPlaytimeUpdate(long seconds){
		put("lastPlaytimeUpdate", seconds);
	}
	
	public void updateTotalPlaytime(){
		long now = TimeUtil.nowSeconds();
		long lastUpdate = getLastPlaytimeUpdate();
		if(lastUpdate > 0L && isOnline()){
			addToTotalPlaytime(now - lastUpdate);
		}
		setLastPlaytimeUpdate(now);
	}
	
	public long getTotalPlaytime(){
		updateTotalPlaytime();
		return getTotalPlaytimeValue();
	}
	
	private long getTotalPlaytimeValue(){
		Object obj = get("totalPlaytime");
		if(obj instanceof Long){
			return (Long)obj;
		}else{
			put("totalPlaytime", 0L);
			return getTotalPlaytimeValue();
		}
	}
	
	private void setTotalPlaytime(long seconds){
		put("totalPlaytime", seconds);
	}
	
	private void addToTotalPlaytime(long seconds){
		setTotalPlaytime(getTotalPlaytimeValue() + seconds);
	}
	
	public Vector3D getLastLocation(){
		int x, y, z;
		Object objX = get("lastLocationX");
		Object objY = get("lastLocationY");
		Object objZ = get("lastLocationZ");
		if(objX instanceof Integer){
			x = (Integer)objX;
		}else{
			put("lastLocationX", 0);
			x = 0;
		}
		if(objY instanceof Integer){
			y = (Integer)objY;
		}else{
			put("lastLocationY", 0);
			y = 0;
		}
		if(objZ instanceof Integer){
			z = (Integer)objZ;
		}else{
			put("lastLocationZ", 0);
			z = 0;
		}
		return new Vector3D(x, y, z);
	}
	
	public String getLastLocationWorldName(){
		Object obj = get("lastLocationWorldName");
		if(obj instanceof String){
			return (String)obj;
		}else{
			put("lastLocationWorldName", "world");
			return getLastLocationWorldName();
		}
	}
	
	public void setLastLocation(int x, int y, int z){
		put("lastLocationX", x);
		put("lastLocationY", y);
		put("lastLocationZ", z);
	}
	
	public void setLastLocationWorldName(String name){
		put("lastLocationWorldName", name);
	}
	
	public boolean isOnline(){
		return getPlayer() != null;
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(UUID.fromString(getUUID()));
	}
	
	public void sendMessage(String message){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.sendMessage(message);
	}
	
	public void sendMessages(String...messages){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.sendMessage(messages);
	}
	
	public void sendMessage(BaseComponent component){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.spigot().sendMessage(component);
	}
	
	public void sendMessage(BaseComponent...components){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		player.spigot().sendMessage(components);
	}
	
	public String getOnlinePlayerName(){
		return getPlayer().getName();
	}
	
	public Rank getRank(){
		Object obj = get("rank");
		if(obj instanceof String){
			try{
				return Rank.valueOf((String)obj);
			}catch(Exception ignored){}
		}
		put("rank", Rank.MEMBER.toString());
		return getRank();
	}
	
	public void setRank(Rank rank){
		put("rank", rank.toString());
	}
	
	public boolean check(Rank rank){
		return getRank().check(rank);
	}
	
	/*@SuppressWarnings("unchecked")
	public HashMap<String,Integer> getCurrency(){
		Object obj = get("currency");
		if(obj instanceof HashMap){
			return (HashMap<String,Integer>)obj;
		}else{
			put("currency", new HashMap<String,Integer>());
			return getCurrency();
		}
	}
	
	public int getCurrency(Currency currency){
		if(!getCurrency().containsKey(currency.toString()))
			setCurrency(currency, currency.getStartValue());
		return getCurrency().get(currency.toString());
	}
	
	public void setCurrency(Currency currency, int value){
		HashMap<String,Integer> currencies = getCurrency();
		currencies.put(currency.toString(), value);
		put("currency", currencies);
	}
	
	public void addCurrency(Currency currency, int amount, boolean notify){
		if(amount == 0) return;
		int newAmount = getCurrency(currency) + amount;
		if(!currency.canBeNegative() && newAmount < 0) newAmount = 0;
		if(notify && isOnline()){
			sendMessage("§b" + currency.getName() + (amount < 0 ? "§c-" : "§a+") + currency.getDisplay(amount));
		}
		setCurrency(currency, newAmount);
	}*/
	
	@SuppressWarnings("unchecked")
	public List<String> getUsernames(){
		Object obj = get("usernames");
		if(obj instanceof List){
			return (List<String>)obj;
		}else{
			List<String> usernames = new ArrayList<>();
			usernames.add(getLatestUsername());
			put("usernames", usernames);
			return getUsernames();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAddresses(){
		Object obj = get("addresses");
		if(obj instanceof List){
			return (List<String>)obj;
		}else{
			List<String> addresses = new ArrayList<>();
			String latest = getLatestAddress();
			if(!latest.isEmpty()){
				addresses.add(latest);
			}
			put("addresses", addresses);
			return getAddresses();
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Boolean> getSettings(){
		Object obj = get("settings");
		if(obj instanceof HashMap){
			return (HashMap<String,Boolean>)obj;
		}else{
			put("settings", new HashMap<String,Boolean>());
			return getSettings();
		}
	}
	
	public boolean getSetting(Setting setting){
		if(!getSettings().containsKey(setting.getID())) {
			setSetting(setting, setting.getDefaultValue());
		}
		return getSettings().get(setting.getID());
	}
	
	public void setSetting(Setting setting, boolean value) {
		HashMap<String,Boolean> settings = getSettings();
		settings.put(setting.getID(), value);
		put("settings", settings);
	}
	
	public String getNick(){
		Object obj = get("nick");
		if(obj instanceof String){
			return (String)obj;
		}else{
			put("nick", "none");
			return getNick();
		}
	}
	
	public void setNick(String nick){
		put("nick", nick);
	}
	
	public boolean hasAcceptedRules(){
		Object obj = get("hasAcceptedRules");
		if(obj instanceof Boolean){
			return (Boolean)obj;
		}else{
			put("hasAcceptedRules", false);
			return hasAcceptedRules();
		}
	}
	
	public void setHasAcceptedRules(boolean hasAcceptedRules){
		put("hasAcceptedRules", hasAcceptedRules);
	}
	
}
