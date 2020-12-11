package com.xenry.stagecraft.creative.gameplay.heads;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.inventory.ItemStack;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class PlayerHead extends Head {
	
	public final String playerName;
	
	public PlayerHead(String id, String name, String playerName, Head.Category category, String... tags) {
		super(id, name, category, tags);
		this.playerName = playerName;
	}
	
	@Override
	public ItemStack getItem() {
		return SkullUtil.getSkullFromOwnerName(playerName);
	}
	
}
