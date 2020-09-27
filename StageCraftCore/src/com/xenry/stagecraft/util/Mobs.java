package com.xenry.stagecraft.util;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum Mobs {
	
	BAT("Bat", EntityType.BAT),
	BEE("Bee", EntityType.BEE),
	BLAZE("Blaze", EntityType.BLAZE),
	CAT("Cat", EntityType.CAT),
	CAVE_SPIDER("Cave Spider", EntityType.CAVE_SPIDER),
	CHICKEN("Chicken", EntityType.CHICKEN),
	COD("Cod", EntityType.COD),
	COW("Cow", EntityType.COW),
	CREEPER("Creeper", EntityType.CREEPER),
	DOLPHIN("Dolphin", EntityType.DOLPHIN),
	DONKEY("Donkey", EntityType.DONKEY),
	DROWNED("Drowned", EntityType.DROWNED),
	ELDER_GUARDIAN("Elder Guardian", EntityType.ELDER_GUARDIAN),
	ENDER_DRAGON("Ender Dragon", EntityType.ENDER_DRAGON),
	ENDERMAN("Enderman", EntityType.ENDERMAN),
	ENDERMITE("Endermite", EntityType.ENDERMITE),
	EVOKER("Evoker", EntityType.EVOKER),
	FOX("Fox", EntityType.FOX),
	GHAST("Ghast", EntityType.GHAST),
	GIANT("Giant", EntityType.GIANT),
	GUARDIAN("Guardian", EntityType.GUARDIAN),
	HOGLIN("Hoglin", EntityType.HOGLIN),
	HORSE("Horse", EntityType.HORSE),
	HUSK("Husk", EntityType.HUSK),
	ILLUSIONER("Illusioner", EntityType.ILLUSIONER),
	IRON_GOLEM("Iron Golem", EntityType.IRON_GOLEM),
	LLAMA("Llama", EntityType.LLAMA),
	MAGMA_CUBE("Magma Cube", EntityType.MAGMA_CUBE),
	MULE("Mule", EntityType.MULE),
	MOOSHROOM("Mooshroom", EntityType.MUSHROOM_COW),
	OCELOT("Ocelot", EntityType.OCELOT),
	PANDA("Panda", EntityType.PANDA),
	PARROT("Parrot", EntityType.PARROT),
	PHANTOM("Phantom", EntityType.PHANTOM),
	PIG("Pig", EntityType.PIG),
	PIGLIN("Piglin", EntityType.PIGLIN),
	PILLARGER("Pillager", EntityType.PILLAGER),
	POLAR_BEAR("Polar Bear", EntityType.POLAR_BEAR),
	PUFFERFISH("Pufferfish", EntityType.PUFFERFISH),
	RABBIT("Rabbit", EntityType.RABBIT),
	RAVAGER("Ravager", EntityType.RAVAGER),
	SALMON("Salmon", EntityType.SALMON),
	SHEEP("Sheep", EntityType.SHEEP),
	SHULKER("Shulker", EntityType.SHULKER),
	SILVERFISH("Silverfish", EntityType.SILVERFISH),
	SKELETON_HORSE("Skeleton Horse", EntityType.SKELETON_HORSE),
	SKELETON("Skeleton", EntityType.SKELETON),
	SLIME("Slime", EntityType.SLIME),
	SNOWMAN("Snowman", EntityType.SNOWMAN),
	SPIDER("Spider", EntityType.SPIDER),
	SQUID("Squid", EntityType.SQUID),
	STRAY("Stray", EntityType.STRAY),
	STRIDER("Strider", EntityType.STRIDER),
	TRADER_LLAMA("Trader Llama", EntityType.TRADER_LLAMA),
	TROPICAL_FISH("Tropical Fish", EntityType.TROPICAL_FISH),
	TURTLE("Turtle", EntityType.TURTLE),
	VEX("Vex", EntityType.VEX),
	VILLAGER("Villager", EntityType.VILLAGER),
	VINDICATOR("Vindicator", EntityType.VINDICATOR),
	WANDERING_TRADER("Wandering Trader", EntityType.WANDERING_TRADER),
	WITCH("Witch", EntityType.WITCH),
	WITHER_SKELETON("Wither Skeleton", EntityType.WITHER_SKELETON),
	WITHER("Wither", EntityType.WITHER),
	WOLF("Wolf", EntityType.WOLF),
	ZOGLIN("Zoglin", EntityType.ZOGLIN),
	ZOMBIE_HORSE("Zombie Horse", EntityType.ZOMBIE_HORSE),
	ZOMBIE_VILLAGER("Zombie Villager", EntityType.ZOMBIE_VILLAGER),
	ZOMBIE("Zombie", EntityType.ZOMBIE),
	ZOMBIFIED_PIGLIN("Zombified Piglin", EntityType.ZOMBIFIED_PIGLIN);
	
	private static final HashMap<String,Mobs> allMobs;
	
	public final String name;
	public final EntityType type;
	
	Mobs(String name, EntityType type){
		this.name = name;
		this.type = type;
	}
	
	static {
		allMobs = new HashMap<>();
		for (Mobs mob : Mobs.values()) {
			allMobs.put(mob.name.toLowerCase().replaceAll("[^a-z]", ""), mob);
		}
	}
	
	public static Mobs fromName(String name) {
		return allMobs.getOrDefault(name.toLowerCase().replaceAll("[^a-z]", ""), null);
	}
	
}
