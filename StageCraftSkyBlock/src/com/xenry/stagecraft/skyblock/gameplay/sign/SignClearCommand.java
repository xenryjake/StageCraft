package com.xenry.stagecraft.skyblock.gameplay.sign;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.skyblock.SkyBlock;
import com.xenry.stagecraft.skyblock.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SignClearCommand extends PlayerCommand<SkyBlock,GameplayManager> {
	
	public SignClearCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "clear");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Integer line = null;
		if(args.length > 0 && !args[0].equalsIgnoreCase("all")){
			try{
				line = Integer.parseInt(args[0]);
			}catch(Exception ex){
				profile.sendMessage(M.error("Invalid integer: " + args[0]));
				return;
			}
		}
		if(line != null && (line < 1 || line > 4)){
			profile.sendMessage(M.error("Line must be between 1 and 4."));
			return;
		}
		Player player = profile.getPlayer();
		Block block = player.getTargetBlock(null, 5);
		if(!(block.getState() instanceof Sign)){
			profile.sendMessage(M.error("You aren't looking at a sign."));
			return;
		}
		
		if(!manager.getSignEditHandler().checkPermission(player, block)){
			return;
		}
		
		Sign sign = (Sign)block.getState();
		
		if(line == null){
			sign.setLine(0, "");
			sign.setLine(1, "");
			sign.setLine(2, "");
			sign.setLine(3, "");
		}else{
			sign.setLine(line - 1, "");
		}
		sign.update();
		profile.sendMessage(M.msg + "Cleared " + (line == null ? "all lines" : "line " + line) + " from the sign.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? filter(Arrays.asList("all", "1", "2", "3", "4"), args[0]) : Collections.emptyList();
	}
	
}
