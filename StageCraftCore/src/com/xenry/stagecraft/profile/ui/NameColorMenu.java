package com.xenry.stagecraft.profile.ui;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.ProfileManager;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.ui.item.Item;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/4/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class NameColorMenu extends Menu<Core,ProfileManager> {
	
	public NameColorMenu(ProfileManager manager, String uuid){
		super(manager, "nameColor#" + uuid, "Change Name Color", 1);
	}
	
	@Override
	protected void init(Player player, MenuContents contents) {
		Profile profile = manager.getProfile(player);
		if(profile == null){
			contents.fill(getBackgroundItem());
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cError loading profile.");
			is.setItemMeta(im);
			contents.set(0, 4, new Item(is));
			return;
		}
		contents.set(0, 7, getBackgroundItem());
		{
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cClose");
			is.setItemMeta(im);
			contents.set(0, 8, Button.closeInventoryButton(is));
		}
		for(ChatColor color : profile.getRank().getAvailableColors()){
			ItemStack is = new ItemStack(chatColorToMaterial(color));
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(color + color.getName());
			im.setLore(Collections.singletonList("§f/namecolor " + color.getName()));
			is.setItemMeta(im);
			contents.add(Button.runCommandButton(is, "namecolor " + color.getName()));
		}
		contents.fillEmptySlots(getBackgroundItem());
	}
	
	private Material chatColorToMaterial(ChatColor color){
		if(color == ChatColor.BLACK){
			return Material.BLACK_CONCRETE;
		}else if(color == ChatColor.DARK_BLUE){
			return Material.BLUE_CONCRETE;
		}else if(color == ChatColor.DARK_GREEN){
			return Material.GREEN_CONCRETE;
		}else if(color == ChatColor.DARK_AQUA){
			return Material.CYAN_CONCRETE;
		}else if(color == ChatColor.DARK_RED){
			return Material.RED_CONCRETE;
		}else if(color == ChatColor.DARK_PURPLE){
			return Material.PURPLE_CONCRETE;
		}else if(color == ChatColor.GOLD){
			return Material.ORANGE_CONCRETE;
		}else if(color == ChatColor.GRAY){
			return Material.LIGHT_GRAY_CONCRETE;
		}else if(color == ChatColor.DARK_GRAY){
			return Material.GRAY_CONCRETE;
		}else if(color == ChatColor.BLUE){
			return Material.BLUE_CONCRETE;
		}else if(color == ChatColor.GREEN){
			return Material.LIME_CONCRETE;
		}else if(color == ChatColor.AQUA){
			return Material.LIGHT_BLUE_CONCRETE;
		}else if(color == ChatColor.RED){
			return Material.RED_CONCRETE;
		}else if(color == ChatColor.LIGHT_PURPLE){
			return Material.MAGENTA_CONCRETE;
		}else if(color == ChatColor.YELLOW){
			return Material.YELLOW_CONCRETE;
		}else if(color == ChatColor.WHITE){
			return Material.WHITE_CONCRETE;
		}else{
			return Material.PAPER;
		}
	}
	
}
