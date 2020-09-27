package com.xenry.stagecraft.survival.gameplay;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.commands.*;
import com.xenry.stagecraft.survival.gameplay.damageindicator.DamageIndicatorCommand;
import com.xenry.stagecraft.survival.gameplay.damageindicator.DamageIndicatorHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.BasicEnchantmentHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.CustomEnchantment;
import com.xenry.stagecraft.survival.gameplay.enchantment.automine.LumberjackHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.automine.OreMinerHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.autosmelt.OreSmeltingHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.commands.EnchantedBookCommand;
import com.xenry.stagecraft.survival.gameplay.enchantment.commands.GiveEnchantedItemCommand;
import com.xenry.stagecraft.survival.gameplay.enchantment.growth.GrowthHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.specialitem.SpecialItemHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.telekinesis.TelekinesisHandler;
import com.xenry.stagecraft.survival.gameplay.grapplinghook.GrapplingHookCommand;
import com.xenry.stagecraft.survival.gameplay.grapplinghook.GrapplingHookHandler;
import com.xenry.stagecraft.survival.gameplay.pvptoggle.PvPCommand;
import com.xenry.stagecraft.survival.gameplay.pvptoggle.PvPHandler;
import com.xenry.stagecraft.survival.gameplay.rules.AcceptRulesHandler;
import com.xenry.stagecraft.survival.gameplay.rules.RulesCommand;
import com.xenry.stagecraft.survival.gameplay.sign.SignCommand;
import com.xenry.stagecraft.survival.gameplay.sign.SignEditHandler;
import com.xenry.stagecraft.survival.gameplay.villagers.VillagersDamageCommand;
import com.xenry.stagecraft.survival.gameplay.villagers.VillagersHandler;
import com.xenry.stagecraft.survival.gameplay.enchantment.growth.GrowthIngredient;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.util.ItemUtil;
import com.xenry.stagecraft.util.Log;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Strider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import org.spigotmc.event.entity.EntityMountEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 3/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class GameplayManager extends Manager<Survival> {
	
	private PvPHandler pvpHandler;
	private AcceptRulesHandler acceptRulesHandler;
	private VillagersHandler villagersHandler;
	private GrapplingHookHandler grapplingHookHandler;
	//private ShulkerBoxHandler shulkerBoxHandler;
	//private BloodHandler bloodHandler;
	private DamageIndicatorHandler damageIndicatorHandler;
	
	private final List<CustomEnchantment> registeredEnchantments;
	private OreMinerHandler oreMinerHandler;
	private LumberjackHandler lumberjackHandler;
	private TelekinesisHandler telekinesisHandler;
	private GrowthHandler growthHandler;
	private OreSmeltingHandler oreSmeltingHandler;
	private SpecialItemHandler specialItemHandler;
	private BasicEnchantmentHandler basicEnchantmentHandler;
	
	public GameplayManager(Survival plugin){
		super("Gameplay", plugin);
		registeredEnchantments = new ArrayList<>();
	}
	
	//NOTE: TelekinesisHandler must be registered AFTER OreSmeltingHandler
	public void onEnable(){
		
		registerEnchantments();
		
		pvpHandler = new PvPHandler(this);
		registerListener(pvpHandler);
		registerCommand(new PvPCommand(this));
		
		acceptRulesHandler = new AcceptRulesHandler(this);
		registerListener(acceptRulesHandler);
		registerCommand(new RulesCommand(this));
		
		villagersHandler = new VillagersHandler(this);
		registerListener(villagersHandler);
		registerCommand(new VillagersDamageCommand(this));
		
		grapplingHookHandler = new GrapplingHookHandler(this);
		registerListener(grapplingHookHandler);
		registerCommand(new GrapplingHookCommand(this));
		
		//shulkerBoxHandler = new ShulkerBoxHandler(this);
		//registerListener(shulkerBoxHandler.getListener());
		
		//bloodHandler = new BloodHandler(this);
		//registerListener(bloodHandler);
		
		damageIndicatorHandler = new DamageIndicatorHandler(this);
		registerListener(damageIndicatorHandler);
		registerCommand(new DamageIndicatorCommand(this));
		
		oreMinerHandler = new OreMinerHandler(this);
		registerListener(oreMinerHandler);
		lumberjackHandler = new LumberjackHandler(this);
		registerListener(lumberjackHandler);
		growthHandler = new GrowthHandler(this);
		registerListener(growthHandler);
		oreSmeltingHandler = new OreSmeltingHandler(this);
		registerListener(oreSmeltingHandler);
		telekinesisHandler = new TelekinesisHandler(this);
		registerListener(telekinesisHandler);
		specialItemHandler = new SpecialItemHandler(this);
		registerListener(specialItemHandler);
		basicEnchantmentHandler = new BasicEnchantmentHandler(this);
		registerListener(basicEnchantmentHandler);
		registerCommand(new GiveEnchantedItemCommand(this));
		registerCommand(new EnchantedBookCommand(this));
		
		registerCommand(new ShopCommand(this));
		
		registerCommand(new GamemodeCommand(this));
		registerCommand(new FlyCommand(this));
		registerCommand(new EnderChestCommand(this));
		registerCommand(new WorkbenchCommand(this));
		registerCommand(new SmithingTableCommand(this));
		registerCommand(new SuicideCommand(this));
		registerCommand(new TrashCommand(this));
		registerCommand(new KillCommand(this));
		registerCommand(new NearCommand(this));
		registerCommand(new RepairCommand(this));
		registerCommand(new HealCommand(this));
		registerCommand(new FeedCommand(this));
		registerCommand(new WeatherCommand(this));
		registerCommand(new TimeCommand(this));
		registerCommand(new ListCommand(this));
		registerCommand(new BurnCommand(this));
		registerCommand(new ExtinguishCommand(this));
		registerCommand(new MoreCommand(this));
		registerCommand(new SpeedCommand(this));
		registerCommand(new BreakCommand(this));
		registerCommand(new SpawnerCommand(this));
		registerCommand(new LightningCommand(this));
		registerCommand(new RestCommand(this));
		registerCommand(new ItemNameCommand(this));
		registerCommand(new ItemLoreCommand(this));
		registerCommand(new SkullCommand(this));
		registerCommand(new SudoCommand(this));
		registerCommand(new ExperienceCommand(this));
		
		registerCommand(new WebRegisterCommand(this));
		registerCommand(new WaypointCommand(this));
		registerCommand(new HelpCommand(this));
		
		addGrowthIngredientRecipes();
		
		registerCommand(new SignCommand(this));
		registerListener(new SignEditHandler(this));
	}
	
	private void registerEnchantments(){
		registerEnchantment(CustomEnchantment.SPECIAL_ITEM);
		registerEnchantment(CustomEnchantment.ORE_MINER);
		registerEnchantment(CustomEnchantment.LUMBERJACK);
		registerEnchantment(CustomEnchantment.TELEKINESIS);
		registerEnchantment(CustomEnchantment.DELICATE_WALKER);
		registerEnchantment(CustomEnchantment.GROWTH);
		registerEnchantment(CustomEnchantment.ORE_SMELTING);
		registerEnchantment(CustomEnchantment.GRAPPLING_HOOK);
		
		Enchantment.stopAcceptingRegistrations();
	}
	
	private void registerEnchantment(CustomEnchantment enchantment){
		if(enchantment.isRegistered()){
			Log.warn("Attempted to register already-registered enchantment (" + enchantment.getKey() + ")");
			return;
		}
		try{
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			f.setAccessible(false);
			
			Enchantment.registerEnchantment(enchantment);
		}catch(Exception ex){
			Log.severe("Failed to register enchantment (" + enchantment.getKey() + "): " + ex.toString());
			return;
		}
		
		enchantment.setRegistered();
		registeredEnchantments.add(enchantment);
	}
	
	public void addTestRecipe() {
		ItemStack item = new ItemStack(Material.COBBLESTONE);
		item.addUnsafeEnchantment(CustomEnchantment.SPECIAL_ITEM, 1);
		ItemMeta meta = item.getItemMeta();
		if(meta == null){
			return;
		}
		meta.setDisplayName("§aEnchanted Cobblestone");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		NamespacedKey key = ItemUtil.key("enchanted_cobblestone");
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		recipe.shape("xxx","xxx","xxx");
		recipe.setIngredient('x', Material.COBBLESTONE);
		Bukkit.addRecipe(recipe);
	}
	
	public void addGrowthIngredientRecipes(){
		{
			GrowthIngredient ingredient = GrowthIngredient.ENCHANTED_POTATO;
			ItemStack item = ingredient.getItemStack();
			NamespacedKey key = ItemUtil.key(ingredient.name().toLowerCase());
			ShapedRecipe recipe = new ShapedRecipe(key, item);
			recipe.shape("ppp","pgp","ppp");
			recipe.setIngredient('p', Material.POTATO);
			recipe.setIngredient('g', Material.GOLD_NUGGET);
			Bukkit.addRecipe(recipe);
		}{
			GrowthIngredient ingredient = GrowthIngredient.ENCHANTED_CARROT;
			ItemStack item = ingredient.getItemStack();
			NamespacedKey key = ItemUtil.key(ingredient.name().toLowerCase());
			ShapedRecipe recipe = new ShapedRecipe(key, item);
			recipe.shape("ccc","cgc","ccc");
			recipe.setIngredient('c', Material.CARROT);
			recipe.setIngredient('g', Material.GOLD_NUGGET);
			Bukkit.addRecipe(recipe);
		}
	}
	
	@EventHandler
	public void onStriderMount(EntityMountEvent event){
		if(!(event.getEntity() instanceof Player) || !(event.getMount() instanceof Strider)){
			return;
		}
		TitleManagerAPI api = getCore().getIntegrationManager().getTitleManager();
		if(api == null){
			return;
		}
		api.sendTitles((Player)event.getEntity(), "§r", "§5This boat has legs!", 10, 50, 20);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		String deathMessage = event.getDeathMessage();
		if(deathMessage == null){
			return;
		}
		Profile profile = getCore().getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		
		deathMessage = " §4☠§r " + deathMessage.replaceAll(Pattern.quote(event.getEntity().getName()), profile.getDisplayName() + "§r");
		
		ScoreboardManager sbManager =  plugin.getServer().getScoreboardManager();
		if(sbManager != null){
			Objective objective = sbManager.getMainScoreboard().getObjective("deaths");
			if(objective != null){
				deathMessage += " §4#" + (objective.getScore(player.getName()).getScore() + 1);
			}
		}
		event.setDeathMessage(deathMessage);
	}
	
	public PvPHandler getPvPHandler() {
		return pvpHandler;
	}
	
	public AcceptRulesHandler getAcceptRulesHandler() {
		return acceptRulesHandler;
	}
	
	public VillagersHandler getVillagersHandler() {
		return villagersHandler;
	}
	
	public GrapplingHookHandler getGrapplingHookHandler() {
		return grapplingHookHandler;
	}
	
	/*public BloodHandler getBloodHandler() {
		return bloodHandler;
	}*/
	
	public DamageIndicatorHandler getDamageIndicatorHandler() {
		return damageIndicatorHandler;
	}
	
	public List<CustomEnchantment> getRegisteredEnchantments() {
		return registeredEnchantments;
	}
	
	public OreMinerHandler getOreMinerHandler() {
		return oreMinerHandler;
	}
	
	public LumberjackHandler getLumberjackHandler() {
		return lumberjackHandler;
	}
	
	public TelekinesisHandler getTelekinesisHandler() {
		return telekinesisHandler;
	}
	
	public GrowthHandler getGrowthHandler() {
		return growthHandler;
	}
	
	public OreSmeltingHandler getOreSmeltingHandler() {
		return oreSmeltingHandler;
	}
	
	public SpecialItemHandler getSpecialItemHandler() {
		return specialItemHandler;
	}
	
	public BasicEnchantmentHandler getBasicEnchantmentHandler() {
		return basicEnchantmentHandler;
	}
	
}
