package com.xenry.stagecraft.ui.item;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class UISkull extends UIItemStack {
	
	//todo: change to allow any texture
	
	public UISkull(String owner){
		super(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) getItemMeta();
		if(meta == null){
			return;
		}
		//noinspection deprecation
		meta.setOwner(owner);
		setItemMeta(meta);
	}
	
	public UISkull(String owner, String name){
		this(owner);
		setName(name);
	}
	
}
