package com.xenry.stagecraft.profile;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.util.time.TimeUtil;
import net.md_5.bungee.api.ChatColor;
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
	
	private boolean afk = false;
	private boolean vanished = false;
	
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
		
		put("ranks", new HashMap<String,Long>()); // holds ranks and timestamp at which they expire (-1 is never)
		
		put("settings", new HashMap<String,Boolean>());
		put("nick", "none");
		put("nameColor", Rank.DEFAULT.getColor().getName());
		
		put("hasAcceptedRules", false);
	}
	
	public Profile(UUID uuid, String name, InetSocketAddress socketAddress){
		this(uuid, name,socketAddress == null ? null : socketAddress.getAddress());
	}
	
	public Profile(Player player){
		this(player.getUniqueId(), player.getName(), player.getAddress());
	}
	
	public boolean isAFK() {
		return afk;
	}
	
	public void setAFK(boolean afk) {
		this.afk = afk;
	}
	
	public boolean isVanished() {
		return vanished;
	}
	
	public void setVanished(boolean vanished) {
		this.vanished = vanished;
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
		return getNameColor() + getColorlessDisplayName();
	}
	
	public String getColorlessDisplayName(){
		String name = getNick();
		return name.equals("none") ? getLatestUsername() : ChatColor.stripColor(name);
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
		return getLastLogins().getOrDefault(serverName, -1L);
	}
	
	public void setLastLogin(String serverName, long timestamp){
		setLastPlaytimeUpdate(serverName, timestamp);
		HashMap<String,Long> lastLogins = getLastLogins();
		lastLogins.put(serverName, timestamp);
		put("lastLogins", lastLogins);
	}
	
	public long getSecondsSinceLastLogin(String serverName){
		long last = getLastLogin(serverName);
		if(last <= 0){
			return -1;
		}
		return TimeUtil.nowSeconds() - last;
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
		return getLastLogouts().getOrDefault(serverName, -1L);
	}
	
	public void setLastLogout(String serverName, long timestamp){
		HashMap<String,Long> lastLogouts = getLastLogouts();
		lastLogouts.put(serverName, timestamp);
		put("lastLogouts", lastLogouts);
	}
	
	/**
	 * Get the number of seconds since the player last logged out of a specified server
	 * @param serverName the server
	 * @return the number of seconds
	 */
	public long getSecondsSinceLastLogout(String serverName){
		long last = getLastLogout(serverName);
		if(last <= 0){
			return -1;
		}
		return TimeUtil.nowSeconds() - last;
	}
	
	/**
	 * Get the timestamp (in seconds) of the most recent server logout
	 * @return the timestamp in seconds
	 */
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
	private HashMap<String,Long> getLastPlaytimeUpdates(){
		Object obj = get("lastPlaytimeUpdates");
		if(obj instanceof HashMap){
			return (HashMap<String,Long>)obj;
		}else{
			put("lastPlaytimeUpdates", new HashMap<String,Long>());
			return getLastPlaytimeUpdates();
		}
	}
	
	private long getLastPlaytimeUpdate(String serverName){
		return getLastPlaytimeUpdates().getOrDefault(serverName, 0L);
	}
	
	private void setLastPlaytimeUpdate(String serverName, long timestamp){
		HashMap<String,Long> lastPlaytimeUpdates = getLastPlaytimeUpdates();
		lastPlaytimeUpdates.put(serverName, timestamp);
		put("lastPlaytimeUpdates", lastPlaytimeUpdates);
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String,Long> getPlaytimes(){
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
	
	/**
	 * Get the playtime on a specific server of the user, in seconds
	 * @param serverName the name of the server
	 * @return the number of seconds
	 */
	public long getPlaytime(String serverName){
		if(serverName.equalsIgnoreCase(Core.getInstance().getServerName())){
			updateLocalPlaytime();
		}
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
	
	public void updateLocalPlaytime(){
		String serverName = Core.getInstance().getServerName();
		long now = TimeUtil.nowSeconds();
		long lastUpdate = getLastPlaytimeUpdate(serverName);
		if(lastUpdate > 0L && isOnline()){
			addToPlaytime(serverName, now - lastUpdate);
		}
		setLastPlaytimeUpdate(serverName, now);
	}
	
	/**
	 * Get the total network-wide playtime of the user, in seconds
	 * @return the number of seconds
	 */
	public long getTotalPlaytime(){
		updateLocalPlaytime();
		long total = 0;
		for(Long l : getPlaytimes().values()){
			total += l;
		}
		return total;
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String,Long> getRankExpiries(){
		Object obj = get("ranks");
		if(obj instanceof HashMap){
			return (HashMap<String,Long>)obj;
		}else{
			put("ranks", new HashMap<String,Long>());
			return getRankExpiries();
		}
	}
	
	/**
	 * Get the timestamp (in seconds) the rank will expire.
	 * This method will trigger rank expiration.
	 * @param rank the rank to check
	 * @return the timestamp, in seconds. -1 for never
	 */
	public long getExpiry(Rank rank){
		HashMap<String,Long> rankMap = getRankExpiries();
		if(!rankMap.containsKey(rank.name())){
			return 0L;
		}
		long expiry = rankMap.getOrDefault(rank.name(), 0L);
		if(expiry == -1L){
			return -1L;
		}
		long now = TimeUtil.nowSeconds();
		if(expiry <= now){
			setRankExpiry(rank, 0L, true);
			return 0L;
		}else{
			return expiry;
		}
	}
	
	/**
	 * Get the number of seconds until the given rank expires.
	 * This method will trigger rank expiration.
	 * @param rank the rank to check
	 * @return the number of seconds until the rank expires, -1 for never
	 */
	public long getSecondsUntilExpiry(Rank rank){
		HashMap<String,Long> rankMap = getRankExpiries();
		if(!rankMap.containsKey(rank.name())){
			return 0L;
		}
		long expiry = rankMap.getOrDefault(rank.name(), 0L);
		if(expiry == -1L){
			return -1L;
		}
		long now = TimeUtil.nowSeconds();
		if(expiry <= now){
			setRankExpiry(rank, 0L, true);
			return 0L;
		}else{
			return expiry - now;
		}
	}
	
	/**
	 * Get a list of the ranks the players has.
	 * This method will trigger rank expiration.
	 * @return the list of ranks the player has
	 */
	public List<Rank> getRanks(){
		HashMap<String,Long> rankMap = getRankExpiries();
		List<Rank> ranks = new ArrayList<>();
		for(Map.Entry<String,Long> entry : rankMap.entrySet()){
			Rank rank;
			try{
				rank = Rank.valueOf(entry.getKey());
			}catch(Exception ex){
				continue;
			}
			long expiry = entry.getValue();
			if(expiry != -1L && expiry <= TimeUtil.nowSeconds()){
				setRankExpiry(rank, 0L, true);
				return getRanks();
			}else{
				ranks.add(rank);
			}
		}
		ranks.add(Rank.DEFAULT);
		return ranks;
	}
	
	/**
	 * Get the highest-weighted rank the player has.
	 * This method will trigger rank expiration.
	 * @return the highest-weighted rank
	 */
	public Rank getMainRank(){
		Rank bestRank = Rank.DEFAULT;
		for(Rank rank : getRanks()){
			if(rank.getWeight() > bestRank.getWeight()){
				bestRank = rank;
			}
		}
		return bestRank;
	}
	
	/**
	 * Check if the player has a rank explicitly assigned to them. This should
	 * not be used for permission checks as it does not consider rank inheritance.
	 * This method will trigger rank expiration.
	 * @param rank the rank to check
	 * @return if the player has the rank
	 */
	public boolean hasRankExplicit(Rank rank){
		return getRanks().contains(rank);
	}
	
	/**
	 * Check if the user has Access to this rank.
	 * This method will trigger rank expiration.
	 * @param rank the rank to check
	 * @return if the user has access
	 */
	public boolean check(Rank rank){
		for(Rank userRank : getRanks()){
			if(userRank.check(rank)){
				return true;
			}
		}
		return false;
	}
	
	private void setRankExpiry(Rank rank, long expiry, boolean autoExpire){
		if(rank == Rank.DEFAULT){
			return;
		}
		HashMap<String,Long> rankMap = getRankExpiries();
		ProfileRanksUpdateEvent.Action action = null;
		if(expiry != -1L && expiry <= TimeUtil.nowSeconds()){
			if(rankMap.containsKey(rank.name())){
				action = autoExpire ? ProfileRanksUpdateEvent.Action.EXPIRE
						: ProfileRanksUpdateEvent.Action.REMOVE;
			}
			rankMap.remove(rank.name());
		}else{
			if(!rankMap.containsKey(rank.name())){
				action = ProfileRanksUpdateEvent.Action.ADD;
			}
			rankMap.put(rank.name(), expiry);
		}
		put("ranks", rankMap);
		if(action != null){
			ProfileRanksUpdateEvent.Action finalAction = action;
			coreProfileManager.plugin.getServer().getScheduler().runTask(coreProfileManager.plugin, () ->
					coreProfileManager.plugin.getServer().getPluginManager().callEvent(
							new ProfileRanksUpdateEvent(this, rank, finalAction)));
		}
	}
	
	/**
	 * Permanently add a rank, unless later removed manually
	 * @param rank the rank to add
	 */
	public void addRankPermanent(Rank rank){
		setRankExpiry(rank, -1L, false);
	}
	
	/**
	 * Increase (or decrease) the timestamp of rank expiry by a given number of seconds.
	 * If the user does not have this rank, the rank will be given to the user for the number of seconds.
	 * @param rank the rank to adjust
	 * @param adjustment the amount to adjust the expiry by
	 */
	public void rankTemporarily(Rank rank, long adjustment){
		HashMap<String,Long> rankMap = getRankExpiries();
		if(!rankMap.containsKey(rank.name())){
			if(adjustment > 0){
				setRankExpiry(rank, TimeUtil.nowSeconds() + adjustment, false);
			}
			return;
		}
		long current = rankMap.get(rank.name());
		long expiry = current + adjustment;
		if(expiry > 0){
			setRankExpiry(rank, expiry, false);
		}else{
			setRankExpiry(rank, 0L, false);
		}
	}
	
	/**
	 * Immediately remove a rank from a user.
	 * @param rank the rank to remove
	 */
	public void removeRank(Rank rank){
		setRankExpiry(rank, 0L, false);
	}
	
	/**
	 * Get a list of known usernames for a player.
	 * Only includes names the user has logged into the server with.
	 * @return the list of names
	 */
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
	
	/**
	 * Get a list of known addresses for a player.
	 * @return the list of addresses
	 */
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
	
	/*public ChatColor getNameColor(){
		Object obj = get("nameColor");
		if(obj instanceof String){
			ChatColor color;
			try{
				color = ChatColor.of((String)obj);
			}catch(Exception ex){
				Log.warn(getLatestUsername() + "'s name color is invalid in the database.");
				setNameColor(getMainRank().getColor());
				return getMainRank().getColor();
			}
			if(!getMainRank().getAvailableColors().contains(color)){
				setNameColor(getMainRank().getColor());
				return getMainRank().getColor();
			}
			return color;
		}else{
			put("nameColor", getMainRank().getColor().getName());
			return getNameColor();
		}
	}*/
	
	public ChatColor getNameColor(){
		return getMainRank().getColor();
	}
	
	public void setNameColor(ChatColor color){
		put("nameColor", color.getName());
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
