package com.xenry.stagecraft.survival.gameplay.grapplinghook;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.util.Cooldown;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.ItemUtil;
import com.xenry.stagecraft.util.M;
import com.xenry.stagecraft.util.SkullUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GrapplingHookHandler extends Handler<Survival,GameplayManager> {
	
	public static boolean enabled = true;
	
	//changing these values will break all existing grappling hooks and/or grappling hook fuel
	public static final String GRAPPLING_HOOK_NAME = "§aGrappling Hook";
	public static final String FUEL_NAME = "§aGrappling Hook Fuel";
	
	private final Cooldown cooldown;
	
	public GrapplingHookHandler(GameplayManager manager){
		super(manager);
		addGrapplingHookRecipe();
		addFuelRecipe();
		cooldown = new Cooldown(2000, M.err + "Please wait " + M.elm + "%t%" + M.err + " to use the grappling hook again.");
	}
	
	public void addGrapplingHookRecipe() {
		ItemStack item = getNewGrapplingHookItem();
		
		NamespacedKey key = ItemUtil.key("grappling_hook");
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		recipe.shape("  t"," ts","t n");
		recipe.setIngredient('t', Material.STICK);
		recipe.setIngredient('s', Material.STRING);
		recipe.setIngredient('n', Material.NETHERITE_INGOT);
		Bukkit.addRecipe(recipe);
	}
	
	public void addFuelRecipe(){
		ItemStack item = getNewFuelItem();
		item.setAmount(16);
		
		NamespacedKey key = ItemUtil.key("grappling_hook_fuel");
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		recipe.shape("rsr","glg","rsr");
		recipe.setIngredient('r', Material.REDSTONE);
		recipe.setIngredient('s', Material.STRING);
		recipe.setIngredient('g', Material.GUNPOWDER);
		recipe.setIngredient('l', Material.EMERALD);
		Bukkit.addRecipe(recipe);
	}
	
	public static ItemStack getNewGrapplingHookItem(){
		ItemStack item = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta = item.getItemMeta();
		if(meta == null){
			throw new NullPointerException("I don't even know how we got here.");
		}
		meta.setDisplayName(GRAPPLING_HOOK_NAME);
		meta.addEnchant(CustomEnchantment.GRAPPLING_HOOK, 1, true);
		item.setItemMeta(meta);
		return item;
	}
	
	//eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYW
	//Y5ZDI2ZTFhNWJhODIzN2E0ZDg2MWFhMWE0NTc4ZDkxN2QxMGFjOTM5NmI4ZTZjNmE5MTMyNDJmYWQwMzgxNiJ9fX0=
	public static ItemStack getNewFuelItem(){
		ItemStack item = SkullUtil.getSkullFromTextureBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY5ZDI2ZTFhNWJhODIzN2E0ZDg2MWFhMWE0NTc4ZDkxN2QxMGFjOTM5NmI4ZTZjNmE5MTMyNDJmYWQwMzgxNiJ9fX0=");
		ItemMeta meta = item.getItemMeta();
		if(meta == null){
			throw new NullPointerException("I don't even know how we got here.");
		}
		meta.setDisplayName(FUEL_NAME);
		meta.addEnchant(CustomEnchantment.SPECIAL_ITEM, 1, true);
		item.setItemMeta(meta);
		return item;
	}
	
	@EventHandler
	public void on(PlayerFishEvent event) {
		if(!enabled){
			return;
		}
		Player player = event.getPlayer();
		if(!isGrapplingHook(player.getInventory().getItemInMainHand())){
			return;
		}
		switch(event.getState()){
			case REEL_IN:
			case IN_GROUND:
			case FAILED_ATTEMPT:
				break;
			case FISHING:
				return;
			case BITE:
			case CAUGHT_FISH:
			case CAUGHT_ENTITY:
			default:
				event.setCancelled(true);
				return;
		}
		
		if(!cooldown.canUse(player, true)){
			return;
		}
		
		if(player.getGameMode() != GameMode.CREATIVE){
			if(!useFuelItem(player)){
				player.sendMessage(M.err + "You don't have any Grappling Hook Fuel.");
				return;
			}
		}
		
		player.setVelocity(calculateVelocity(event.getHook().getLocation(), player.getLocation()));
		ItemUtil.repairItem(player.getInventory().getItemInMainHand());
		//ItemUtil.damageItem(player.getInventory().getItemInMainHand(), 1);
		cooldown.use(player, false);
	}
	
	/*public Vector calculateVelocityA(Location hook, Location player){
		Vector diff = hook.toVector().subtract(player.toVector());
		double distance = hook.distance(player);
		Vector velocity = new Vector();
		velocity.setX((1 + HORIZ_MOD * distance) * diff.getX() / distance);
		velocity.setY((1 + VERT_MOD * distance) * diff.getY() / distance + Math.max(VERT_BOOST, 0) * distance);
		velocity.setZ((1 + HORIZ_MOD * distance) * diff.getZ() / distance);
		return velocity;
	}*/
	
	public Vector calculateVelocity(Location hook, Location player){
		Vector diff = hook.toVector().subtract(player.toVector());
		Vector velocity = diff.clone().normalize();
		velocity.multiply(2);
		velocity.setY(1.2);
		return velocity;
	}
	
	@EventHandler
	public void on(EntityDamageEvent event){
		if(!enabled){
			return;
		}
		Entity entity = event.getEntity();
		if(event.getCause() != EntityDamageEvent.DamageCause.FALL || !(entity instanceof Player)){
			return;
		}
		
		Player player = (Player)entity;
		if(isGrapplingHook(player.getInventory().getItemInMainHand())){
			event.setDamage(event.getDamage() / 2);
		}
	}
	
	public boolean isGrapplingHook(ItemStack item){
		ItemMeta im = item.getItemMeta();
		return item.getType() == Material.FISHING_ROD && im != null && im.getEnchantLevel(CustomEnchantment.GRAPPLING_HOOK) > 0 && im.getDisplayName().equals(GRAPPLING_HOOK_NAME);
	}
	
	public boolean isFuelItem(ItemStack item){
		ItemMeta im = item.getItemMeta();
		return item.getType() == Material.PLAYER_HEAD && im != null && im.getEnchantLevel(CustomEnchantment.SPECIAL_ITEM) > 0 && im.getDisplayName().equals(FUEL_NAME);
	}
	
	public boolean useFuelItem(Player player){
		Inventory inv = player.getInventory();
		for(ItemStack is : inv.getStorageContents()){
			if(is == null || is.getAmount() == 0 || !isFuelItem(is)){
				continue;
			}
			if(is.getAmount() == 1){
				player.getInventory().removeItem(is);
			}else{
				is.setAmount(is.getAmount() - 1);
			}
			return true;
		}
		return false;
	}
	
}
