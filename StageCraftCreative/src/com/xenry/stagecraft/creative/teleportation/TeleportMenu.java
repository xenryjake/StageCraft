package com.xenry.stagecraft.creative.teleportation;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.ui.Menu;
import com.xenry.stagecraft.ui.MenuContents;
import com.xenry.stagecraft.ui.Slot;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/3/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class TeleportMenu extends Menu<Creative,TeleportationManager> {
	
	private final String command;
	
	public TeleportMenu(TeleportationManager manager, String uuid, String command){
		super(manager, "teleport#" + uuid, "Teleport", 6);
		this.command = command;
	}
	
	@Override
	protected void init(Player player, MenuContents contents) {
		setTitle("/" + command + " <player>");
		contents.fillBorders(getBackgroundItem());
		{
			ItemStack is = new ItemStack(Material.BARRIER);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§cCancel");
			is.setItemMeta(im);
			contents.set(5, 4, Button.closeInventoryButton(is));
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p == player){
				continue;
			}
			Slot slot = contents.firstEmpty();
			if(slot == null){
				break;
			}
			ItemStack is = SkullUtil.getSkullFromOwnerName(p.getName());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§r/" + command + " " + p.getName());
			is.setItemMeta(im);
			contents.set(slot, Button.runCommandButton(is, command + " " + p.getName()));
		}
		contents.fillEmptySlots(getInvisibleItem());
	}
	
}
