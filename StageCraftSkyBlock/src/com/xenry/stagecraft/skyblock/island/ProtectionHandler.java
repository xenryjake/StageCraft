package com.xenry.stagecraft.skyblock.island;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.util.Cooldown;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class ProtectionHandler extends Handler<SkyBlock,IslandManager> {
	
	/**
	 * player events to handle:
	 * 		BlockBreakEvent
	 * 		BlockPlaceEvent
	 * 		InventoryClickEvent
	 * 		InventoryInteractEvent
	 * 		PlayerDropItemEvent ?
	 * 		PlayerInteractEvent
	 *
	 * 	other events to handle:
	 * 		PortalCreateEvent ?
	 * 		BlockIgniteEvent ?
	 * 		BlockPistonExtendEvent
	 * 		BlockPistonRetractEvent
	 *		BlockFromToEvent ?
	 *		BlockShearEntityEvent ?
	 *		CauldronChangeLevelEvent ?
	 *		FluidLevelChangeEvent ?
	 *		LeavesDecayEvent ?
	 *		MoistureChangeEvent
	 *		NotePlayEvent
	 *		SpongeAbsorbEvent
	 *		InventoryPickupEvent
	 *		BlockGrowEvent
	 *		BlockSpreadEvent
	 *		EntityExplodeEvent
	 *		BlockExplodeEvent
	 *		BlockBurnEvent
	 *		BlockFadeEvent
	 *		EntityBlockFormEvent
	 */
	
	private final Cooldown messageCooldown;
	
	public ProtectionHandler(IslandManager manager) {
		super(manager);
		messageCooldown = new Cooldown(5000, null);
	}
	
	private void sendMessage(Player player, String message){
		if(!messageCooldown.use(player, false)){
			return;
		}
		player.sendMessage(message);
	}
	
	// prevents players from breaking blocks
	@EventHandler(ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Island island = manager.getIsland(block.getLocation());
		if(island == null){
			return;
		}
		if(!island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't break blocks on this island.");
		}else if(!island.isInBuildArea(block.getLocation())){
			event.setCancelled(true);
			sendMessage(player, "You can't break blocks here.");
		}
	}
	
	// prevents players from harvesting crops without breaking them
	@EventHandler(ignoreCancelled = true)
	public void onHarvest(PlayerHarvestBlockEvent event){
		Player player = event.getPlayer();
		Block block = event.getHarvestedBlock();
		Island island = manager.getIsland(block.getLocation());
		if(island == null){
			return;
		}
		if(!island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't harvest blocks on this island.");
		}else if(!island.isInBuildArea(block.getLocation())){
			event.setCancelled(true);
			sendMessage(player, "You can't harvest blocks here.");
		}
	}
	
	// prevents players from placing blocks
	@EventHandler(ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Island island = manager.getIsland(block.getLocation());
		if(island == null){
			return;
		}
		if(!island.isMember(player)) {
			event.setCancelled(true);
			sendMessage(player, "You can't place blocks on this island.");
		}else if(!island.isInIsland(block.getLocation())){
			event.setCancelled(true);
			sendMessage(player, "You can't place blocks here.");
		}
	}
	
	// prevents players from general interactions with blocks
	@EventHandler(ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null){
			return;
		}
		Island island = manager.getIsland(block.getLocation());
		if(island != null && !island.isMember(player)) {
			event.setCancelled(true);
			sendMessage(player, "You can't interact with this island.");
		}
	}
	
	// prevents players from general interactions with other entities
	@EventHandler(ignoreCancelled = true)
	public void onEntityInteract(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		Island island = manager.getIsland(entity.getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
		}
	}
	
	// prevents players from shearing entities
	@EventHandler(ignoreCancelled = true)
	public void onShear(PlayerShearEntityEvent event){
		Player player = event.getPlayer();
		Entity entity = event.getEntity();
		Island island = manager.getIsland(entity.getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't shear mobs on this island.");
		}
	}
	
	// prevents players from taking books from lecterns
	@EventHandler(ignoreCancelled = true)
	public void onLecternTakeBook(PlayerTakeLecternBookEvent event){
		Player player = event.getPlayer();
		Block block = event.getLectern().getBlock();
		Island island = manager.getIsland(block.getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't take books from lecterns on this island.");
		}
	}
	
	// prevents players from placing paintings, item frames
	@EventHandler(ignoreCancelled = true)
	public void onHangingPlace(HangingPlaceEvent event){
		Player player = event.getPlayer();
		if(player == null){
			return;
		}
		Entity entity = event.getEntity();
		Island island = manager.getIsland(entity.getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't place hanging entities on this island.");
		}
	}
	
	// prevents players from breaking paintings, item frames
	@EventHandler(ignoreCancelled = true)
	public void onHangingBreak(HangingBreakByEntityEvent event){
		Entity entity = event.getRemover();
		if(!(entity instanceof Player)){
			return;
		}
		Player player = (Player)entity;
		Island island = manager.getIsland(entity.getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't break hanging entities on this island.");
		}
	}
	
	// prevents players from leashing mobs
	@EventHandler(ignoreCancelled = true)
	public void onLeash(PlayerLeashEntityEvent event){
		Player player = event.getPlayer();
		Island island = manager.getIsland(event.getEntity().getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't leash entities on this island.");
		}
	}
	
	// prevents players from destroying vehicles
	@EventHandler(ignoreCancelled = true)
	public void onVehicleDestroy(VehicleDestroyEvent event){
		Entity entity = event.getAttacker();
		if(!(entity instanceof Player)){
			return;
		}
		Player player = (Player)entity;
		Island island = manager.getIsland(event.getVehicle().getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't break vehicles on this island.");
		}
	}
	
	// prevents players from punching vehicles
	@EventHandler(ignoreCancelled = true)
	public void onVehicleDamage(VehicleDamageEvent event){
		Entity entity = event.getAttacker();
		if(!(entity instanceof Player)){
			return;
		}
		Player player = (Player)entity;
		Island island = manager.getIsland(event.getVehicle().getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
			sendMessage(player, "You can't break vehicles on this island.");
		}
	}
	
	// prevents entity explosions from destroying blocks
	@EventHandler(ignoreCancelled = true)
	public void on(EntityExplodeEvent event){
		if(!event.blockList().isEmpty()){
			event.setCancelled(true);
			World world = event.getLocation().getWorld();
			if(world != null){
				world.createExplosion(event.getLocation(), 0);
			}
		}
	}
	
	// prevents block explosions from destroying other blocks
	@EventHandler(ignoreCancelled = true)
	public void on(BlockExplodeEvent event){
		if(!event.blockList().isEmpty()){
			event.setCancelled(true);
			event.getBlock().getWorld().createExplosion(event.getBlock().getLocation(), 0);
		}
	}
	
	//this handles things such as the frost walker enchantment
	@EventHandler(ignoreCancelled = true)
	public void on(EntityBlockFormEvent event){
		Entity entity = event.getEntity();
		if(!(entity instanceof Player)){
			return;
		}
		Player player = (Player)entity;
		Island island = manager.getIsland(event.getBlock().getLocation());
		if(island != null && !island.isMember(player)){
			event.setCancelled(true);
		}
	}
	
}
