package com.xenry.stagecraft.creative.gameplay;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.armorstand.ArmorStandHandler;
import com.xenry.stagecraft.creative.gameplay.commands.*;
import com.xenry.stagecraft.creative.gameplay.armorstand.commands.ArmorStandCommand;
import com.xenry.stagecraft.creative.gameplay.pvptoggle.PvPCommand;
import com.xenry.stagecraft.creative.gameplay.pvptoggle.PvPHandler;
import com.xenry.stagecraft.creative.gameplay.pvptoggle.PvPLockCommand;
import com.xenry.stagecraft.creative.gameplay.rules.AcceptRulesHandler;
import com.xenry.stagecraft.creative.gameplay.rules.RulesCommand;
import com.xenry.stagecraft.creative.gameplay.sign.SignCommand;
import com.xenry.stagecraft.creative.gameplay.sign.SignEditHandler;
import com.xenry.stagecraft.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/3/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GameplayManager extends Manager<Creative> {
	
	private final List<String> playerOverrides;
	private final BlacklistHandler blacklistHandler;
	private final PvPHandler pvpHandler;
	private final SignEditHandler signEditHandler;
	private final ProtectionHandler protectionHandler;
	private final EntityHandler entityHandler;
	private final AcceptRulesHandler acceptRulesHandler;
	private final ArmorStandHandler armorStandHandler;
	
	private boolean lockoutMode = false;
	
	public GameplayManager(Creative creative){
		super("Gameplay", creative);
		playerOverrides = new ArrayList<>();
		blacklistHandler = new BlacklistHandler(this);
		pvpHandler = new PvPHandler(this);
		signEditHandler = new SignEditHandler(this);
		protectionHandler = new ProtectionHandler(this);
		entityHandler = new EntityHandler(this);
		acceptRulesHandler = new AcceptRulesHandler(this);
		armorStandHandler = new ArmorStandHandler(this);
	}
	
	@Override
	protected void onEnable() {
		registerListener(blacklistHandler);
		
		registerListener(pvpHandler);
		registerCommand(new PvPCommand(this));
		registerCommand(new PvPLockCommand(this));
		
		registerListener(signEditHandler);
		registerCommand(new SignCommand(this));
		
		registerListener(protectionHandler);
		registerListener(entityHandler);
		
		registerListener(acceptRulesHandler);
		registerCommand(new RulesCommand(this));
		
		registerListener(armorStandHandler);
		registerCommand(new ArmorStandCommand(armorStandHandler));
		
		registerCommand(new LockoutCommand(this));
		registerCommand(new OverrideCommand(this));
		
		registerCommand(new AnvilCommand(this));
		registerCommand(new BookCommand(this));
		registerCommand(new BreakCommand(this));
		registerCommand(new BurnCommand(this));
		registerCommand(new CartographyTableCommand(this));
		registerCommand(new ClearInventoryCommand(this));
		registerCommand(new EnchantingTableCommand(this));
		registerCommand(new EnderChestCommand(this));
		registerCommand(new ExperienceCommand(this));
		registerCommand(new ExtinguishCommand(this));
		registerCommand(new FeedCommand(this));
		registerCommand(new FlyCommand(this));
		registerCommand(new GameModeCommand(this));
		registerCommand(new GrindstoneCommand(this));
		registerCommand(new HealCommand(this));
		registerCommand(new ItemLoreCommand(this));
		registerCommand(new ItemNameCommand(this));
		registerCommand(new KillCommand(this));
		registerCommand(new LightningCommand(this));
		registerCommand(new LoomCommand(this));
		registerCommand(new MoreCommand(this));
		registerCommand(new NearCommand(this));
		registerCommand(new RepairCommand(this));
		registerCommand(new RestCommand(this));
		registerCommand(new SkullCommand(this));
		registerCommand(new SmithingTableCommand(this));
		registerCommand(new SmithingTableCommand(this));
		registerCommand(new SpeedCommand(this));
		registerCommand(new StonecutterCommand(this));
		registerCommand(new SuicideCommand(this));
		registerCommand(new TimeCommand(this));
		registerCommand(new TrashCommand(this));
		registerCommand(new WeatherCommand(this));
		registerCommand(new WorkbenchCommand(this));
		registerCommand(new MobCannonCommand(this));
		
		armorStandHandler.downloadPoses();
	}
	
	@Override
	protected void onDisable() {
		armorStandHandler.saveAllPosesSync();
	}
	
	public BlacklistHandler getBlacklistHandler() {
		return blacklistHandler;
	}
	
	public PvPHandler getPvPHandler() {
		return pvpHandler;
	}
	
	public SignEditHandler getSignEditHandler() {
		return signEditHandler;
	}
	
	public ProtectionHandler getProtectionHandler() {
		return protectionHandler;
	}
	
	public EntityHandler getEntityHandler() {
		return entityHandler;
	}
	
	public AcceptRulesHandler getAcceptRulesHandler() {
		return acceptRulesHandler;
	}
	
	public ArmorStandHandler getArmorStandHandler() {
		return armorStandHandler;
	}
	
	public List<String> getPlayerOverrides() {
		return playerOverrides;
	}
	
	public boolean isPlayerOverride(Player player){
		return playerOverrides.contains(player.getUniqueId().toString());
	}
	
	public void setPlayerOverride(Player player, boolean state){
		String uuid = player.getUniqueId().toString();
		if(!state){
			playerOverrides.remove(uuid);
		}else if(!playerOverrides.contains(uuid)){
			playerOverrides.add(uuid);
		}
	}
	
	public boolean isLockoutMode() {
		return lockoutMode;
	}
	
	public void setLockoutMode(boolean lockoutMode, String senderName) {
		this.lockoutMode = lockoutMode;
		String enableDisable = lockoutMode ? "§aenabled§b" : "§cdisabled§b";
		Log.toCS("");
		Log.toCS("§e" + name + "§b has " + enableDisable + " lockout mode.");
		Log.toCS("");
		for(Player player : Bukkit.getOnlinePlayers()){
			if(LockoutCommand.ACCESS.has(player)){
				player.sendMessage("\n§e" + senderName + "§b has " + enableDisable + " lockout mode.\n\n");
			}else{
				player.sendMessage("\n§bLockout mode has been " + enableDisable + "\n\n");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setGameMode(GameMode.CREATIVE);
	}
	
}
