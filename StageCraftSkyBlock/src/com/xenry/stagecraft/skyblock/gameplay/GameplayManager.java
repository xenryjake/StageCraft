package com.xenry.stagecraft.skyblock.gameplay;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.gameplay.commands.*;
import com.xenry.stagecraft.skyblock.gameplay.pvptoggle.PvPCommand;
import com.xenry.stagecraft.skyblock.gameplay.pvptoggle.PvPHandler;
import com.xenry.stagecraft.skyblock.gameplay.pvptoggle.PvPLockCommand;
import com.xenry.stagecraft.skyblock.gameplay.rules.AcceptRulesHandler;
import com.xenry.stagecraft.skyblock.gameplay.rules.RulesCommand;
import com.xenry.stagecraft.skyblock.gameplay.sign.SignCommand;
import com.xenry.stagecraft.skyblock.gameplay.sign.SignEditHandler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class GameplayManager extends Manager<SkyBlock> {
	
	private PvPHandler pvpHandler;
	private AcceptRulesHandler acceptRulesHandler;
	private SignEditHandler signEditHandler;
	
	public GameplayManager(SkyBlock plugin) {
		super("Gameplay", plugin);
	}
	
	@Override
	protected void onEnable() {
		pvpHandler = new PvPHandler(this);
		registerListener(pvpHandler);
		registerCommand(new PvPCommand(this));
		registerCommand(new PvPLockCommand(this));
		
		acceptRulesHandler = new AcceptRulesHandler(this);
		registerListener(acceptRulesHandler);
		registerCommand(new RulesCommand(this));
		
		signEditHandler = new SignEditHandler(this);
		registerListener(signEditHandler);
		registerCommand(new SignCommand(this));
		
		registerCommand(new AnvilCommand(this));
		registerCommand(new BookCommand(this));
		registerCommand(new BreakCommand(this));
		registerCommand(new BurnCommand(this));
		registerCommand(new CartographyTableCommand(this));
		registerCommand(new ClearInventoryCommand(this));
		registerCommand(new EnchantingTableCommand(this));
		registerCommand(new EnderChestCommand(this));
		registerCommand(new ExperienceCommand(this));
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
		registerCommand(new SpawnerCommand(this));
		registerCommand(new SpeedCommand(this));
		registerCommand(new StonecutterCommand(this));
		registerCommand(new SuicideCommand(this));
		registerCommand(new TimeCommand(this));
		registerCommand(new TrashCommand(this));
		registerCommand(new WeatherCommand(this));
		registerCommand(new WorkbenchCommand(this));
	}
	
	public PvPHandler getPvPHandler() {
		return pvpHandler;
	}
	
	public AcceptRulesHandler getAcceptRulesHandler() {
		return acceptRulesHandler;
	}
	
	public SignEditHandler getSignEditHandler() {
		return signEditHandler;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setGameMode(GameMode.SURVIVAL);
	}
	
}
