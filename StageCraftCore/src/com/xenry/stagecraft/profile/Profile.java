package com.xenry.stagecraft.profile;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.util.time.TimeUtil;
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
public class Profile extends GenericProfile {
	
	@Deprecated
	public Profile(){
		//required for Mongo instantiation
	}
	
	public Profile(UUID uuid, String name, InetAddress address){
		super(uuid);
		
		updateUsernames(name);
		if(address != null){
			updateAddresses(address);
		}
		
		put("lastLogins", new HashMap<String,Long>());
		put("lastLogouts", new HashMap<String,Long>());
		
		put("lastPlaytimeUpdates", new HashMap<String,Long>());
		put("playtimes", new HashMap<String,Long>());
		
		put("rank", Rank.MEMBER.toString());
		put("settings", new HashMap<String,Boolean>());
		put("nick", "none");
	}
	
	public Profile(UUID uuid, String name, InetSocketAddress socketAddress){
		this(uuid, name,socketAddress == null ? null : socketAddress.getAddress());
	}
	
	public Profile(Player player){
		this(player.getUniqueId(), player.getName(), player.getAddress());
	}
	
	public void updateDisplayName(){
		Player player = getPlayer();
		if(player == null){
			return;
		}
		updateDisplayName(player);
	}
	
	private void updateDisplayName(Player player){
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
			InetSocketAddress socketAddress = player.getAddress();
			if(socketAddress != null){
				put("latestAddress", socketAddress.getAddress().toString());
			}
			return getLatestAddress();
		}
	}
	
	public void updateAddresses(InetAddress address){
		if(address == null){
			return;
		}
		put("latestAddress", address.toString());
		List<String> addresses = getAddresses();
		if(!addresses.contains(address.toString())){
			addresses.add(address.toString());
			put("addresses", addresses);
		}
	}
	
	public void updateAddresses(InetSocketAddress socketAddress){
		if(socketAddress == null){
			return;
		}
		updateAddresses(socketAddress.getAddress());
	}
	
	public void updateAddresses(){
		if(!isOnline()){
			return;
		}
		updateAddresses(getPlayer().getAddress());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Long> getLastLogins(){
		Object obj = get("lastLogins");
		if(obj instanceof HashMap){
			return (HashMap<String,Long>)obj;
		}else{
			put("lastLogins", new HashMap<String,Long>());
			return getLastLogins();
		}
	}
	
	public long getLastLogin(String serverName){
		return getLastLogins().getOrDefault(serverName, 0L);
	}
	
	public void setLastLogin(String serverName, long timestamp){
		setLastPlaytimeUpdate(serverName, timestamp);
		HashMap<String,Long> lastLogins = getLastLogins();
		lastLogins.put(serverName, timestamp);
		put("lastLogins", lastLogins);
	}
	
	public long getSecondsSinceLastLogin(String serverName){
		return TimeUtil.nowSeconds() - getLastLogin(serverName);
	}
	
	public long getMostRecentLogin(){
		long value = 0;
		for(Long l : getLastLogins().values()){
			if(l > value){
				value = l;
			}
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Long> getLastLogouts(){
		Object obj = get("lastLogouts");
		if(obj instanceof HashMap){
			return (HashMap<String,Long>)obj;
		}else{
			put("lastLogouts", new HashMap<String,Long>());
			return getLastLogouts();
		}
	}
	
	public long getLastLogout(String serverName){
		return getLastLogouts().getOrDefault(serverName, 0L);
	}
	
	public void setLastLogout(String serverName, long timestamp){
		HashMap<String,Long> lastLogouts = getLastLogouts();
		lastLogouts.put(serverName, timestamp);
		put("lastLogouts", lastLogouts);
	}
	
	public long getSecondsSinceLastLogout(String serverName){
		return TimeUtil.nowSeconds() - getLastLogout(serverName);
	}
	
	public long getMostRecentLogout(){
		long value = 0;
		for(Long l : getLastLogouts().values()){
			if(l > value){
				value = l;
			}
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Long> getLastPlaytimeUpdates(){
		Object obj = get("lastPlaytimeUpdates");
		if(obj instanceof HashMap){
			return (HashMap<String,Long>)obj;
		}else{
			put("lastPlaytimeUpdates", new HashMap<String,Long>());
			return getLastPlaytimeUpdates();
		}
	}
	
	public long getLastPlaytimeUpdate(String serverName){
		return getLastPlaytimeUpdates().getOrDefault(serverName, 0L);
	}
	
	public void setLastPlaytimeUpdate(String serverName, long timestamp){
		HashMap<String,Long> lastPlaytimeUpdates = getLastPlaytimeUpdates();
		lastPlaytimeUpdates.put(serverName, timestamp);
		put("lastPlaytimeUpdates", lastPlaytimeUpdates);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Long> getPlaytimes(){
		Object obj = get("playtimes");
		if(obj instanceof HashMap){
			return (HashMap<String,Long>)obj;
		}else{
			put("playtimes", new HashMap<String,Long>());
			return getPlaytimes();
		}
	}
	
	private long getPlaytimeValue(String serverName){
		return getPlaytimes().getOrDefault(serverName, 0L);
	}
	
	public long getPlaytime(String serverName){
		updatePlaytime(serverName);
		return getPlaytimeValue(serverName);
	}
	
	private void setPlaytime(String serverName, long time){
		HashMap<String,Long> playtimes = getPlaytimes();
		playtimes.put(serverName, time);
		put("playtimes", playtimes);
	}
	
	private void addToPlaytime(String serverName, long time){
		setPlaytime(serverName, getPlaytimeValue(serverName) + time);
	}
	
	public void updatePlaytime(String serverName){
		long now = TimeUtil.nowSeconds();
		long lastUpdate = getLastPlaytimeUpdate(serverName);
		if(lastUpdate > 0L && isOnline()){
			addToPlaytime(serverName, now - lastUpdate);
		}
		setLastPlaytimeUpdate(serverName, now);
	}
	
	public void updateLocalPlaytime(){
		updatePlaytime(Core.getInstance().getServerName());
	}
	
	public long getTotalPlaytime(){
		updateLocalPlaytime();
		long total = 0;
		for(Long l : getPlaytimes().values()){
			total += l;
		}
		return total;
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
	
}
