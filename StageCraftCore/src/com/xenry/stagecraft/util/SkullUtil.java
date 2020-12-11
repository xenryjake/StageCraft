package com.xenry.stagecraft.util;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class SkullUtil {
	
	private static final Random random = new Random();
	private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private SkullUtil(){}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getSkullFromOwnerName(String name){
		ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(name);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack getSkullFromTextureURL(String url){
		return getSkullFromTextureURL(url, false);
	}
	
	public static ItemStack getSkullFromTextureURL(String url, boolean randomName){
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
		return getSkullFromTextureBase64(new String(encodedData), randomName);
	}
	
	public static ItemStack getSkullFromTextureBase64(String texture){
		return getSkullFromTextureBase64(texture, false);
	}
	
	public static ItemStack getSkullFromTextureBase64(String texture, boolean randomName){
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		if(meta == null){
			return null;
		}
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), randomName ? getRandomString(16) : null);
		profile.getProperties().put("textures", new Property("textures", texture));
		
		try{
			Field profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static String getRandomString(int length){
		StringBuilder b = new StringBuilder(length);
		for(int j = 0; j < length; j++) {
			b.append(chars.charAt(random.nextInt(chars.length())));
		}
		return b.toString();
	}
	
}

