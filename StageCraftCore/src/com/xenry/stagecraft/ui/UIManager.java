package com.xenry.stagecraft.ui;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.ui.item.Button;
import com.xenry.stagecraft.ui.item.Item;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class UIManager extends Manager<Core> {
	
	private final Map<Player,Menu<?,?>> menus;
	private final Map<Player,MenuContents> contents;
	
	private final List<Opener> openers;
	private BukkitTask tickTask;

	public UIManager(Core plugin){
		super("UI", plugin);
		
		menus = new HashMap<>();
		contents = new HashMap<>();
		
		openers = new ArrayList<>();
		openers.add(new ChestOpener(this));
		openers.add(new SpecialOpener(this));
	}
	
	@Override
	protected void onEnable() {
		tickTask = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
			for(Map.Entry<Player,Menu<?,?>> entry : menus.entrySet()){
				entry.getValue().tick(entry.getKey(), contents.get(entry.getKey()));
			}
		}, 1, 1);
	}
	
	@Override
	protected void onDisable() {
		tickTask.cancel();
		for(Map.Entry<Player,Menu<?,?>> entry : menus.entrySet()){
			entry.getValue().close(entry.getKey());
		}
		menus.clear();
		contents.clear();
	}
	
	@Nullable
	public Opener getOpener(InventoryType type){
		for(Opener opener : openers){
			if(opener.supports(type)){
				return opener;
			}
		}
		return null;
	}
	
	public List<Player> getOpenedPlayers(Menu<?,?> menu){
		List<Player> players = new ArrayList<>();
		for(Map.Entry<Player,Menu<?,?>> entry : menus.entrySet()){
			if(menu.equals(entry.getValue())){
				players.add(entry.getKey());
			}
		}
		return players;
	}
	
	@Nullable
	public Menu<?,?> getOpenMenu(Player player){
		return menus.getOrDefault(player, null);
	}
	
	protected void setOpenMenu(@NotNull Player player, @Nullable Menu<?,?> menu){
		if(menu == null){
			menus.remove(player);
		}else{
			menus.put(player, menu);
		}
	}
	
	@Nullable
	public MenuContents getOpenContents(Player player){
		return contents.getOrDefault(player, null);
	}
	
	protected void setOpenContents(@NotNull Player player, @Nullable MenuContents menuContents){
		if(menuContents == null){
			contents.remove(player);
		}else{
			contents.put(player, menuContents);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Menu<?,?> menu = menus.get(player);
		if(menu != null){
			menu.onQuit(event);
			menus.remove(player);
			contents.remove(player);
		}
	}
	
	/*@EventHandler
	public void onOpen(InventoryOpenEvent event){
		HumanEntity human = event.getPlayer();
		if(!(human instanceof Player)){
			return;
		}
		menus.get(human).onOpen(event);
	}*/
	
	@EventHandler
	public void onClose(InventoryCloseEvent event){
		HumanEntity human = event.getPlayer();
		if(!(human instanceof Player)){
			return;
		}
		Player player = (Player)human;
		Menu<?,?> menu = menus.get(player);
		if(menu == null){
			return;
		}
		menu.onClose(event);
		if(menu.isClosable()){
			event.getInventory().clear();
			menus.remove(player);
			contents.remove(player);
		}else{
			plugin.getServer().getScheduler().runTask(plugin, () -> player.openInventory(event.getInventory()));
		}
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent event){
		HumanEntity human = event.getWhoClicked();
		if(!(human instanceof Player)){
			return;
		}
		Player player = (Player)human;
		Menu<?,?> menu = menus.get(player);
		if(menu == null){
			return;
		}
		for(int slot : event.getRawSlots()){
			if(slot < player.getOpenInventory().getTopInventory().getSize()){
				event.setCancelled(true);
				break;
			}
		}
		menu.onDrag(event);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		HumanEntity human = event.getWhoClicked();
		if(!(human instanceof Player)){
			return;
		}
		Player player = (Player)human;
		Menu<?,?> menu = menus.get(player);
		if(menu == null){
			return;
		}
		//if(!menu.canMoveItems()){
		event.setCancelled(true);
		//}*/
		if(event.getAction() == InventoryAction.COLLECT_TO_CURSOR
				|| (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY &&
				(!(event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT)
						|| event.getClickedInventory() == player.getOpenInventory().getBottomInventory()))
				|| (event.getAction() == InventoryAction.NOTHING && event.getClick() != ClickType.MIDDLE)){
			return;
		}
		if(event.getClickedInventory() == player.getOpenInventory().getTopInventory()){
			int row = event.getSlot() / 9;
			int col = event.getSlot() % 9;
			if(row < 0 || col < 0 || row >= menu.rows || col >= menu.cols){
				return;
			}
			Item item = contents.get(player).get(row, col);
			menu.onClick(event);
			if(item instanceof Button){
				event.setCancelled(true);
				((Button)item).onClick(event);
			}
			player.updateInventory();
		}else{
			menu.onClickOther(event);
		}
	}
	
}
