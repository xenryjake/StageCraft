package com.xenry.stagecraft.integration;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.earth2me.essentials.Essentials;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.util.Log;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class IntegrationManager extends Manager<Core> {
	
	private ProtocolManager protocolManager;
	private Economy vaultEconomy;
	private Essentials essentials;
	private TitleManagerAPI titleManager;
	private GriefPrevention griefPrevention;
	
	public IntegrationManager(Core plugin){
		super("Integration", plugin, false);
	}
	
	@Override
	protected void onEnable() {
		if(setupProtocolLib()){
			Log.info("Successfully hooked into ProtocolLib.");
		}else{
			Log.warn("ProtocolLib not found. ProtocolLib integration failed.");
		}
		if(setupEconomy()){
			Log.info("Successfully hooked into Vault Economy.");
		}else{
			Log.warn("Vault not found. VaultEconomy integration failed.");
		}
		if(setupEssentials()){
			Log.info("Successfully hooked into Essentials.");
		}else{
			Log.warn("Essentials not found. Essentials integration failed.");
		}
		if(setupTitleManager()){
			Log.info("Successfully hooked into TitleManager.");
		}else{
			Log.warn("TitleManager not found. TitleManager integration failed.");
		}
		if(setupGriefPrevention()){
			Log.info("Successfully hooked into GriefPrevention.");
		}else{
			Log.warn("GriefPrevention not found. GriefPrevention integration failed.");
		}
	}
	
	private boolean setupProtocolLib(){
		if(plugin.getServer().getPluginManager().getPlugin("ProtocolLib") == null){
			protocolManager = null;
			return false;
		}
		protocolManager = ProtocolLibrary.getProtocolManager();
		return protocolManager != null;
	}
	
	private boolean setupEconomy(){
		if(plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
			vaultEconomy = null;
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		vaultEconomy = rsp.getProvider();
		return true;
	}
	
	private boolean setupEssentials(){
		if(plugin.getServer().getPluginManager().getPlugin("Essentials") == null){
			essentials = null;
			return false;
		}
		essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
		return essentials != null;
	}
	
	private boolean setupTitleManager(){
		Plugin tmPlugin = Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
		if(tmPlugin == null){
			titleManager = null;
			return false;
		}
		if(tmPlugin instanceof TitleManagerAPI){
			titleManager = (TitleManagerAPI)tmPlugin;
		}else{
			titleManager = null;
			return false;
		}
		return true;
	}
	
	private boolean setupGriefPrevention(){
		if(plugin.getServer().getPluginManager().getPlugin("GriefPrevention") == null){
			griefPrevention = null;
			return false;
		}
		griefPrevention = GriefPrevention.instance;
		return griefPrevention != null;
	}
	
	@Nullable
	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}
	
	@Nullable
	public Economy getVaultEconomy() {
		return vaultEconomy;
	}
	
	@Nullable
	public Essentials getEssentials() {
		return essentials;
	}
	
	@Nullable
	public TitleManagerAPI getTitleManager() {
		return titleManager;
	}
	
	@Nullable
	public GriefPrevention getGriefPrevention() {
		return griefPrevention;
	}
	
}
