package com.xenry.stagecraft.creative.heads.ui;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.Head;
import com.xenry.stagecraft.creative.heads.HeadsManager;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/12/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class HeadsCategoryListMenu extends Menu<Creative,HeadsManager> {
	
	public HeadsCategoryListMenu(HeadsManager manager, String uuid){
		super(manager, "heads_home#" + uuid, "Heads (" + manager.countHeads() + ")", 6);
	}
	
	@Override
	protected void init(Player player, MenuContents contents) {
		contents.fillBorders(getBackgroundItem());
		{
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cClose");
			is.setItemMeta(im);
			contents.set(5, 4, Button.closeInventoryButton(is));
		}{
			ItemStack is = new ItemStack(Material.NAME_TAG);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§aView Tags");
			is.setItemMeta(im);
			contents.set(5,5, Button.runCommandButton(is, "heads tags"));
		}{
			ItemStack is = SkullUtil.getSkullFromTextureBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM4ZmI2MzdkNmUxYTdiYThmYTk3ZWU5ZDI5MTVlODQzZThlYzc5MGQ4YjdiZjYwNDhiZTYyMWVlNGQ1OWZiYSJ9fX0=");
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§c❤§r §aView Favorites");
			is.setItemMeta(im);
			contents.set(0, 8, Button.runCommandButton(is, "heads favorites"));
		}
		for(Head.Category category : Head.Category.values()){
			ItemStack is = category.getIcon();
			ItemMeta im = is.getItemMeta();
			im.setLore(Arrays.asList("§fView the §e" + category.name + "§f category.",
					"§7" + manager.countHeads(category) + " Heads",
					"§7/heads " + category.id));
			is.setItemMeta(im);
			contents.add(Button.runCommandButton(is, "heads " + category.id));
		}
		contents.fillEmptySlots(getInvisibleItem());
	}
	
}
