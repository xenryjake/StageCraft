package com.xenry.stagecraft.creative.gameplay.heads;
/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/6/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public enum MHFHead {
	
	// Mobs
	MHF_ALEX("Alex", "MHF_Alex"),
	MHF_BLAZE("Blaze", "MHF_Blaze"),
	MHF_CAVESPIDER("Cave Spider", "MHF_CaveSpider"),
	MHF_CHICKEN("Chicken", "MHF_Chicken"),
	MHF_COW("Cow", "MHF_Cow"),
	MHF_CREEPER("Creeper", "MHF_Creeper"),
	MHF_ENDERMAN("Enderman", "MHF_Enderman"),
	MHF_GHAST("Ghast", "MHF_Ghast"),
	MHF_GOLEM("Iron Golem", "MHF_Golem"),
	MHF_HEROBRINE("Herobrine", "MHF_Herobrine"),
	MHF_LAVASLIME("Magma Cube", "MHF_LavaSlime"),
	MHF_MUSHROOMCOW("Mooshroom", "MHF_MushroomCow"),
	MHF_OCELOT("Ocelot", "MHF_Ocelot"),
	MHF_PIG("Pig", "MHF_Pig"),
	MHF_PIGZOMBIE("Zombie Pigman", "MHF_PigZombie"),
	MHF_SHEEP("Sheep", "MHF_Sheep"),
	MHF_SKELETON("Skeleton", "MHF_Skeleton"),
	MHF_SLIME("Slime", "MHF_Slime"),
	MHF_SPIDER("Spider", "MHF_Spider"),
	MHF_SQUID("Squid", "MHF_Squid"),
	MHF_STEVE("Steve", "MHF_Steve"),
	MHF_VILLAGER("Villager", "MHF_Villager"),
	MHF_WSKELETON("Wither Skeleton", "MHF_WSkeleton"),
	MHF_ZOMBIE("Zombie", "MHF_Zombie"),
	
	// Blocks
	MHF_CACTUS("Cactus", "MHF_Cactus"),
	MHF_CAKE("Cake", "MHF_Cake"),
	MHF_CHEST("Chest", "MHF_Chest"),
	MHF_COCONUTB("Brown Coconut", "MHF_CoconutB"),
	MHF_COCONUTG("Green Coconut", "MHF_CoconutG"),
	MHF_MELON("Melon", "MHF_Melon"),
	MHF_OAKLOG("Oak Log", "MHF_OakLog"),
	MHF_PRESENT1("Present 1", "MHF_Present1"),
	MHF_PRESENT2("Present 2", "MHF_Present2"),
	MHF_PUMPKIN("Pumpkin", "MHF_Pumpkin"),
	MHF_TNT("TNT", "MHF_TNT"),
	MHF_TNT2("TNT 2", "MHF_TNT2"),
	
	// Bonus
	MHF_ARROWUP("Arrow Up", "MHF_ArrowUp"),
	MHF_ARROWDOWN("Arrow Down", "MHF_ArrowDown"),
	MHF_ARROWLEFT("Arrow Left", "MHF_ArrowLeft"),
	MHF_ARROWRIGHT("Arrow Right", "MHF_ArrowRight"),
	MHF_EXCLAMATION("Exclamation", "MHF_Exclamation"),
	MHF_QUESTION("Question", "MHF_Question");
	
	private PlayerHead head = null;
	public final String name, player;
	
	MHFHead(String name, String player){
		this.name = name;
		this.player = player;
	}
	
	public PlayerHead getHead() {
		if(head == null){
			head = new PlayerHead("mhf:" + player, name, player, Head.Category.MHF, "Official", "MHF");
		}
		return head;
	}
	
}
