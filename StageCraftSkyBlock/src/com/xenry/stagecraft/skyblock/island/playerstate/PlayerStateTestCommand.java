package com.xenry.stagecraft.skyblock.island.playerstate;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.island.IslandManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 12/27/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PlayerStateTestCommand extends PlayerCommand<SkyBlock,IslandManager> {
	
	public PlayerStateTestCommand(IslandManager manager){
		super(manager, Rank.ADMIN, "pst");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label){
		Player player = profile.getPlayer();
		if(args.length < 2){
			player.sendMessage(M.usage("/pst <get|set> <attribute> [value]"));
			return;
		}
		args[0] = args[0].toLowerCase();
		boolean set;
		if(args[0].startsWith("g")){
			set = false;
		}else if(args[0].startsWith("s")){
			set = true;
			if(args.length < 3){
				player.sendMessage(M.error("Please specify a value to set."));
				return;
			}
		}else{
			player.sendMessage(M.error("Invalid operation: " + args[0]));
			return;
		}
		if(args[1].equalsIgnoreCase("health")){
			if(set){
				double value;
				try{
					value = Double.parseDouble(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid double: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting health (double) to " + M.elm + value);
				player.setHealth(value);
			}else{
				player.sendMessage(M.msg + "Health (double): " + M.elm + player.getHealth());
			}
		}else if(args[1].equalsIgnoreCase("healthScale")){
			if(set){
				double value;
				try{
					value = Double.parseDouble(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid double: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting health scale (double) to " + M.elm + value);
				player.setHealthScale(value);
			}else{
				player.sendMessage(M.msg + "Health scale (double): " + M.elm + player.getHealthScale());
			}
		}else if(args[1].equalsIgnoreCase("foodLevel")){
			if(set){
				int value;
				try{
					value = Integer.parseInt(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid int: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting food level (int) to " + M.elm + value);
				player.setFoodLevel(value);
			}else{
				player.sendMessage(M.msg + "Food level (int): " + M.elm + player.getFoodLevel());
			}
		}else if(args[1].equalsIgnoreCase("exhaustion")){
			if(set){
				float value;
				try{
					value = Float.parseFloat(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid float: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting exhaustion (float) to " + M.elm + value);
				player.setExhaustion(value);
			}else{
				player.sendMessage(M.msg + "Exhaustion (float): " + M.elm + player.getExhaustion());
			}
		}else if(args[1].equalsIgnoreCase("saturation")){
			if(set){
				float value;
				try{
					value = Float.parseFloat(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid float: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting saturation (float) to " + M.elm + value);
				player.setSaturation(value);
			}else{
				player.sendMessage(M.msg + "Saturation (float): " + M.elm + player.getSaturation());
			}
		}else if(args[1].equalsIgnoreCase("remainingAir")){
			if(set){
				int value;
				try{
					value = Integer.parseInt(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid int: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting remaining air (int) to " + M.elm + value);
				player.setRemainingAir(value);
			}else{
				player.sendMessage(M.msg + "Remaining air (int): " + M.elm + player.getRemainingAir());
			}
		}else if(args[1].equalsIgnoreCase("maxAir")){
			if(set){
				int value;
				try{
					value = Integer.parseInt(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid int: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting max air (int) to " + M.elm + value);
				player.setMaximumAir(value);
			}else{
				player.sendMessage(M.msg + "Max air (int): " + M.elm + player.getMaximumAir());
			}
		}else if(args[1].equalsIgnoreCase("exp")){
			if(set){
				float value;
				try{
					value = Float.parseFloat(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid float: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting exp (float) to " + M.elm + value);
				player.setExp(value);
			}else{
				player.sendMessage(M.msg + "Exp (float): " + M.elm + player.getExp());
			}
		}else if(args[1].equalsIgnoreCase("level")){
			if(set){
				int value;
				try{
					value = Integer.parseInt(args[2]);
				}catch(Exception ex){
					player.sendMessage(M.error("Invalid int: " + args[2]));
					return;
				}
				player.sendMessage(M.msg + "Setting level (int) to " + M.elm + value);
				player.setLevel(value);
			}else{
				player.sendMessage(M.msg + "Level (int): " + M.elm + player.getLevel());
			}
		}else{
			player.sendMessage(M.error("Invalid attribute."));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		switch(args.length){
			case 1:
				return filter(Arrays.asList("get", "set"), args[0]);
			case 2:
				return filter(Arrays.asList("health", "healthScale", "foodLevel", "exhaustion", "saturation",
						"remainingAir", "maxAir", "exp", "level"), args[1]);
			default:
				return Collections.emptyList();
		}
	}
	
}
