package com.xenry.stagecraft.profile.ui;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.ui.item.Item;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.SkullUtil;
import com.xenry.stagecraft.util.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class MyProfileMenu extends Menu<Core,ProfileManager> {
	
	public MyProfileMenu(ProfileManager manager, String uuid) {
		super(manager, "myProfile#" + uuid, "My Profile", 5);
	}
	
	@Override
	protected void init(Player player, MenuContents contents) {
		contents.fill(getBackgroundItem());
		Profile profile = manager.getProfile(player);
		if(profile == null){
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cError loading profile.");
			is.setItemMeta(im);
			contents.set(2, 4, new Item(is));
			return;
		}
		{
			ItemStack is = SkullUtil.getSkullFromOwnerName(profile.getLatestUsername());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(profile.getDisplayName() + M.msg + "'s Profile");
			im.setLore(Collections.singletonList("§7" + profile.getUUID()));
			is.setItemMeta(im);
			contents.set(1, 4, new Item(is));
		}{
			ItemStack is = new ItemStack(Material.DIAMOND);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(M.msg + "Rank: " + profile.getRank().getColoredName());
			is.setItemMeta(im);
			contents.set(3, 2, new Item(is));
		}{
			ItemStack is = new ItemStack(Material.CLOCK);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(M.msg + "Playtime: " + M.WHITE + TimeUtil.simplerString(profile.getTotalPlaytime()));
			is.setItemMeta(im);
			contents.set(3, 4, new Item(is));
		}{
			boolean online = profile.isOnline();
			ItemStack is = new ItemStack(online ? Material.LIME_DYE : Material.LIGHT_GRAY_DYE);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(online ? "§aOnline" : "§cOffline");
			im.setLore(Collections.singletonList(M.WHITE + TimeUtil.simplerString(profile.getSecondsSinceLastLogin(manager.plugin.getServerName()))));
			is.setItemMeta(im);
			contents.set(3, 6, new Item(is));
		}
	}
	
	@Override
	protected void tick(Player player, MenuContents contents) {
		int state = contents.getProperty("tick", 0);
		contents.setProperty("tick", ++state);
		
		if(state < 10){
			return;
		}
		contents.setProperty("tick", 0);
		
		Profile profile = manager.getProfile(player);
		if(profile == null){
			close(player);
			player.sendMessage(M.error("Error loading profile."));
			return;
		}
		
		{
			ItemStack is = new ItemStack(Material.DIAMOND);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(M.msg + "Rank: " + profile.getRank().getColoredName());
			is.setItemMeta(im);
			contents.set(3, 2, new Item(is));
		}{
			ItemStack is = new ItemStack(Material.CLOCK);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(M.msg + "Playtime: " + M.WHITE + TimeUtil.simplerString(profile.getTotalPlaytime()));
			is.setItemMeta(im);
			contents.set(3, 4, new Item(is));
		}{
			boolean online = profile.isOnline();
			ItemStack is = new ItemStack(online ? Material.LIME_DYE : Material.LIGHT_GRAY_DYE);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(online ? "§aOnline" : "§cOffline");
			im.setLore(Collections.singletonList(M.WHITE + TimeUtil.simplerString(profile.getSecondsSinceLastLogin(manager.plugin.getServerName()))));
			is.setItemMeta(im);
			contents.set(3, 6, new Item(is));
		}
	}
	
}
