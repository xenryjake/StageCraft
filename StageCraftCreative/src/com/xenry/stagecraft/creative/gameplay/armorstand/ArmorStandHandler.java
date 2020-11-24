package com.xenry.stagecraft.creative.gameplay.armorstand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.event.FakeEntityDamageByEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/23/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ArmorStandHandler extends Handler<Creative,GameplayManager> {
	
	public static final Access INTERACT_INVISIBLE = Rank.ADMIN;
	public static final Access CAN_MAKE_POSES = Rank.PREMIUM;
	
	private final HashMap<String,ArmorStandClipboard> clipboards;
	private final DBCollection poseCollection;
	private final List<Pose> poses;
	
	public ArmorStandHandler(GameplayManager manager){
		super(manager);
		clipboards = new HashMap<>();
		poseCollection = manager.getCore().getMongoManager().getCoreCollection("creativeArmorStandPoses");
		poseCollection.setObjectClass(Pose.class);
		poses = new ArrayList<>();
	}
	
	public Pose addPose(Pose pose){
		poses.add(pose);
		savePose(pose);
		return pose;
	}
	
	public void downloadPoses(){
		poses.clear();
		DBCursor c = poseCollection.find();
		while(c.hasNext()){
			poses.add((Pose)c.next());
		}
	}
	
	public void saveAllPoses(){
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> {
			for(Pose pose : poses){
				poseCollection.save(pose);
			}
		});
	}
	
	public void savePose(Pose pose){
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> poseCollection.save(pose));
	}
	
	public void saveAllPosesSync(){
		for(Pose pose : poses){
			savePoseSync(pose);
		}
	}
	
	public void savePoseSync(Pose pose){
		poseCollection.save(pose);
	}
	
	public void deletePose(final Pose pose){
		poses.remove(pose);
		Bukkit.getScheduler().runTaskAsynchronously(manager.plugin, () -> poseCollection.remove(new BasicDBObject("_id", pose.get("_id"))));
	}
	
	public List<Pose> getPoses(){
		return poses;
	}
	
	public List<String> getPoseNameList(){
		List<String> names = new ArrayList<>();
		for(Pose pose : poses){
			names.add(pose.getName());
		}
		//Collections.sort(names);
		return names;
	}
	
	public Pose getPose(String name){
		name = name.toLowerCase();
		for(Pose pose : poses){
			if(pose.getName().equalsIgnoreCase(name)){
				return pose;
			}
		}
		return null;
	}
	
	public boolean checkPerms(Player player, ArmorStand as){
		FakeEntityDamageByEntityEvent fedbee = new FakeEntityDamageByEntityEvent(player, as,
				EntityDamageEvent.DamageCause.CUSTOM, 1);
		manager.plugin.getServer().getPluginManager().callEvent(fedbee);
		return !fedbee.isCancelled();
	}
	
	public ArmorStand getStand(Profile profile){
		Player player = profile.getPlayer();
		Entity entity = player.getTargetEntity(5);
		if(!(entity instanceof ArmorStand)){
			player.sendMessage(M.error("You aren't looking at an armor stand."));
			return null;
		}
		ArmorStand as = (ArmorStand)entity;
		if(as.isMarker() || (as.isInvisible() && !INTERACT_INVISIBLE.has(profile))){
			player.sendMessage(M.error("You can't interact with that armor stand."));
			return null;
		}
		return as;
	}
	
	@Nullable
	public ArmorStandClipboard getClipboard(String uuid){
		return clipboards.getOrDefault(uuid, null);
	}
	
	@Nullable
	public ArmorStandClipboard getClipboard(Player player){
		return getClipboard(player.getUniqueId().toString());
	}
	
	@Nullable
	public ArmorStandClipboard getClipboard(Profile profile){
		return getClipboard(profile.getUUID());
	}
	
	public void setClipboard(String uuid, ArmorStandClipboard clipboard){
		clipboards.put(uuid, clipboard);
	}
	
	public void setClipboard(Player player, ArmorStandClipboard clipboard){
		setClipboard(player.getUniqueId().toString(), clipboard);
	}
	
	public void setClipboard(Profile profile, ArmorStandClipboard clipboard){
		setClipboard(profile.getUUID(), clipboard);
	}
	
}
