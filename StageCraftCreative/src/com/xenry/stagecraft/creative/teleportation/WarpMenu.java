package com.xenry.stagecraft.creative.teleportation;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.ui.item.Item;
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
public final class WarpMenu extends Menu<Creative,TeleportationManager> {
	
	private final String command;
	
	public WarpMenu(TeleportationManager manager, String uuid, String command) {
		super(manager, "warps#" + uuid, "Warps", 6);
		this.command = command;
	}
	
	@Override
	protected void init(Player player, MenuContents contents) {
		setTitle("/" + command + " <warp>");
		List<Warp> warps = manager.getWarpHandler().getWarps();
		if(warps.size() < 1){
			contents.fill(getBackgroundItem());
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cThere are no available warps.");
			is.setItemMeta(im);
			contents.set(2, 4, new Item(is));
			return;
		}
		Item[] items = new Item[warps.size()];
		for(int i = 0; i < items.length; i++){
			Warp warp = warps.get(i);
			ItemStack is = new ItemStack(Material.ENDER_PEARL);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§e" + warp.getName());
			im.setLore(Collections.singletonList("§f/" + command + " " + warp.getName()));
			is.setItemMeta(im);
			items[i] = Button.runCommandButton(is, command + " " + warp.getName());
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
