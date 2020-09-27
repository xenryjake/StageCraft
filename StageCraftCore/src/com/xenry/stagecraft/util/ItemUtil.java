package com.xenry.stagecraft.util;
import com.xenry.stagecraft.Core;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/20/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ItemUtil {
	
	private ItemUtil(){}
	
	@SuppressWarnings("deprecation")
	public static NamespacedKey key(String id){
		return new NamespacedKey("stagecraft", id);
	}
	
	public static void damageItem(ItemStack item, int damage){
		ItemMeta meta = item.getItemMeta();
		if(!(meta instanceof Damageable)){
			return;
		}
		int finalDamage = 0;
		int unbreakingLevel = meta.getEnchantLevel(Enchantment.DURABILITY);
		if(unbreakingLevel > 0){
			float p = isArmor(item) ? 0.6f+(0.4f/(unbreakingLevel+1f)) : 1f/(unbreakingLevel+1f);
			for(int i = 0; i < damage ; i++){
				if(Core.getInstance().getRandom().nextFloat() < p){
					finalDamage++;
				}
			}
		}else{
			finalDamage = damage;
		}
		((Damageable)meta).setDamage(((Damageable)meta).getDamage() + finalDamage);
		item.setItemMeta(meta);
	}
	
	public static void repairItem(ItemStack item){
		ItemMeta meta = item.getItemMeta();
		if(meta instanceof Damageable){
			((Damageable)meta).setDamage(0);
			item.setItemMeta(meta);
		}
	}
	
	public static boolean isArmor(ItemStack item){
		return isArmor(item.getType());
	}
	
	public static boolean isArmor(Material material){
		switch(material){
			case LEATHER_HELMET:
			case LEATHER_CHESTPLATE:
			case LEATHER_LEGGINGS:
			case LEATHER_BOOTS:
			case CHAINMAIL_HELMET:
			case CHAINMAIL_CHESTPLATE:
			case CHAINMAIL_LEGGINGS:
			case CHAINMAIL_BOOTS:
			case IRON_HELMET:
			case IRON_CHESTPLATE:
			case IRON_LEGGINGS:
			case IRON_BOOTS:
			case GOLDEN_HELMET:
			case GOLDEN_CHESTPLATE:
			case GOLDEN_LEGGINGS:
			case GOLDEN_BOOTS:
			case DIAMOND_HELMET:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_LEGGINGS:
			case DIAMOND_BOOTS:
			case TURTLE_HELMET:
				return true;
			default:
				return false;
		}
	}
	
}
