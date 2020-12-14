package com.xenry.stagecraft.survival.teleportation;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.ui.item.Item;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/3/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HomeMenu extends Menu<Survival,TeleportationManager> {
	
	private final String command;
	
	public HomeMenu(TeleportationManager manager, String uuid, String command){
		super(manager, "homes#" + uuid, "Homes", 6);
		this.command = command;
	}
	
	@Override
	protected void init(Player player, MenuContents contents) {
		setTitle("/" + command + " <home>");
		List<Home> homes = manager.getHomeHandler().getHomes(player);
		if(homes.size() < 1){
			contents.fill(getBackgroundItem());
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cThere are no available homes.");
			is.setItemMeta(im);
			contents.set(2, 4, new Item(is));
			return;
		}
		Item[] items = new Item[homes.size()];
		for(int i = 0; i < items.length; i++){
			Home home = homes.get(i);
			ItemStack is = SkullUtil.getSkullFromTextureBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y3Y2RlZWZjNmQzN2ZlY2FiNjc2YzU4NGJmNjIwODMyYWFhYzg1Mzc1ZTlmY2JmZjI3MzcyNDkyZDY5ZiJ9fX0=");
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§e" + home.getName());
			im.setLore(Collections.singletonList("§f/" + command + " " + home.getName()));
			is.setItemMeta(im);
			items[i] = Button.runCommandButton(is, command + " " + home.getName());
		}
		contents.pagination().setItems(items);
		contents.pagination().setItemsPerPage(28);
		contents.setProperty("currentPage", -1);
		tick(player, contents);
	}
	
	@Override
	protected void tick(Player player, MenuContents contents) {
		if(contents.getProperty("currentPage", -1) == contents.pagination().current()){
			return;
		}
		contents.setProperty("currentPage", contents.pagination().current());
		
		contents.clear();
		contents.fillBorders(getBackgroundItem());
		{
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cClose");
			is.setItemMeta(im);
			contents.set(5, 4, Button.closeInventoryButton(is));
		}
		if(!contents.pagination().isFirst()){
			ItemStack is = new ItemStack(Material.ARROW);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§a« Page " + (contents.pagination().current()));
			is.setItemMeta(im);
			contents.set(5, 0, new Button(is, (event) -> contents.pagination().prev()));
		}
		if(!contents.pagination().isLast()){
			ItemStack is = new ItemStack(Material.ARROW);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§aPage " + (contents.pagination().current()+2) + " »");
			is.setItemMeta(im);
			contents.set(5, 8, new Button(is, (event) -> contents.pagination().next()));
		}
		
		Item[] items = contents.pagination().getItems();
		for(Item item : items) {
			contents.add(item);
		}
		contents.fillEmptySlots(getInvisibleItem());
	}
	
}
