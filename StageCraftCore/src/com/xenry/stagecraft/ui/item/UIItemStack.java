package com.xenry.stagecraft.ui.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Jake on January 03, 2016.
 * Copyright 2016 Henry Jake.
 * All content in this file may not be used without written consent of Henry Jake.
 */
public class UIItemStack extends ItemStack {

	public UIItemStack(Material material){
		super(material, 1);
	}

	public UIItemStack(Material material, int qty){
		super(material, qty);
	}

	public UIItemStack(Material material, int qty, String name){
		this(material, qty);
		this.setName(name);
	}

	public UIItemStack(Material material, int qty, String name, List<String> lore){
		this(material, qty, name);
		this.setLore(lore);
	}

	public UIItemStack(Material material, int qty, String name, String...lore){
		this(material, qty, name, Arrays.asList(lore));
	}

	public UIItemStack(Material material, String name){
		this(material, 1, name);
	}

	public UIItemStack(Material material, String name, String...lore){
		this(material, 1, name, lore);
	}

	public UIItemStack(ItemStack stack){
		super(stack);
	}

	public void setName(String name){
		ItemMeta m = this.getItemMeta();
		if(m == null){
			throw new IllegalArgumentException("ItemMeta is null???");
		}
		m.setDisplayName(name);
		setItemMeta(m);
	}

	public String getName(){
		ItemMeta m = this.getItemMeta();
		if(m == null){
			throw new IllegalArgumentException("ItemMeta is null???");
		}
		return m.getDisplayName();
	}

	public void setLore(List<String> lore){
		ItemMeta m = this.getItemMeta();
		if(m == null){
			throw new IllegalArgumentException("ItemMeta is null???");
		}
		m.setLore(lore);
		setItemMeta(m);
	}

	public List<String> getLore(){
		ItemMeta m = this.getItemMeta();
		if(m == null){
			throw new IllegalArgumentException("ItemMeta is null???");
		}
		return m.getLore() == null ? new ArrayList<>() : m.getLore();
	}

	public UIItemStack addLore(String...lines){
		List<String> lore = getLore();
		Collections.addAll(lore, lines);
		setLore(lore);
		return this;
	}

	public UIItemStack clearLore(){
		setLore(new ArrayList<>());
		return this;
	}

}
