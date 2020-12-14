package com.xenry.stagecraft.creative.heads;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class CustomHead extends Head {
	
	public final String b64Value;
	
	public CustomHead(String id, String name, String b64Value, Head.Category category, String...tags) {
		super(id, name, category, tags);
		this.b64Value = b64Value;
	}
	
	@Override
	public ItemStack getItem(){
		ItemStack is = SkullUtil.getSkullFromTextureBase64(b64Value);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§e" + getName());
		is.setItemMeta(im);
		return is;
	}
	
}
