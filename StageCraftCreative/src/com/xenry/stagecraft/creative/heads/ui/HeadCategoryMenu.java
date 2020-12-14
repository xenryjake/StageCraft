package com.xenry.stagecraft.creative.heads.ui;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.Head;
import com.xenry.stagecraft.creative.heads.HeadsManager;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.ui.item.Item;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class HeadCategoryMenu extends Menu<Creative,HeadsManager> {
	
	private final Head.Category category;
	private final String command;
	
	public HeadCategoryMenu(HeadsManager manager, Head.Category category, String uuid, String command){
		super(manager, "head-category-" + category.id + "#" + uuid, "Heads: " + category.name + " (" + manager.countHeads(category) + ")", 6);
		this.category = category;
		this.command = command;
	}
	
	@Override
	protected void init(Player player, MenuContents contents) {
		List<Head> heads = manager.getHeadsByCategory(category);
		if(heads.size() < 1){
			contents.fill(getBackgroundItem());
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cThere are no available heads.");
			is.setItemMeta(im);
			contents.set(2, 4, new Item(is));
			return;
		}
		contents.pagination().setEmptyItems(heads.size());
		contents.pagination().setItemsPerPage(28);
		contents.setProperty("currentPage", -1);
		tick(player, contents);
	}
	
	@Override
	protected void tick(Player player, MenuContents contents) {
		if(contents.getProperty("currentPage", -1) == contents.pagination().current()
				&& !manager.hasFavoriteUpdate(player)){
			return;
		}
		contents.setProperty("currentPage", contents.pagination().current());
		manager.removeFavoriteUpdate(player);
		
		contents.clear();
		contents.fillBorders(getBackgroundItem());
		{
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cClose");
			is.setItemMeta(im);
			contents.set(5, 4, Button.closeInventoryButton(is));
		}{
			ItemStack is = new ItemStack(Material.ARROW);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§a« View Categories");
			is.setItemMeta(im);
			contents.set(5, 3, Button.runCommandButton(is, command));
		}
		if(!contents.pagination().isFirst()){
			{
				ItemStack is = SkullUtil.getSkullFromTextureBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE4YTJkZDViZWYwYjA3M2IxMzI3MWE3ZWViOWNmZWE3YWZlODU5M2M1N2E5MzgyMWU0MzE3NTU3MjQ2MTgxMiJ9fX0=");
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("§a« First Page");
				is.setItemMeta(im);
				contents.set(5, 0, new Button(is, (event) -> contents.pagination().first()));
			}{
				ItemStack is = SkullUtil.getSkullFromTextureBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0=");
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("§a« Page " + (contents.pagination().current()));
				is.setItemMeta(im);
				contents.set(5, 1, new Button(is, (event) -> contents.pagination().prev()));
			}
		}
		if(!contents.pagination().isLast()){
			{
				ItemStack is = SkullUtil.getSkullFromTextureBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllY2NjNWMxYzc5YWE3ODI2YTE1YTdmNWYxMmZiNDAzMjgxNTdjNTI0MjE2NGJhMmFlZjQ3ZTVkZTlhNWNmYyJ9fX0=");
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("§aPage " + (contents.pagination().current()+2) + " »");
				is.setItemMeta(im);
				contents.set(5, 7, new Button(is, (event) -> contents.pagination().next()));
			}{
				ItemStack is = SkullUtil.getSkullFromTextureBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5ZjI4MzMyYmNjMzQ5ZjQyMDIzYzI5ZTZlNjQxZjRiMTBhNmIxZTQ4NzE4Y2FlNTU3NDY2ZDUxZWI5MjIifX19");
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("§aLast Page »");
				is.setItemMeta(im);
				contents.set(5, 8, new Button(is, (event) -> contents.pagination().last()));
			}
		}
		
		List<Head> heads = manager.getHeadsByCategory(category);
		for(int i = contents.pagination().currentStart(); i < contents.pagination().currentEnd(); i++){
			Head head = heads.get(i);
			boolean favorite = manager.getPlayerFavorites(player).isFavorite(head.id);
			contents.add(manager.getSpawnHeadButton(head, favorite));
		}
		contents.fillEmptySlots(getInvisibleItem());
	}
	
}
