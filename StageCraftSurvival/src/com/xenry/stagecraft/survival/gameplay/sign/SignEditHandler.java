package com.xenry.stagecraft.survival.gameplay.sign;
import com.xenry.stagecraft.Handler;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.commands.Access;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.event.FakeBlockBreakEvent;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_16_R3.TileEntity;
import net.minecraft.server.v1_16_R3.TileEntitySign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 6/25/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SignEditHandler extends Handler<Survival,GameplayManager> {
	
	public static final Access COLOR_SIGNS = Rank.PREMIUM;
	public static final List<Material> DYE_MATERIALS = Arrays.asList(Material.BLACK_DYE, Material.BLUE_DYE,
			Material.BROWN_DYE, Material.CYAN_DYE, Material.GRAY_DYE, Material.GREEN_DYE, Material.LIGHT_BLUE_DYE,
			Material.LIGHT_GRAY_DYE, Material.LIME_DYE, Material.MAGENTA_DYE, Material.ORANGE_DYE, Material.PINK_DYE,
			Material.PURPLE_DYE, Material.RED_DYE, Material.WHITE_DYE, Material.YELLOW_DYE);
	
	public SignEditHandler(GameplayManager manager){
		super(manager);
	}
	
	@EventHandler
	public void onSignEdit(SignChangeEvent event){
		Player player = event.getPlayer();
		Profile profile = getCore().getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		for(int i = 0; i < event.getLines().length; i++){
			String line = event.getLine(i);
			if(line == null){
				continue;
			}
			if(COLOR_SIGNS.has(profile)){
				line = ChatColor.translateAlternateColorCodes('&', line);
			}
			if(Emote.EMOTE_ACCESS.has(profile)){
				line = Emote.replaceEmotes(line);
			}
			event.setLine(i, line);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Profile profile = getCore().getProfileManager().getProfile(player);
		if(profile == null){
			return;
		}
		if(player.isSneaking() || event.getAction() != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		if(DYE_MATERIALS.contains(player.getInventory().getItemInMainHand().getType())){
			return;
		}
		Block block = event.getClickedBlock();
		if(block == null){
			return;
		}
		BlockState blockState = block.getState();
		if (!(blockState instanceof Sign)){
			return;
		}
		if(checkPermission(player, block)){
			openSignEditor(player, (Sign)blockState);
		}
	}
	
	public void openSignEditor(Player player, Sign sign){
		
		// remove colors, replace with '&' color codes
		CraftSign craftSign = (CraftSign)sign;
		for(int i = 0; i < craftSign.getLines().length; i++){
			String line = craftSign.getLine(i);
			if(ChatColor.stripColor(line).trim().isEmpty()){
				craftSign.setLine(i, "");
			}else{
				craftSign.setLine(i, line.replace('§', '&'));
			}
		}
		craftSign.setEditable(true);
		craftSign.update();
		
		Location loc = craftSign.getLocation();
		BlockPosition position = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		// make sign editable
		TileEntity tileEntity = ((CraftWorld)sign.getWorld()).getHandle().getTileEntity(position);
		if(!(tileEntity instanceof TileEntitySign)){
			return;
		}
		
		try {
			TileEntitySign tes = ((TileEntitySign)tileEntity);
			tes.isEditable = true;
			tes.signEditor = player.getUniqueId();
			/*Field c = tes.getClass().getDeclaredField("c");
			c.setAccessible(true);
			c.set(tes, ((CraftPlayer)player).getHandle());*/
			
			PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(position);
			Bukkit.getScheduler().runTaskLater(manager.plugin,
					() -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet), 2L);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean checkPermission(Player player, Block block){
		FakeBlockBreakEvent bbe = new FakeBlockBreakEvent(block, player);
		manager.plugin.getServer().getPluginManager().callEvent(bbe);
		return !bbe.isCancelled();
	}
	
}
