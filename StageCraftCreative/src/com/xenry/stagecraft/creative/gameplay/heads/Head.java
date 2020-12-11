package com.xenry.stagecraft.creative.gameplay.heads;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public abstract class Head {
	
	public final String id, name;
	public final Category category;
	private final List<String> tags;
	
	public Head(String id, String name, Category category, String...tags) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.tags = Arrays.asList(tags);
	}
	
	public String getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public Category getCategory(){
		return category;
	}
	
	public List<String> getTags(){
		return tags;
	}
	
	public abstract ItemStack getItem();
	
	public enum Category {
		
		//todo better icons...
		
		ALPHABET("alphabet", "Alphabet", true, null), // material!!
		ANIMALS("animals", "Animals", true, Material.PIG_SPAWN_EGG),
		BLOCKS("blocks", "Blocks", true, Material.DIRT),
		DECORATION("decoration", "Decoration", true, null), // material!!
		FOOD_AND_DRINKS("food-drinks", "Food & Drinks", true, Material.COOKED_BEEF),
		HUMANS("humans", "Humans", true, Material.PLAYER_HEAD),
		HUMANOID("humanoid", "Humanoid", true, null), // material!!
		MONSTERS("monsters", "Monsters", true, Material.CREEPER_HEAD), // material!!
		PLANTS("plants", "Plants", true, Material.POPPY),
		MISCELLANEOUS("miscellaneous", "Miscellaneous", true, Material.STRING),
		MHF("mhf", "Official Mojang Heads", false, null), // material!!
		VANILLA("vanilla", "Vanilla Heads", false, Material.SKELETON_SKULL); // material!!
		
		public final String id, name;
		public final boolean download;
		public final Material iconMaterial;
		
		Category(String id, String name, boolean download, Material iconMaterial) {
			this.id = id;
			this.name = name;
			this.download = download;
			this.iconMaterial = iconMaterial;
		}
		
		@Nullable
		public static Category fromID(String id){
			for(Category category : values()){
				if(category.id.equals(id)){
					return category;
				}
			}
			return null;
		}
		
	}
	
}
