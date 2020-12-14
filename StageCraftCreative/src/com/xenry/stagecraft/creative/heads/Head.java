package com.xenry.stagecraft.creative.heads;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
		
		ALPHABET("alphabet", "Alphabet", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY3ZDgxM2FlN2ZmZTViZTk1MWE0ZjQxZjJhYTYxOWE1ZTM4OTRlODVlYTVkNDk4NmY4NDk0OWM2M2Q3NjcyZSJ9fX0="),
		ANIMALS("animals", "Animals", true, SkullUtil.getSkullFromOwnerName("MHF_Pig")),
		BLOCKS("blocks", "Blocks", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZkMWZhYmRmM2UzNDI2NzFiZDlmOTVmNjg3ZmUyNjNmNDM5ZGRjMmYxYzllYThmZjE1YjEzZjFlN2U0OGI5In19fQ=="),
		DECORATION("decoration", "Decoration", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE1YWIwNWVhMjU0YzMyZTNjNDhmM2ZkY2Y5ZmQ5ZDc3ZDNjYmEwNGU2YjVlYzJlNjhiM2NiZGNmYWMzZmQifX19"),
		FOOD_AND_DRINKS("food-drinks", "Food & Drinks", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk4MGNiY2E2MTlmMzY4ZTlkNjNlY2FlYTk4MjhkNzgzNmQxNzU5ZjI4MzQ0ZDFkNWNkMmFkZTNiYWQ2Njk4NCJ9fX0="),
		HUMANS("humans", "Humans", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY4YTc2MzYyYzQxNzk2NjhmNTk0YjdiNDcyZjkzMDk2YzdiYjM1OWY3M2M0MWViYzExNGI4ZTUzZDI1YjhkOSJ9fX0="),
		HUMANOID("humanoid", "Humanoid", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTk3MTg0NjRkYWIwNDljMDY0OGE3MTYwYzZlMzRmMzc1MjIzN2NjMTlhMTljYzcyZDA0MDFiNTE3ZjZjMjQifX19"),
		MONSTERS("monsters", "Monsters", true, SkullUtil.getSkullFromOwnerName("MHF_Spider")),
		PLANTS("plants", "Plants", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTA0ZjFhNTU5NDNjNTk0ZTcxMTllODg0YzVkYTJhMmJjYThlN2U2NTE2YTA2NDlhYTdlNTU2NThlMGU5In19fQ=="),
		MISCELLANEOUS("miscellaneous", "Miscellaneous", true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ZmYzk3N2NjN2UxMGU1NjRhMDk2MzhhNTNiYmM0YzU0YzljOGRhYzc0NTBiYTNkZmEzYzkwOTlkOTRmNSJ9fX0="),
		MHF("mhf", "Official Mojang Heads", false, SkullUtil.getSkullFromOwnerName("MHF_Alex")),
		VANILLA("vanilla", "Vanilla Heads", false, Material.SKELETON_SKULL);
		
		public final String id, name;
		public final boolean download;
		private final ItemStack icon;
		
		Category(String id, String name, boolean download, Material iconMaterial) {
			this(id, name, download, new ItemStack(iconMaterial));
		}
		
		Category(String id, String name, boolean download, String headValue){
			this(id, name, download, SkullUtil.getSkullFromTextureBase64(headValue));
		}
		
		Category(String id, String name, boolean download, ItemStack icon){
			this.id = id;
			this.name = name;
			this.download = download;
			ItemMeta meta = icon.getItemMeta();
			meta.setDisplayName("§e" + name);
			icon.setItemMeta(meta);
			this.icon = icon;
		}
		
		public ItemStack getIcon() {
			return icon.clone();
		}
		
		@Nullable
		public static Category fromID(String id){
			for(Category category : values()){
				if(category.id.equalsIgnoreCase(id)){
					return category;
				}
			}
			return null;
		}
		
		public static List<String> getAllIDs(){
			List<String> ids = new ArrayList<>();
			for(Category category : values()){
				ids.add(category.id);
			}
			return ids;
		}
		
		public static List<String> getIDs(String startsWith){
			startsWith = startsWith.toLowerCase();
			List<String> ids = new ArrayList<>();
			for(Category category : values()){
				if(category.id.toLowerCase().startsWith(startsWith)) {
					ids.add(category.id);
				}
			}
			return ids;
		}
		
	}
	
}
