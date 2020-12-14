package com.xenry.stagecraft.creative.heads;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class VanillaHead extends Head {
	
	private static final List<VanillaHead> values = new ArrayList<>();
	
	static {
		values.add(new VanillaHead("vanilla:player", "Player", Material.PLAYER_HEAD));
		values.add(new VanillaHead("vanilla:creeper", "Creeper", Material.CREEPER_HEAD));
		values.add(new VanillaHead("vanilla:zombie", "Zombie", Material.ZOMBIE_HEAD));
		values.add(new VanillaHead("vanilla:skeleton", "Skeleton", Material.SKELETON_SKULL));
		values.add(new VanillaHead("vanilla:wither_skeleton", "Wither Skeleton", Material.WITHER_SKELETON_SKULL));
		values.add(new VanillaHead("vanilla:dragon", "Dragon", Material.DRAGON_HEAD));
	}
	
	public final Material material;
	
	private VanillaHead(String id, String name, Material material) {
		super(id, name, Head.Category.VANILLA, "Vanilla");
		this.material = material;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§e" + name + " Head");
		is.setItemMeta(im);
		return is;
	}
	
	public static List<VanillaHead> values(){
		return values;
	}
	
}
