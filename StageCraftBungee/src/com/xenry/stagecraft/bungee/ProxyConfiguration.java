package com.xenry.stagecraft.bungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/9/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProxyConfiguration {
	
	private final Bungee plugin;
	private final ConfigurationProvider provider;
	private final File file;
	private Configuration configuration;
	
	public ProxyConfiguration(Bungee plugin){
		this.plugin = plugin;
		provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
		file = new File(plugin.getDataFolder(), "config.yml");
		saveDefaultConfig();
		load();
	}
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void saveDefaultConfig(){
		if(!plugin.getDataFolder().exists()){
			plugin.getDataFolder().mkdir();
		}
		if(!file.exists()){
			try{
				InputStream in = plugin.getResourceAsStream("config.yml");
				Files.copy(in, file.toPath());
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void save(){
		try{
			provider.save(configuration, file);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public void load(){
		try{
			configuration = provider.load(file);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	Configuration getConfiguration() {
		return configuration;
	}
	
	public boolean isDebug(){
		return configuration.getBoolean("debug");
	}
	
	public boolean betaFeaturesEnabled(){
		return configuration.getBoolean("beta-features");
	}
	
	public String getMOTD(){
		return ChatColor.translateAlternateColorCodes('&',
				configuration.getString("motd1") + "\n§r" + configuration.getString("motd2"));
	}
	
	public TextComponent getMOTDComponent(){
		return new TextComponent(TextComponent.fromLegacyText(getMOTD()));
	}
	
}
