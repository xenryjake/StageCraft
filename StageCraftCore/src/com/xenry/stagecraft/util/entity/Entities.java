package com.xenry.stagecraft.util.entity;

import com.xenry.stagecraft.util.EnumUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.xenry.stagecraft.util.entity.Entities.Category.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum Entities {
	
	AREA_EFFECT_CLOUD(EntityType.AREA_EFFECT_CLOUD, GENERIC),
	ENDER_CRYSTAL(EntityType.ENDER_CRYSTAL, GENERIC, "endcrystal"),
	ENDER_SIGNAL(EntityType.ENDER_SIGNAL, GENERIC, "endsignal"),
	EXPERIENCE_ORB(EntityType.EXPERIENCE_ORB, GENERIC, "exporb", "experience", "xporb", "xp", "exp"),
	FALLING_BLOCK(EntityType.FALLING_BLOCK, GENERIC),
	ITEM(EntityType.DROPPED_ITEM, GENERIC, "droppeditem"),
	PRIMED_TNT(EntityType.PRIMED_TNT, GENERIC, "tnt"),
	
	EVOKER_FANGS(EntityType.EVOKER_FANGS, TECHNICAL, "fangs"),
	LIGHTNING_STRIKE(EntityType.LIGHTNING, TECHNICAL, "lightning"),
	
	ARMOR_STAND(EntityType.ARMOR_STAND, HANGING),
	ITEM_FRAME(EntityType.ITEM_FRAME, HANGING),
	LEASH_HITCH(EntityType.LEASH_HITCH, HANGING, "leash", "hitch"),
	PAINTING(EntityType.PAINTING, HANGING),
	
	FIREWORK(EntityType.FIREWORK, PROJECTILE),
	FISH_HOOK(EntityType.FISHING_HOOK, PROJECTILE, "fishbobber", "hook", "bobber"),
	LLAMA_SPIT(EntityType.LLAMA_SPIT, PROJECTILE, "spit"),
	SHULKER_BULLET(EntityType.SHULKER_BULLET, PROJECTILE),
	THROWN_POTION(EntityType.SPLASH_POTION, PROJECTILE, "potion"),
	ARROW(EntityType.ARROW, PROJECTILE),
	SPECTRAL_ARROW(EntityType.SPECTRAL_ARROW, PROJECTILE),
	TRIDENT(EntityType.TRIDENT, PROJECTILE),
	FIREBALL(EntityType.FIREBALL, PROJECTILE, "largefireball"),
	SMALL_FIREBALL(EntityType.SMALL_FIREBALL, PROJECTILE),
	DRAGON_FIREBALL(EntityType.DRAGON_FIREBALL, PROJECTILE),
	WITHER_SKULL(EntityType.WITHER_SKULL, PROJECTILE),
	EGG(EntityType.EGG, PROJECTILE),
	ENDER_PEARL(EntityType.ENDER_PEARL, PROJECTILE, "endpearl"),
	SNOWBALL(EntityType.SNOWBALL, PROJECTILE),
	THROWN_EXP_BOTTLE(EntityType.THROWN_EXP_BOTTLE, PROJECTILE, "xpbottle", "expbottle", "experiencebottle", "thrownxpbottle", "thrownexpbottle", "thrownexperiencebottle"),
	
	BOAT(EntityType.BOAT, VEHICLE),
	MINECART(EntityType.MINECART, VEHICLE, "rideableminecart"),
	CHEST_MINECART(EntityType.MINECART_CHEST, VEHICLE, "storageminecart"),
	FURNACE_MINECART(EntityType.MINECART_FURNACE, VEHICLE, "poweredminecart"),
	TNT_MINECART(EntityType.MINECART_TNT, VEHICLE),
	HOPPER_MINECART(EntityType.MINECART_HOPPER, VEHICLE),
	SPAWNER_MINECART(EntityType.MINECART_MOB_SPAWNER, VEHICLE, "mobspawnerminecart"),
	COMMAND_BLOCK_MINECART(EntityType.MINECART_COMMAND, VEHICLE, "commandminecart"),
	
	SLIME(EntityType.SLIME, MONSTER),
	MAGMA_CUBE(EntityType.MAGMA_CUBE, MONSTER),
	ENDER_DRAGON(EntityType.ENDER_DRAGON, MONSTER, "dragon"),
	GHAST(EntityType.GHAST, MONSTER),
	PHANTOM(EntityType.PHANTOM, MONSTER),
	BLAZE(EntityType.BLAZE, MONSTER),
	SPIDER(EntityType.SPIDER, MONSTER),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, MONSTER),
	CREEPER(EntityType.CREEPER, MONSTER),
	ZOMBIE(EntityType.ZOMBIE, MONSTER),
	DROWNED(EntityType.DROWNED, MONSTER),
	HUSK(EntityType.HUSK, MONSTER),
	ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, MONSTER, "zombifiedvillager"),
	ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, MONSTER, "pigman", "zombiepigman", "zombiepiglin"),
	PIGLIN(EntityType.PIGLIN, MONSTER),
	PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE, MONSTER),
	GUARDIAN(EntityType.GUARDIAN, MONSTER),
	ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, MONSTER),
	ENDERMAN(EntityType.ENDERMAN, MONSTER),
	ENDERMITE(EntityType.ENDERMITE, MONSTER),
	GIANT(EntityType.GIANT, MONSTER),
	SILVERFISH(EntityType.SILVERFISH, MONSTER),
	SKELETON(EntityType.SKELETON, MONSTER),
	STRAY(EntityType.STRAY, MONSTER),
	WITHER_SKELETON(EntityType.WITHER_SKELETON, MONSTER),
	VEX(EntityType.VEX, MONSTER),
	WITHER(EntityType.WITHER, MONSTER),
	HOGLIN(EntityType.HOGLIN, MONSTER),
	ZOGLIN(EntityType.ZOGLIN, MONSTER),
	SKELETON_HORSE(EntityType.SKELETON_HORSE, MONSTER),
	ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, MONSTER),
	SHULKER(EntityType.SHULKER, MONSTER),
	RAVAGER(EntityType.RAVAGER, MONSTER),
	WITCH(EntityType.WITCH, MONSTER),
	PILLAGER(EntityType.PILLAGER, MONSTER),
	VINDICATOR(EntityType.VINDICATOR, MONSTER),
	EVOKER(EntityType.EVOKER, MONSTER),
	ILLUSIONER(EntityType.ILLUSIONER, MONSTER),
	
	BAT(EntityType.BAT, ANIMAL),
	IRON_GOLEM(EntityType.IRON_GOLEM, ANIMAL),
	SNOWMAN(EntityType.SNOWMAN, ANIMAL, "snowgolem"),
	DOLPHIN(EntityType.DOLPHIN, ANIMAL),
	SQUID(EntityType.SQUID, ANIMAL),
	COD(EntityType.COD, ANIMAL),
	PUFFERFISH(EntityType.PUFFERFISH, ANIMAL),
	SALMON(EntityType.SALMON, ANIMAL),
	TROPICAL_FISH(EntityType.TROPICAL_FISH, ANIMAL, "clownfish"),
	BEE(EntityType.BEE, ANIMAL),
	CHICKEN(EntityType.CHICKEN, ANIMAL),
	COW(EntityType.COW, ANIMAL),
	MUSHROOM_COW(EntityType.MUSHROOM_COW, ANIMAL, "mooshroom"),
	FOX(EntityType.FOX, ANIMAL),
	OCELOT(EntityType.OCELOT, ANIMAL),
	PANDA(EntityType.PANDA, ANIMAL),
	POLAR_BEAR(EntityType.POLAR_BEAR, ANIMAL),
	RABBIT(EntityType.RABBIT, ANIMAL),
	SHEEP(EntityType.SHEEP, ANIMAL),
	TURTLE(EntityType.TURTLE, ANIMAL),
	WOLF(EntityType.WOLF, ANIMAL),
	PIG(EntityType.PIG, ANIMAL),
	STRIDER(EntityType.STRIDER, ANIMAL),
	CAT(EntityType.CAT, ANIMAL),
	PARROT(EntityType.PARROT, ANIMAL),
	HORSE(EntityType.HORSE, ANIMAL),
	DONKEY(EntityType.DONKEY, ANIMAL),
	MULE(EntityType.MULE, ANIMAL),
	LLAMA(EntityType.LLAMA, ANIMAL),
	TRADER_LLAMA(EntityType.TRADER_LLAMA, ANIMAL),
	VILLAGER(EntityType.VILLAGER, ANIMAL),
	WANDERING_TRADER(EntityType.WANDERING_TRADER, ANIMAL),
	
	PLAYER(EntityType.PLAYER, Category.PLAYER);
	
	public final String name;
	public final EntityType type;
	public final Category category;
	private final List<String> identifiers;
	
	Entities(String name, EntityType type, Category category, String...ids){
		this.name = name;
		this.type = type;
		this.category = category;
		this.identifiers = generateIdentifiers(ids);
	}
	
	Entities(EntityType type, Category category, String...ids){
		this.name = EnumUtil.generateName(this);
		this.type = type;
		this.category = category;
		this.identifiers = generateIdentifiers(ids);
	}
	
	private List<String> generateIdentifiers(String[] ids){
		List<String> identifiers = new ArrayList<>();
		identifiers.add(makeIdentifier(name()));
		for(String id : ids){
			identifiers.add(makeIdentifier(id));
		}
		return identifiers;
	}
	
	public Class<? extends Entity> getEntityClass(){
		return type.getEntityClass();
	}
	
	public String getName() {
		return name;
	}
	
	public EntityType getType() {
		return type;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public List<String> getIdentifiers() {
		return identifiers;
	}
	
	public boolean isLivingEntity(){
		return LivingEntity.class.isAssignableFrom(getEntityClass());
	}
	
	public enum Category {
		
		GENERIC(false),	// done
		TECHNICAL(false), 	// ignore other than max
		HANGING(false),	// done
		PROJECTILE(false),	// done
		VEHICLE(false),
		ANIMAL(true),		// doneish
		MONSTER(true),		// doneish
		PLAYER(false);		// ignore
		
		private final boolean mob;
		
		Category(boolean mob){
			this.mob = mob;
		}
		
		public boolean isMob() {
			return mob;
		}
		
	}
	
	private static final HashMap<EntityType,Entities> entitiesByType;
	private static final HashMap<Category,List<Entities>> entitiesByCategory;
	private static final List<Entities> allEntities;
	private static final List<Entities> allMobs;
	private static final HashMap<String,Entities> entitiesByIdentifier;
	private static final List<String> allMobIdentifiers;
	
	static {
		entitiesByType = new HashMap<>();
		entitiesByCategory = new HashMap<>();
		allEntities = new ArrayList<>();
		entitiesByIdentifier = new HashMap<>();
		allMobIdentifiers = new ArrayList<>();
		
		for(Category category : Category.values()){
			entitiesByCategory.put(category, new ArrayList<>());
		}
		for(Entities entity : values()){
			entitiesByType.put(entity.type, entity);
			entitiesByCategory.get(entity.category).add(entity);
			allEntities.add(entity);
			for(String id : entity.identifiers){
				entitiesByIdentifier.put(id, entity);
			}
			if(entity.category.mob){
				allMobIdentifiers.addAll(entity.identifiers);
			}
		}
		
		allMobs = new ArrayList<>();
		for(Category category : Category.values()){
			allMobs.addAll(entitiesByCategory.get(category));
		}
	}
	
	public static HashMap<EntityType,Entities> getEntitiesByType() {
		return entitiesByType;
	}
	
	public static Entities getEntityByType(EntityType type){
		return entitiesByType.get(type);
	}
	
	public static Entities get(Entity entity){
		return getEntityByType(entity.getType());
	}
	
	public static HashMap<Category,List<Entities>> getEntitiesByCategory() {
		return entitiesByCategory;
	}
	
	public static List<Entities> getEntitiesByCategory(Category category){
		return entitiesByCategory.get(category);
	}
	
	public static List<Entities> getAllEntities(){
		return allEntities;
	}
	
	public static List<Entities> getAllMobs(){
		return allMobs;
	}
	
	public static HashMap<String,Entities> getEntitiesByIdentifier() {
		return entitiesByIdentifier;
	}
	
	public static Set<String> getAllIdentifiers(){
		return entitiesByIdentifier.keySet();
	}
	
	public static List<String> getAllMobIdentifiers() {
		return allMobIdentifiers;
	}
	
	@Nullable
	public static Entities getEntity(String identifier){
		return entitiesByIdentifier.getOrDefault(makeIdentifier(identifier), null);
	}
	
	@Nullable
	public static Entities getEntity(String identifier, Category category){
		Entities entity = getEntity(identifier);
		if(entity == null || entity.category != category){
			return null;
		}
		return entity;
	}
	
	@Nullable
	public static Entities getMob(String identifier){
		Entities entity = getEntity(identifier);
		if(entity == null || !entity.category.mob){
			return null;
		}
		return entity;
	}
	
	private static String makeIdentifier(String string){
		return string.toLowerCase().replaceAll("[^a-z]", "");
	}
	
}
