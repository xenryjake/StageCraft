package com.xenry.stagecraft.survival.economy;
import com.xenry.stagecraft.command.PlayerCommand;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.M;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DiamondsCommand extends PlayerCommand<Survival,EconomyManager> {
	
	public DiamondsCommand(EconomyManager manager){
		super(manager, Rank.ADMIN, "diamonds");
		setDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 2){
			profile.sendMessage(M.usage("/" + label + " <add|remove> <amount>"));
			return;
		}
		args[0] = args[0].toLowerCase();
		boolean remove;
		if(args[0].startsWith("a")){
			remove = false;
		}else if(args[0].startsWith("r")){
			remove = true;
		}else{
			profile.sendMessage(M.error("Add or remove?"));
			return;
		}
		int amount;
		try{
			amount = Integer.parseInt(args[1]);
		}catch(Exception ex){
			profile.sendMessage(M.error("Invalid integer."));
			return;
		}
		if(remove){
			if(manager.removeDiamondsFromInventory(profile.getPlayer(), amount)){
				profile.sendMessage(M.msg + "Success: removed " + amount + " diamonds.");
			}else{
				profile.sendMessage(M.error("Failed! Not enough diamonds or severe error."));
			}
		}else{
			int n = manager.addDiamondsToInventory(profile.getPlayer(), amount);
			if(n == 0){
				profile.sendMessage(M.msg + "Success: added " + amount + " diamonds.");
			}else{
				profile.sendMessage(M.msg + "You didn't have room for " + n + " of the diamonds.");
			}
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
