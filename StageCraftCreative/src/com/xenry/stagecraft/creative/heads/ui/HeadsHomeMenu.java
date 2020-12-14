package com.xenry.stagecraft.creative.heads.ui;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.heads.Head;
import com.xenry.stagecraft.creative.heads.HeadsManager;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.ui.item.Button;
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
public class HeadsHomeMenu extends Menu<Creative,HeadsManager> {
	
	private final String command;
	
	public HeadsHomeMenu(HeadsManager manager, String uuid, String command){
		super(manager, "heads_home#" + uuid, "Heads (" + manager.countHeads() + ")", 6);
		this.command = command;
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
		}
		for(Head.Category category : Head.Category.values()){
			ItemStack is = category.getIcon();
			ItemMeta im = is.getItemMeta();
			im.setLore(Arrays.asList("§fView the §e" + category.name + "§f category.",
					"§7" + manager.countHeads(category) + " Heads",
					"§7/" + command + " " + category.id));
			is.setItemMeta(im);
			contents.add(Button.runCommandButton(is, command + " " + category.id));
		}
		contents.fillEmptySlots(getInvisibleItem());
	}
	
}
